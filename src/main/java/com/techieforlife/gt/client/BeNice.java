package com.techieforlife.gt.client;

public class BeNice extends Client {

  
  public BeNice(String name) {
    super(name);
  }

  @Override
  public boolean confess(String otherPlayer) {
    return false;
  }
  
  @Override
  public void reportResult(String otherPlayer, String move) {
  }



}
