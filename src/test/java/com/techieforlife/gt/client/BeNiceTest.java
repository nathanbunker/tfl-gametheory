package com.techieforlife.gt.client;

public class BeNiceTest extends ClientTest {
  protected void init() {
    client = new BeNice(ME);
  }

  public void test() {
    init();
    expectNice();
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
    expectNice();

    init();
    expectNice();
    playMean();
    expectNice();
    playNice();
    expectNice();
    playNice();
    expectNice();
    
    init();
    expectNice();
  }
}
