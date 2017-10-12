package com.techieforlife.gt.client;

import java.util.Random;

public class BeRandom extends Client {

  Random random = new Random();

  public BeRandom(String name) {
    super(name);
  }

  @Override
  public boolean confess(String otherPlayer) {
    return random.nextBoolean();
  }

  @Override
  public void reportResult(String otherPlayer, String move) {
  }


}
