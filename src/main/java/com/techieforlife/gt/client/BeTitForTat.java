package com.techieforlife.gt.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BeTitForTat extends Client {

  private Map<String, String> othersResponsesMap = new HashMap<>();

  public BeTitForTat(String name) {
    super(name);
  }

  @Override
  public boolean confess(String otherPlayer) {
    String response = othersResponsesMap.get(otherPlayer);
    if (response == null) {
      response = "Be Quiet";
    }
    return response.equals("Confess");
  }

  @Override
  public void reportResult(String otherPlayer, String move) {
    othersResponsesMap.put(otherPlayer, move);
  }

}
