/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * TestMultiServer.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package junit;

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
public class TestMultiServer
{
	@Test
	public final void test() throws Exception
	{
		int size = 8192;
		int count = 1000;
		TransferPolicySelect selector = new SelectByDataAmount(true);
		TransferPolicy policy = TransferPolicy.UDP;
		SessionMulti s = new SessionMulti(selector, policy);
		s.update(size);
		long begin = System.currentTimeMillis();
		Transaction t = s.beginTransaction();
		for (int i = 0; i < count; i++)
			t.receive(t.allocateReceiveBuffer());
		t.close();
		s.close();
		System.out.printf("[TASK] Server Time = %dms\r\n", System.currentTimeMillis()
				- begin);
	}
}
