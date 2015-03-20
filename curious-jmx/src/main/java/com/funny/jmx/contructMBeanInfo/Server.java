package com.funny.jmx.contructMBeanInfo;

public class Server {
	private long startTime;

	public Server() {
	}

	public int start() {
		startTime = System.currentTimeMillis();
		return 0;
	}

	public long getUpTime() {
		// return 109L;
		return System.currentTimeMillis();
	}
}
