package com.techieforlife.gt.client;

public class BeNasty extends Client {

  
  public BeNasty(String name) {
    super(name);
  }

  @Override
  public boolean confess(String otherPlayer) {
    return true;
  }

  @Override
  public void reportResult(String otherPlayer, String move) {
  }


}
