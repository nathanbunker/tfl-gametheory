package com.techieforlife.gt;

public class Game {
  private int gameId = 0;
  private Player playerA = null;
  private Player playerB = null;
  private String playA = null;
  private String playB = null;
  private int scoreA = 0;
  private int scoreB = 0;

  public boolean bothHavePlayed() {
    return playA != null && playB != null;
  }

  public String getPlayA() {
    return playA;
  }

  public void setPlay(String play, Player player) {
    if (player.equals(playerA)) {
      playA = play;
    } else if (player.equals(playerB)) {
      playB = play;
    } else {
      throw new RuntimeException("Player " + player + " is not playing this game!");
    }
    if (playA != null && playB != null) {
      // score it!
      // Confess
      // Be Quiet
      if (playA.equalsIgnoreCase("Confess")) {
        if (playB.equalsIgnoreCase("Confess")) {
          scoreA = -2;
          scoreB = -2;
        } else {
          scoreA = 0;
          scoreB = -5;
        }
      } else {
        if (playB.equalsIgnoreCase("Confess")) {
          scoreA = -5;
          scoreB = 0;
        } else {
          scoreA = -1;
          scoreB = -1;
        }
      }
    }
  }

  public String getOtherPlay(Player player) {
    if (playerA.equals(player)) {
      return playB;
    }
    return playA;
  }

  public String getPlay(Player player) {
    if (playerA.equals(player)) {
      return playA;
    }
    return playB;
  }
  public int getOtherScore(Player player) {
    if (playerA.equals(player)) {
      return scoreB;
    }
    return scoreA;
  }

  public int getScore(Player player) {
    if (playerA.equals(player)) {
      return scoreA;
    }
    return scoreB;
  }

  public void setPlayA(String playA) {
    this.playA = playA;
  }

  public String getPlayB() {
    return playB;
  }

  public void setPlayB(String playB) {
    this.playB = playB;
  }

  public int getScoreA() {
    return scoreA;
  }

  public void setScoreA(int scoreA) {
    this.scoreA = scoreA;
  }

  public int getScoreB() {
    return scoreB;
  }

  public void setScoreB(int scoreB) {
    this.scoreB = scoreB;
  }

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  public Player getPlayerA() {
    return playerA;
  }

  public void setPlayerA(Player playerA) {
    this.playerA = playerA;
  }

  public Player getOtherPlayer(Player player) {
    if (playerA.equals(player)) {
      return playerB;
    }
    return playerA;
  }

  public Player getPlayer(Player player) {
    if (playerA.equals(player)) {
      return playerA;
    }
    return playerB;
  }

  public Player getPlayerB() {
    return playerB;
  }

  public void setPlayerB(Player playerB) {
    this.playerB = playerB;
  }
}
