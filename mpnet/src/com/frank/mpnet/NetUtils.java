/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * NetUtils.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * The net utilities.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class NetUtils
{
	/**
	 * Compares the two {@linkplain SocketAddress} whether they are equal.
	 * 
	 * @param a
	 *            the {@linkplain SocketAddress} a to compare
	 * @param b
	 *            the {@linkplain SocketAddress} b to compare
	 * @return <code>true</code> if <code>a</code> and <code>b</code> are equal,
	 *         otherwise, <code>false</code>
	 */
	public static boolean socketAddressEquals(SocketAddress a, SocketAddress b)
	{
		if ((a == null ^ b == null) || a == null)
			return false;
		if ((a instanceof InetSocketAddress)
				&& (b instanceof InetSocketAddress))
		{
			InetSocketAddress ia = (InetSocketAddress) a;
			InetSocketAddress ib = (InetSocketAddress) b;
			if (ia.getPort() == ib.getPort())
				return ia.getAddress().equals(ib.getAddress());
			else
				return false;
		}
		else
			return a.equals(b);
	}
}
