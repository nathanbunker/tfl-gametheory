package com.techieforlife.gt.client;

public class BeResentfulTest extends ClientTest {
  protected void init() {
    client = new BeResentful(ME);
  }

  public void test() {
    init();
    expectNice();
    playNice();
    expectNice();
    playMean();
    expectMean();
    playNice();
    expectMean();
    playNice();
    expectMean();
    playMean();
    expectMean();
    playNice();
    expectMean();
    
    init();
    expectNice();
    playNice();
    expectNice();
    playNice();
    expectNice();
    playNice();
    expectNice();
    playNice();
    expectNice();
    playNice();
    expectNice();
    playNice();
    expectNice();
  }
}
