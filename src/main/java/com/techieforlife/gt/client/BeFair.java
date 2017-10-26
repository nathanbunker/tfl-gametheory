package com.techieforlife.gt.client;

import java.util.HashMap;
import java.util.Map;

public class BeFair extends Client {

  private Map<String, Integer> othersResponsesMap = new HashMap<>();
  
  public BeFair(String name) {
    super(name);
  }

  @Override
  public boolean confess(String otherPlayer) {
    Integer score = othersResponsesMap.get(otherPlayer);
    if (score == null)
    {
      return true;
    }
    return score <= 0;
  }

  @Override
  public void reportResult(String otherPlayer, String move) {
    Integer newScore = move.equals("Confess") ? -1 : 1;
    Integer score = othersResponsesMap.get(otherPlayer);
    if (score == null)
    {
      othersResponsesMap.put(otherPlayer, newScore);
    }
    else
    {
      othersResponsesMap.put(otherPlayer, newScore + score);
    }
  }

}
