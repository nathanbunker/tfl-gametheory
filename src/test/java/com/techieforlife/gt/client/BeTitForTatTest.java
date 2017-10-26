package com.techieforlife.gt.client;

public class BeTitForTatTest extends ClientTest {
  protected void init() {
    client = new BeTitForTat(ME);
  }

  public void test() {
    init();
    expectNice();
    playNice();
    expectNice();
    playMean();
    expectMean();
    playNice();
    expectNice();
    playNice();
    expectNice();
    playMean();
    expectMean();
    playNice();
    expectNice();
    
    
    init();
    expectNice();
    playMean();
    expectMean();
    playNice();
    expectNice();
    playMean();
    expectMean();
    playNice();
    expectNice();
    playNice();
    expectNice();
    playMean();
    expectMean();
    playNice();
    expectNice();
  }
}
