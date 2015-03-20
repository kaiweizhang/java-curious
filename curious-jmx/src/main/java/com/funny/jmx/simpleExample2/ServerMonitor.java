package com.funny.jmx.simpleExample2;

public class ServerMonitor implements ServerMonitorMBean {

	private final ServerImpl target;

	public ServerMonitor(ServerImpl target) {
		this.target = target;
	}

	public long getUpTime() {
		return 5L;
		// return System.currentTimeMillis() - target.startTime;
	}
}