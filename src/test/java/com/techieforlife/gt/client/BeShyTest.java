package com.techieforlife.gt.client;

public class BeShyTest extends ClientTest {
  protected void init() {
    client = new BeShy(ME);
  }

  public void test() {
    init();
    playerName = "LARRY";
    expectMean();

    init();
    playerName = "Larry";
    expectMean();

    init();
    playerName = "larry";
    expectNice();

    init();
    playerName = "larrY";
    expectMean();

    init();
    playerName = "larry";
    expectNice();
    for (int i = 0; i < 100; i++) {
      playMean();
      expectNice();
    }

    init();
    playerName = "Larry";
    expectMean();
    for (int i = 0; i < 100; i++) {
      playMean();
      expectMean();
    }

  }
}
