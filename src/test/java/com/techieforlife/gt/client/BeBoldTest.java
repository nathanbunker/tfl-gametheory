package com.techieforlife.gt.client;

public class BeBoldTest extends ClientTest  {
  
  protected void init() {
    client = new BeBold(ME);
  }
  public void test()
  {
    init();
    playerName = "LARRY";
    expectNice();

    init();
    playerName = "Larry";
    expectNice();

    init();
    playerName = "larry";
    expectMean();

    init();
    playerName = "larrY";
    expectNice();

    init();
    playerName = "larry";
    expectMean();
    for (int i = 0; i < 100; i++) {
      playMean();
      expectMean();
    }

    init();
    playerName = "Larry";
    expectNice();
    for (int i = 0; i < 100; i++) {
      playMean();
      expectNice();
    }
  }
}
