package com.techieforlife.gt.client;

public class BeRandomTest extends ClientTest {
  protected void init() {
    client = new BeRandom(ME);
  }

  public void test() {
    init();
    {
      int confessCount = 0;
      int totalCount = 100000;
      for (int i = 0; i < totalCount; i++) {
        boolean confess = client.confess(playerName);
        playNice();
        if (confess) {
          confessCount++;
        }
      }
      double d = confessCount / (double) totalCount;
      assertTrue(d > 0.48);
      assertTrue(d < 0.52);
    }
    {
      int confessCount = 0;
      int totalCount = 100000;
      for (int i = 0; i < totalCount; i++) {
        boolean confess = client.confess(playerName);
        playMean();
        if (confess) {
          confessCount++;
        }
      }
      double d = confessCount / (double) totalCount;
      assertTrue(d > 0.48);
      assertTrue(d < 0.52);
    }
  }
}
