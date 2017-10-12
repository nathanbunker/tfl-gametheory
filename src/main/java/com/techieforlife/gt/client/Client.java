package com.techieforlife.gt.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Client extends Thread {

  private Random random = new Random();
  private static final String URL = "http://imm.pagekite.me/gt/server";

  protected String name;

  public Client(String name) {
    this.name = name;
  }

  @Override
  public void run() {
    try {
      while (true) {
        String response1 = sendMessage("\"" + name + "\" wants to play");
        if (response1.indexOf("can play") > 0 && response1.indexOf('#') > 0) {
          String otherPlayer = response1.substring(response1.indexOf("can play"));
          {
            int pos1 = otherPlayer.indexOf("\"");
            if (pos1 > 0) {
              pos1++;
              int pos2 = otherPlayer.indexOf("\"", pos1);
              if (pos2 > 0) {
                otherPlayer = otherPlayer.substring(pos1, pos2);
              }
            }
          }

          int gameNumber = 0;
          try {
            gameNumber = Integer.parseInt(response1.substring(response1.lastIndexOf('#') + 1).trim());
          } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
          }
          if (gameNumber > 0) {
            String decision = confess(otherPlayer) ? "Confess" : "Be Quiet";
            String response2 = sendMessage("\"" + name + "\" plays \"" + decision + "\" in game #" + gameNumber);
            System.out.print(response2);

            if (response2.indexOf("plays") > 0) {
              String move = response1.substring(response1.indexOf("can play"));
              int pos1 = move.indexOf("\"");
              if (pos1 > 0) {
                pos1++;
                int pos2 = move.indexOf("\"", pos1);
                if (pos2 > 0) {
                  move = move.substring(pos1, pos2);
                  reportResult(otherPlayer, move);
                }
              }
            }
          }
          synchronized (this) {
            this.wait(1000 * random.nextInt(15) + 5);
          }
        }
      }
    } catch (InterruptedException e) {
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public abstract boolean confess(String otherPlayer);

  public abstract void reportResult(String otherPlayer, String move);

  public static void main(String[] args) throws IOException {
    List<Client> clientList = new ArrayList<>();
    clientList.add(new BeBold("Bold Larry"));
    clientList.add(new BeNasty("Nasty Veronica"));
    clientList.add(new BeNice("Nice Norman"));
    clientList.add(new BeRandom("Random Ralph"));
    for (String s : new String[] { "Jim", "Justin", "Jerry", "Jerk" }) {
      clientList.add(new BeResentful("Resentful " + s));
    }
    for (String s : new String[] { "Felix", "Ferbie", "Frank", "Foible" }) {
      clientList.add(new BeFair("Fair " + s));
    }
    for (String s : new String[] { "Terry", "Thomas", "Tracy" }) {
      clientList.add(new BeFair("TitForTat " + s));
    }
    clientList.add(new BeShy("Shy Lucy"));

    for (Client client : clientList) {
      client.start();
    }
  }

  private static String sendMessage(String request) throws IOException {
    HttpURLConnection urlConn;
    InputStreamReader input = null;
    URL url = new URL(URL);

    urlConn = (HttpURLConnection) url.openConnection();
    urlConn.setConnectTimeout(60 * 1000);

    urlConn.setRequestMethod("POST");

    urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    urlConn.setUseCaches(false);
    String content = "command=" + URLEncoder.encode(request, "UTF-8");
    DataOutputStream printout;
    printout = new DataOutputStream(urlConn.getOutputStream());
    printout.writeBytes(content);
    printout.flush();
    printout.close();
    input = new InputStreamReader(urlConn.getInputStream());
    StringBuilder response = new StringBuilder();
    BufferedReader in = new BufferedReader(input);
    String line;
    while ((line = in.readLine()) != null) {
      response.append(line);
      response.append('\r');
    }
    input.close();
    return response.toString();
  }
}
