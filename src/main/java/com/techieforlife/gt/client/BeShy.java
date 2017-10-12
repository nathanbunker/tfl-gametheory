package com.techieforlife.gt.client;

public class BeShy extends Client {

  public BeShy(String name) {
    super(name);
  }

  @Override
  public boolean confess(String otherPlayer) {
    return !otherPlayer.toLowerCase().equals(otherPlayer);
  }

  @Override
  public void reportResult(String otherPlayer, String move) {
  }

}
