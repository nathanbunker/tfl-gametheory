package com.techieforlife.gt;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ServerServlet extends HttpServlet {

  private static Map<Integer, Game> gamesInPlayMap = new HashMap<>();
  private static int gameId = 0;
  private static Map<String, Map<String, Player>> playerMap = new HashMap<>();
  private static Set<Game> pendingGameSet = new HashSet<>();

  private static Player getPlayer(String playerName, String serverName) {
    synchronized (playerMap) {
      Map<String, Player> serverMap = playerMap.get(serverName);
      if (serverMap == null) {
        serverMap = new HashMap<>();
        playerMap.put(serverName, serverMap);
      }
      Player player = serverMap.get(playerName);
      if (player == null) {
        player = new Player(playerName, serverName);
        serverMap.put(playerName, player);
      }
      return player;
    }
  }

  private static Game getNextGame(Player player) {
    synchronized (pendingGameSet) {
      if (pendingGameSet.size() == 0) {
        return null;
      }
      Game nextGame = null;
      for (Game game : pendingGameSet) {
        if (!game.getPlayerA().equals(player)) {
          nextGame = game;
          break;
        }
      }
      if (nextGame != null) {
        pendingGameSet.remove(nextGame);
        nextGame.setPlayerB(player);
        gameId++;
        nextGame.setGameId(gameId);
        gamesInPlayMap.put(gameId, nextGame);
      }
      return nextGame;
    }
  }

  private static void addPendingGame(Game game) {
    synchronized (pendingGameSet) {
      pendingGameSet.add(game);
    }
  }

  private static Game getGame(Player player) {
    Game game = getNextGame(player);
    if (game == null) {
      game = new Game();
      game.setPlayerA(player);
      synchronized (game) {
        addPendingGame(game);
        try {
          game.wait();
        } catch (InterruptedException e) {
          //
        }
      }
    } else {
      synchronized (game) {
        game.notify();
      }
    }
    return game;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String serverName = readServerName(req);
    resp.setContentType("test/plain");
    String command = req.getParameter("command");
    if (command == null) {
      command = "";
    }
    ConverseRequest converseRequest = new ConverseRequest();
    converseRequest.setRequest(command);
    converseRequest.setServerName(serverName);
    resp.getWriter().println(converse(converseRequest));
    resp.getWriter().close();
  }

  private String readServerName(HttpServletRequest req) {
    String serverName;
    {
      serverName = req.getRequestURI();
      int i = serverName.lastIndexOf("/");
      if (i >= 0) {
        serverName = serverName.substring(i).trim();
        if (serverName.startsWith("/")) {
          serverName = serverName.substring(1).trim();
        }
        try {
          serverName = URLDecoder.decode(serverName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      if (serverName.equals("")) {
        serverName = "the Cloud";
      }
    }
    return serverName;
  }

  private String converse(ConverseRequest converseRequest) {
    String s = converseRequest.getRequest().trim();

    if (s.indexOf("wants to play") > 0) {
      // "A" wants to play --- "Prisoner's Dilenma"
      // "A" can play "B" in game #1
      String playerName = getNextQuotedString(s);
      if (playerName != null) {
        converseRequest.setPlayerName(playerName);
        Player playerA = getPlayer(playerName, converseRequest.getServerName());
        Game game = getGame(playerA);
        converseRequest.setGame(game);
        return "\"" + playerName + "\" can play \"" + game.getOtherPlayer(playerA).getPlayerWithServerName()
            + "\" in game #" + game.getGameId();
      }
    } else {
      // "A" plays "Confess" in game #1
      // "B" plays "Confess", "A" receives -2, "B" receives -2

      int gameId = Integer.parseInt(s.substring(s.indexOf('#') + 1));
      Game game = gamesInPlayMap.get(gameId);
      converseRequest.setGame(game);
      if (game == null) {
        return "Oops, game is already over, sorry. ";
      }
      String playerName = getNextQuotedString(s);
      if (playerName == null) {
        return "No player name, what gives? ";
      }
      converseRequest.setPlayerName(playerName);
      Player player = getPlayer(playerName, converseRequest.getServerName());
      int playsIn = s.indexOf(" plays ");
      if (playsIn == -1) {
        return "Who are you playing against? ";
      }
      s = s.substring(playsIn);
      String play = getNextQuotedString(s);
      game.setPlay(play, player);
      synchronized (game) {
        if (game.bothHavePlayed()) {
          {
            game.getPlayerA().addScore(game.getScoreA());
            game.getPlayerA().incPlayCount();
          }
          {
            game.getPlayerB().addScore(game.getScoreB());
            game.getPlayerB().incPlayCount();
          }
          gamesInPlayMap.remove(game.getGameId());
          game.notify();
        } else {
          while (!game.bothHavePlayed()) {
            try {
              game.wait();
            } catch (InterruptedException e) {
            }
          }
        }
      }
      return "\"" + game.getOtherPlayer(player) + "\" plays \"" + game.getOtherPlay(player) + "\" for "
          + game.getOtherScore(player) + " points and \"" + game.getPlayer(player) + "\" plays \""
          + game.getPlay(player) + "\" for " + game.getScore(player) + ".";
    }

    return "I don't know what you want";
  }

  private String getNextQuotedString(String s) {
    String playerName = null;
    int q1 = s.indexOf('"');
    if (q1 >= 0) {
      int q2 = s.indexOf('"', q1 + 1);
      if (q2 > 0) {
        playerName = s.substring(q1 + 1, q2);
      }
    }
    return playerName;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String serverName = readServerName(req);
    String command = req.getParameter("command");

    PrintWriter out = resp.getWriter();
    out.println("<!doctype html>");
    out.println("<html lang=\"en\">");
    out.println("  <head>");
    out.println("    <title>Game Theory Server for " + serverName + "</title>");
    out.println("  </head>");
    out.println("  <body>");
    out.println("    <h1>Game Theory Server for " + serverName + "</h1>");
    ConverseRequest converseRequest = null;
    if (command != null) {
      converseRequest = new ConverseRequest();
      converseRequest.setRequest(command);
      converseRequest.setServerName(serverName);
      out.println("    <p>" + converse(converseRequest) + "</p>");
    }
    String serverNameEncoded = URLEncoder.encode(serverName, "UTF-8");
    out.println("    <form action=\"" + serverNameEncoded + "\" method=\"GET\">");
    out.println("      <input type=\"text\" name=\"command\" size=\"40\"/>");
    out.println("      <input type=\"submit\" name=\"action\" value=\"Submit\"/>");
    out.println("    </form>");
    if (converseRequest != null && converseRequest.getGame() != null) {
      Game game = converseRequest.getGame();
      if (!game.bothHavePlayed()) {
        String commandConfess = "\"" + converseRequest.getPlayerName() + "\" plays \"Confess\" in game #"
            + game.getGameId();
        String commandBeQuiet = "\"" + converseRequest.getPlayerName() + "\" plays \"Be Quiet\" in game #"
            + game.getGameId();
        out.println("<p><a href=\"" + serverNameEncoded + "?command=" + URLEncoder.encode(commandConfess, "UTF-8")
            + "\">Confess</a></p>");
        out.println("<p><a href=\"" + serverNameEncoded + "?command=" + URLEncoder.encode(commandBeQuiet, "UTF-8")
            + "\">Be Quiet</a></p>");
      }
    }
    out.println("    Example commands:");
    out.println("    <pre>");
    out.println("\"Sam\" wants to play");
    out.println("\"Sam\" plays \"Confess\" in game #1");
    out.println("\"Sam\" plays \"Be Quiet\" in game #1");
    out.println("</pre>");
    synchronized (pendingGameSet) {
      if (pendingGameSet.size() > 0) {
        out.println("   <h2>Player" + (pendingGameSet.size() > 1 ? "s" : "") + " Waiting</h2>");
        for (Game pendingGame : pendingGameSet) {
          out.println("<p>" + pendingGame.getPlayerA() + " is waiting to play :( </p>");
        }
      }
    }
    out.println("   <h2>In Play</h2>");
    for (Game game : gamesInPlayMap.values()) {
      out.println("<p>" + game.getPlayerA() + " is playing with " + game.getPlayerB() + " in game #" + game.getGameId()
          + "</p>");
      if (game.getPlayA() != null || game.getPlayB() != null) {
        out.println("<ul>");
        if (game.getPlayA() != null) {
          out.println("<li>" + game.getPlayerA() + " plays " + game.getPlayA() + "</li>");
        } else {
          String sn = URLEncoder.encode(game.getPlayerA().getServerName(), "UTF-8");
          String commandConfess = "\"" + game.getPlayerA().getPlayerName() + "\" plays \"Confess\" in game #"
              + game.getGameId();
          String commandBeQuiet = "\"" + game.getPlayerA().getPlayerName() + "\" plays \"Be Quiet\" in game #"
              + game.getGameId();
          out.println("<li><a href=\"" + sn + "?command=" + URLEncoder.encode(commandConfess, "UTF-8") + "\">"
              + game.getPlayerA() + " plays Confess</a></li>");
          out.println("<li><a href=\"" + sn + "?command=" + URLEncoder.encode(commandBeQuiet, "UTF-8") + "\">"
              + game.getPlayerA() + " plays Be Quiet</a></li>");
        }
        if (game.getPlayB() != null) {
          out.println("<li>" + game.getPlayerB() + " plays " + game.getPlayB() + "</li>");
        } else {
          String sn = URLEncoder.encode(game.getPlayerB().getServerName(), "UTF-8");
          String commandConfess = "\"" + game.getPlayerB().getPlayerName() + "\" plays \"Confess\" in game #"
              + game.getGameId();
          String commandBeQuiet = "\"" + game.getPlayerB().getPlayerName() + "\" plays \"Be Quiet\" in game #"
              + game.getGameId();
          out.println("<li><a href=\"" + sn + "?command=" + URLEncoder.encode(commandConfess, "UTF-8") + "\">"
              + game.getPlayerB() + " plays Confess</a></li>");
          out.println("<li><a href=\"" + sn + "?command=" + URLEncoder.encode(commandBeQuiet, "UTF-8") + "\">"
              + game.getPlayerB() + " plays Be Quiet</a></li>");
        }
        if (game.bothHavePlayed()) {
          out.println("<li>" + game.getPlayerA() + " wins " + game.getScoreA() + " points</li>");
          out.println("<li>" + game.getPlayerB() + " wins " + game.getScoreB() + " points</li>");
        }
        out.println("</ul>");
      }
    }
    if (playerMap.get(serverName) != null) {
      out.println("<h2>Player Scores for " + serverName + "</h2>");
      out.println("<table>");
      out.println("  <tr>");
      out.println("    <th>Player</th>");
      out.println("    <th>Play Count</th>");
      out.println("    <th>Average Score</th>");
      out.println("  </tr>");
      List<Player> playerScoreList = new ArrayList<>(playerMap.get(serverName).values());
      Collections.sort(playerScoreList, new Comparator<Player>() {
        @Override
        public int compare(Player player1, Player player2) {
          if (player1.getAverageScore() == player2.getAverageScore()) {
            return 0;
          } else if (player1.getAverageScore() < player2.getAverageScore()) {
            return 1;
          }
          return -1;
        }
      });
      for (Player player : playerScoreList) {
        if (player.getServerName().equals(serverName)) {
          String link = serverNameEncoded + "?command=" + URLEncoder.encode("\"Sam\" wants to play", "UTF-8");
          out.println("  <tr>");
          out.println("    <td><a href=\"" + link + "\">" + player.getPlayerName() + "</a></td>");
          out.println("    <td>" + player.getPlayCount() + "</td>");
          out.println("    <td>" + player.getAverageScore() + "</td>");
          out.println("  </tr>");
        }
      }
      out.println("</table>");
    }

    out.println("<h2>Other Servers</h2>");
    out.println("<ul>");
    for (String otherServerName : playerMap.keySet()) {
      out.println(
          "  <li><a href=\"" + URLEncoder.encode(otherServerName, "UTF-8") + "\">" + otherServerName + "</a></li>");
    }
    out.println("</ul>");
    out.println("  </body>");
    out.println("</html>");

  }
}
