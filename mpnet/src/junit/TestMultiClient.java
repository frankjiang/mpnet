/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * TestMultiClient.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package junit;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.junit.Test;

import com.frank.mpnet.Transaction;
import com.frank.mpnet.multi.SelectByDataAmount;
import com.frank.mpnet.multi.SessionMulti;
import com.frank.mpnet.multi.TransferPolicy;
import com.frank.mpnet.multi.TransferPolicySelect;

/**
 * The test case for {@link SessionMulti} as a server.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class TestMultiClient
{
	@Test
	public final void test() throws Exception
	{
		int size = 2;
		int count = 100000;
		TransferPolicySelect selector = new SelectByDataAmount(false);
		TransferPolicy policy = TransferPolicy.UDP;
		SocketAddress sa = new InetSocketAddress("127.0.0.1", 9000);
		SessionMulti s = new SessionMulti(selector, policy);
		s.setPort(9001);
		s.update(size);
		Transaction t = s.beginTransaction(sa);
		long begin = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
			t.send(new byte[size]);
		t.close();
		s.close();
		System.out.printf("[TASK] Client Time = %dms\r\n",
				System.currentTimeMillis() - begin);
	}
}
