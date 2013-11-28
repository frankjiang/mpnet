/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * SocketInfo.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * A Java bean which stores the income net package info.
 * <p>
 * In this structure, the source information of the data packet is stored.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class SocketInfo
{
	/**
	 * The type of transmission protocol used in the socket.
	 */
	protected SocketType	type;
	/**
	 * The source address of the socket.
	 */
	protected SocketAddress	source;

	public SocketInfo(SocketType type, SocketAddress source)
	{
		this.type = type;
		this.source = source;
	}

	/**
	 * Set the socket type.
	 * 
	 * @param type
	 *            the socket type
	 */
	protected synchronized void setType(SocketType type)
	{
		this.type = type;
	}

	/**
	 * Returns the socket type.
	 * 
	 * @return the socket type
	 */
	public synchronized SocketType getType()
	{
		return type;
	}

	/**
	 * Returns the address of the source socket.
	 * 
	 * @return the address
	 */
	public synchronized SocketAddress getAddress()
	{
		return source;
	}

	/**
	 * Set the source address of the socket.
	 * 
	 * @param source
	 *            the source address of the socket
	 */
	protected synchronized void setAddress(SocketAddress source)
	{
		if (source == null || !(source instanceof InetSocketAddress))
			throw new NullPointerException(
					"the specified socket address is null");
		this.source = source;
	}
}
