package com.techieforlife.gt.client;

import junit.framework.TestCase;

public class BeBoldTest extends TestCase {
  public void test()
  {
    BeBold t = new BeBold("Sam");
    assertTrue(t.confess("Hello"));
    assertTrue(t.confess("HELLO"));
    assertFalse(t.confess("hello"));
  }
}
