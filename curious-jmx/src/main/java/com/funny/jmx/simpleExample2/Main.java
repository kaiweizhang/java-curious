package com.funny.jmx.simpleExample2;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

public class Main {
	private static ObjectName objectName1;
	private static ObjectName objectName2;
	private static MBeanServer mBeanServer;

	public static void main(String[] args) throws Exception {
		init();
		manage();
	}

	private static void init() throws Exception {
		ServerImpl serverImpl = new ServerImpl();
		ServerMonitor serverMonitor = new ServerMonitor(serverImpl);
		ServerMonitor2 serverMonitor2 = new ServerMonitor2(serverImpl);
		mBeanServer = MBeanServerFactory.createMBeanServer();
		objectName1 = new ObjectName("objectName:id=ServerMonitor1");
		mBeanServer.registerMBean(serverMonitor, objectName1);
		objectName2 = new ObjectName("objectName:id=ServerMonitor2");
		mBeanServer.registerMBean(serverMonitor2, objectName2);
	}

	private static void manage() throws Exception {
		Long upTime = (Long) mBeanServer.getAttribute(objectName1, "UpTime");
		System.out.println(upTime);
		upTime = (Long) mBeanServer.getAttribute(objectName2, "UpTime");
		System.out.println(upTime);
	}
}