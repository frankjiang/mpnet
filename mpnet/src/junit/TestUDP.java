/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * TestUDPOfTransaction.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package junit;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.frank.mpnet.Session;
import com.frank.mpnet.SessionFactory;
import com.frank.mpnet.SocketType;
import com.frank.mpnet.Transaction;
import com.frank.mpnet.TransactionUDP;

/**
 * Test the UDP transaction.
 * <p>
 * In this test case, the transaction will be performed between the sender point
 * and the receiver point.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class TestUDP
{
	/**
	 * The maximum value of transmission unit.
	 */
	public static final int					MAX	= 20;
	/**
	 * The Internet address of the message sender.
	 */
	public static final InetSocketAddress	SND	= new InetSocketAddress(
														"localhost", 8091);
	/**
	 * The Internet address of the message receiver.
	 */
	public static final InetSocketAddress	RCV	= new InetSocketAddress(
														"localhost", 8092);

	/**
	 * Test method for {@link com.frank.mpnet.SessionUDP#beginTransaction()}.
	 */
	@Test
	public final void testBeginTransaction()
	{
		ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(2);
		stpe.execute(createRCVCloseWithFlag(RCV, -1, -1, System.err));
		stpe.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				PrintStream ps = System.out;
				Session session = SessionFactory.build().createSession(SND,
						SocketType.UDP);
				Transaction transaction = session.beginTransaction();
				ps.println("[SND] Sending started.");
				String s = "0";
				int sum = 0;
				try
				{
					for (int i = 0; i < MAX; i++)
					{
						s += s;
						ps.printf("[SND]\t%d\r\n", s.length());
						ps.flush();
						transaction.send(RCV,
								String.format("%d:%s", s.length(), s));
						sum += s.length();
						//	Thread.sleep(500);
					}
					transaction.send(RCV, "-1:end");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				session.close();
				ps.printf("[SND] Session closed. Total = %d\r\n", sum);
			}
		}, 200, TimeUnit.MILLISECONDS);
		stpe.shutdown();
		try
		{
			stpe.awaitTermination(1, TimeUnit.DAYS);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create receive thread.
	 * 
	 * @param RCV
	 *            the receiving socket address
	 * @param ID
	 *            the ID of the runnable, -1 for not displaying
	 * @param ps
	 *            the print stream to display
	 * @return the runnable
	 */
	Runnable createRCVCloseWithFlag(final SocketAddress RCV, final int FLAG,
			final int ID, final PrintStream ps)
	{
		return new Runnable()
		{
			/**
			 * Run the message receiving transaction.
			 * 
			 * @see java.lang.Thread#run()
			 */
			@Override
			public void run()
			{
				Session session = SessionFactory.build().createSession(RCV,
						SocketType.UDP);
				Transaction transaction = session.beginTransaction();
				ps.println("[RCV] Receiving started.");
				int sum = 0;
				while (transaction.hasNext())
				{
					try
					{
						ByteBuffer buffer = ByteBuffer
								.allocate(TransactionUDP.MAX_UDP_PACKET);
						transaction.receive(buffer);
						String s = new String(buffer.array(),
								buffer.arrayOffset(), buffer.position());
						int index = s.indexOf(':');
						if (index == -1)
							sum += s.length();
						else
						{
							if (Integer.valueOf(s.substring(0, index)) == FLAG)
							{
								s = s.substring(index + 1);
								if (ID == -1)
									ps.printf("[RCV]\t%s\r\n", s);
								else
									ps.printf("[RCV%d]\t%s\r\n", ID, s);
								session.close();
							}
							else
							{
								s = s.substring(index + 1);
								sum += s.length();
								if (ID == -1)
									ps.printf("[RCV]\t%d\r\n", s.length());
								else
									ps.printf("[RCV%d]\t%d\r\n", ID, s.length());
							}
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				session.close();
				if (ID == -1)
					ps.printf("[RCV] Session closed. Total = %d\r\n", sum);
				else
					ps.printf("[RCV%d] Session closed. Total = %d\r\n", ID, sum);
			}
		};
	}
}
