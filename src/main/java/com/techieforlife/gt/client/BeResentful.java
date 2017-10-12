package com.techieforlife.gt.client;

import java.util.HashSet;
import java.util.Set;

public class BeResentful extends Client {

  private Set<String> namesIHate = new HashSet<>();
  public BeResentful(String name) {
    super(name);
  }

  @Override
  public boolean confess(String otherPlayer) {
    return namesIHate.contains(otherPlayer);
  }

  @Override
  public void reportResult(String otherPlayer, String move) {
    if (move.equals("Confess"))
    {
      namesIHate.add(otherPlayer);
    }
  }

}
