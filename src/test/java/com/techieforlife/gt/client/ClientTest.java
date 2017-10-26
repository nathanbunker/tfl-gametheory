package com.techieforlife.gt.client;

import junit.framework.TestCase;

public abstract class ClientTest extends TestCase {
  protected static final String ME = "Sam";
  protected String playerName = "George";

  protected Client client;

  protected abstract void init();

  protected void expectNice() {
    assertFalse(client.confess(playerName));
  }

  protected void expectMean() {
    assertTrue(client.confess(playerName));
  }

  protected void playNice() {
    client.reportResult(playerName, "Be Quiet");
  }

  protected void playMean() {
    client.reportResult(playerName, "Confess");
  }
}
