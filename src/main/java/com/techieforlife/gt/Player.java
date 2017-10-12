package com.techieforlife.gt;

public class Player {
  private String playerName = "";
  private int cumulativeScore = 0;

  public int getCumulativeScore() {
    return cumulativeScore;
  }

  public void setCumulativeScore(int cumulativeScore) {
    this.cumulativeScore = cumulativeScore;
  }

  public Player(String playerName) {
    this.playerName = playerName;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Player) {
      Player otherPlayer = (Player) obj;
      return otherPlayer.getPlayerName().equals(this.getPlayerName());
    }
    return super.equals(obj);
  }

  @Override
  public String toString() {
    return playerName;
  }
  
  @Override
  public int hashCode() {
    return playerName.hashCode();
  }
}
