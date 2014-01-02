/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * CompareMSwithUDP.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package junit;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.junit.Test;

import com.frank.mpnet.Session;
import com.frank.mpnet.SessionFactory;
import com.frank.mpnet.SocketType;
import com.frank.mpnet.Transaction;

/**
 * The UDP server test case for comparing the multi-socket.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class CompareUDPServer
{
	@Test
	public final void test() throws Exception
	{
		int count = 1000;
		SocketAddress sa = new InetSocketAddress("127.0.0.1", 9000);
		SocketType type = SocketType.UDP;
		Session s = SessionFactory.build().createSession(
				new InetSocketAddress(9000), type);
		Transaction t = s.beginTransaction(sa);
		long begin = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
			t.receive(t.allocateReceiveBuffer());
		t.close();
		s.close();
		System.out.printf("[TASK] UDP Server Time = %dms\r\n",
				System.currentTimeMillis() - begin);
	}
}
