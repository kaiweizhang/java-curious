package com.funny.jmx.jvmInfo;

import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JvmInfo {
	public static void main(String[] args) throws Exception {
		getConnection();
	}

	private static void getConnection() throws Exception {

		// 用户名、密码
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("jmx.remote.credentials", new String[] { "monitorRole", "QED" });
		String jmxURL = "service:jmx:rmi:///jndi/rmi://172.17.6.31:40124/jmxrmi";

		JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
		JMXConnector connector = JMXConnectorFactory.connect(serviceURL, map);
		MBeanServerConnection mbsc = connector.getMBeanServerConnection();

		Set MBeanset = mbsc.queryMBeans(null, null);
		Iterator MBeansetIterator = MBeanset.iterator();
		while (MBeansetIterator.hasNext()) {
			ObjectInstance objectInstance = (ObjectInstance) MBeansetIterator.next();
			ObjectName objectName = objectInstance.getObjectName();
			MBeanInfo objectInfo = mbsc.getMBeanInfo(objectName);
			System.out.print("Object名称:" + objectName.getCanonicalName() + ".");
			System.out.print("方法名称:");
			for (int i = 0; i < objectInfo.getAttributes().length; i++) {
				System.out.print(objectInfo.getAttributes()[i].getName() + ",");
			}
			System.out.println();
		}

		ObjectName heapObjName = new ObjectName("java.lang:type=Memory");

		// 堆内存
		MemoryUsage heapMemoryUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(heapObjName, "HeapMemoryUsage"));
		long commitMemory = heapMemoryUsage.getCommitted();// 堆当前分配
		long usedMemory = heapMemoryUsage.getUsed();
		System.out.print("堆内存总量:" + heapMemoryUsage.getMax() / 1024 + "KB,当前分配量:" + commitMemory / 1024 + "KB,当前使用率:" + usedMemory / 1024 + "KB,");
		System.out.println("堆内存使用率:" + (int) usedMemory * 100 / commitMemory + "%");// 堆使用率

		// 栈内存
		MemoryUsage nonheapMemoryUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(heapObjName, "NonHeapMemoryUsage"));
		long noncommitMemory = nonheapMemoryUsage.getCommitted();
		long nonusedMemory = heapMemoryUsage.getUsed();

		System.out.println("栈内存使用率:" + (int) nonusedMemory * 100 / noncommitMemory + "%");

		// PermGen内存
		ObjectName permObjName = new ObjectName("java.lang:type=MemoryPool,name=Perm Gen");

		MemoryUsage permGenUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(permObjName, "Usage"));
		long committed = permGenUsage.getCommitted();// 持久堆大小
		long used = heapMemoryUsage.getUsed();//
		System.out.println("perm gen:" + (int) used * 100 / committed + "%");// 持久堆使用率

		ObjectName managerObjName = new ObjectName("resin:type=ThreadPool,*");
		Set<ObjectName> s = mbsc.queryNames(managerObjName, null);
		for (ObjectName obj : s) {

			ObjectName objname = new ObjectName(obj.getCanonicalName());
			System.out.print("objectName:" + objname);
			System.out.print(",最大会话数:" + mbsc.getAttribute(objname, "ThreadCount") + ",");
			System.out.print("会话数:" + mbsc.getAttribute(objname, "ThreadActiveCount") + ",");
			System.out.println("活动会话数:" + mbsc.getAttribute(objname, "ThreadExecutorMax"));

		}
	}

}
