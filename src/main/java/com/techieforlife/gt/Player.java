package com.techieforlife.gt;

public class Player {
  private String serverName = "";
  private String playerName = "";
  private int cumulativeScore = 0;
  private int playCount = 0;
  private double averageScore = 0.0;

  public double getAverageScore() {
    return averageScore;
  }

  public void incPlayCount() {
    playCount++;
    averageScore = cumulativeScore / (double) playCount;
  }

  public void addScore(int score) {
    cumulativeScore += score;
  }

  public int getPlayCount() {
    return playCount;
  }

  public void setPlayCount(int playCount) {
    this.playCount = playCount;
  }

  public String getServerName() {
    return serverName;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public int getCumulativeScore() {
    return cumulativeScore;
  }

  public void setCumulativeScore(int cumulativeScore) {
    this.cumulativeScore = cumulativeScore;
  }

  public Player(String playerName, String serverName) {
    this.playerName = playerName;
    this.serverName = serverName;
  }

  public String getPlayerName() {
    return playerName;
  }

  public String getPlayerWithServerName() {
    return playerName + " from " + serverName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Player) {
      Player otherPlayer = (Player) obj;
      return otherPlayer.getPlayerWithServerName().equals(this.getPlayerWithServerName());
    }
    return super.equals(obj);
  }

  @Override
  public String toString() {
    return getPlayerWithServerName();
  }

  @Override
  public int hashCode() {
    return playerName.hashCode();
  }
}
