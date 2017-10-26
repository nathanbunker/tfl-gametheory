package com.techieforlife.gt.client;

public class BeFairTest extends ClientTest {
  protected void init() {
    client = new BeFair(ME);
  }

  public void test() {
    init();
    expectMean();
    playNice();
    expectNice();
    playNice();
    expectNice();
    playNice();
    expectNice();
    playMean();
    expectNice();
    playMean();
    expectNice();
    playMean();
    expectMean();

    init();
    expectMean();
    playMean();
    expectMean();
    playNice();
    expectMean();
    playNice();
    expectNice();
    
    init();
    expectMean();
  }
}
