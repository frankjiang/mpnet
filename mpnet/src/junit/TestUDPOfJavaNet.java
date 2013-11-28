/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * IOTest.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package junit;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Java net package UDP transmission.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class TestUDPOfJavaNet
{
	public static final int	MAX	= 20;

	public static class Input extends Thread
	{
		protected DatagramSocket	ds;

		public Input()
		{
			try
			{
				ds = new DatagramSocket(
						new InetSocketAddress("localhost", 8091));
			}
			catch (SocketException e)
			{
				e.printStackTrace();
			}
		}

		public void run()
		{
			int buffSize = 1000;
			byte[] buf = new byte[buffSize];
			try
			{
				for (int i = 0; i < MAX; i++)
				{
					DatagramPacket p = new DatagramPacket(buf, buf.length);
					ds.receive(p);
					// System.out.println();
					String s = new String(p.getData(), p.getOffset(),
							p.getLength());
					int index = s.indexOf(':');
					if (index != -1)
						System.out.println(s.substring(0, index));
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static class Output extends Thread
	{
		protected DatagramSocket	ds;

		public Output()
		{
			try
			{
				ds = new DatagramSocket(
						new InetSocketAddress("localhost", 8092));
			}
			catch (SocketException e)
			{
				e.printStackTrace();
			}
		}

		public void run()
		{
			String s = "0";
			//			SimpleDateFormat sdf = new SimpleDateFormat(
			//					"yyyy-MM-dd hh:mm:ss SSS");
			SocketAddress address = new InetSocketAddress("localhost", 8091);
			try
			{
				for (int i = 0; i < MAX; i++)
				{
					s += s;
					String str = String.format("%d:%s", s.length(), s);
					byte[] buf = str.getBytes();
					DatagramPacket p = new DatagramPacket(buf, buf.length,
							address);
					ds.send(p);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				try
				{
					ds.send(new DatagramPacket("-1:error".getBytes(), 8, address));
				}
				catch (IOException e1)
				{
				}
			}
			ds.close();
		}
	}

	/**
	 * Test IO
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		Input in = new Input();
		Output out = new Output();
		in.start();
		out.start();
		System.out
				.println("WARNING: Last case will throw an exception for the packet size.");
	}
}
