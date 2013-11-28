/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * SessionFactory.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.net.SocketAddress;

/**
 * FIXME The session factory is a factory mode class for creating
 * {@linkplain Session} instances.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class SessionFactory
{
	/**
	 * Build a new session factory.
	 * 
	 * @return the new session factory.
	 */
	public static SessionFactory build()
	{
		return new SessionFactory();
	}

	/**
	 * Construct an instance of <tt>SessionFactory</tt>.
	 */
	private SessionFactory()
	{
		// do nothing for current situation
	}

	/**
	 * Create a specified net transmission session.
	 * 
	 * @param sa
	 *            the socket address to bind
	 * @param type
	 *            the type of the transmission
	 * @return the transmission session
	 */
	public Session createSession(SocketAddress sa, SocketType type)
	{
		switch (type)
		{
			default:
			case UDP:
				return new SessionUDP(sa);
			case TCP_SERVER:
				return new SessionTCPServer(sa);
			case TCP_CLIENT:
				return new SessionTCPClient(sa);
		}
	}
}
