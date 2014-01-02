/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * SessionMHttp.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet.multi;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.frank.mpnet.Session;
import com.frank.mpnet.SessionException;
import com.frank.mpnet.SessionTCPClient;
import com.frank.mpnet.SessionTCPServer;
import com.frank.mpnet.SessionUDP;
import com.frank.mpnet.Transaction;
import com.frank.mpnet.TransactionException;

/**
 * The MHTTP Session.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class SessionMulti extends Session implements TransferPolicySelectable
{
	/**
	 * The port of current socket.
	 */
	protected int					port	= 9000;
	/**
	 * The policy of the current session.
	 */
	protected TransferPolicy		policy;
	/**
	 * The transfer policy selector.
	 */
	protected TransferPolicySelect	selector;
	/**
	 * The underlying session of UDP.
	 */
	protected SessionUDP			sessionUDP;
	/**
	 * The underlying session of TCP server.
	 */
	protected SessionTCPServer		sessionTCPServer;
	/**
	 * The underlying session of TCP client.
	 */
	protected SessionTCPClient		sessionTCPClient;

	/**
	 * Construct an instance of <tt>SessionMulti</tt>.
	 * 
	 * @param selector
	 *            the policy selector
	 * @param policy
	 *            the default transfer policy
	 */
	public SessionMulti(TransferPolicySelect selector, TransferPolicy policy)
	{
		this.selector = selector;
		policy = null;
	}

	/**
	 * Returns a new underlying session according to the transfer policy.
	 * 
	 * @return the new session.
	 */
	protected Session newSession()
	{
		if (policy == null)
			policy = TransferPolicy.UDP;
		switch (policy)
		{
			default:
			case UDP:
				if (sessionUDP == null)
					sessionUDP = new SessionUDP(new InetSocketAddress(port));
				return sessionUDP;
			case TCP_Server:
				if (sessionTCPServer == null)
					sessionTCPServer = new SessionTCPServer(port);
				return sessionTCPServer;
			case TCP_Client:
				if (sessionTCPClient == null)
					sessionTCPClient = new SessionTCPClient(
							new InetSocketAddress(port));
				return sessionTCPClient;
		}
	}

	/**
	 * Returns the current underlying session.
	 * <p>
	 * If the session is not created yet, create a new underlying session.
	 * </p>
	 * 
	 * @return the current underlying session.
	 */
	protected Session currentSession()
	{
		if (policy == null)
			policy = TransferPolicy.UDP;
		switch (policy)
		{
			default:
			case UDP:
				if (sessionUDP == null)
					sessionUDP = (SessionUDP) newSession();
				return sessionUDP;
			case TCP_Server:
				if (sessionTCPServer == null)
					sessionTCPServer = (SessionTCPServer) newSession();
				return sessionTCPServer;
			case TCP_Client:
				if (sessionTCPClient == null)
					sessionTCPClient = (SessionTCPClient) newSession();
				return sessionTCPClient;
		}
	}

	/**
	 * @see com.frank.mpnet.Session#beginTransaction()
	 */
	@Override
	public Transaction beginTransaction() throws TransactionException
	{
		return currentSession().beginTransaction();
	}

	/**
	 * @see com.frank.mpnet.Session#beginTransaction(java.net.SocketAddress)
	 */
	@Override
	public Transaction beginTransaction(SocketAddress sa)
			throws TransactionException
	{
		return currentSession().beginTransaction(sa);
	}

	/**
	 * @see com.frank.mpnet.Session#close()
	 */
	@Override
	public void close() throws SessionException
	{
		if (sessionUDP != null)
			sessionUDP.close();
		if (sessionTCPClient != null)
			sessionTCPClient.close();
		if (sessionTCPServer != null)
			sessionTCPServer.close();
	}

	/**
	 * @see com.frank.mpnet.Session#getSocket()
	 */
	@Override
	public Object getSocket()
	{
		return currentSession().getSocket();
	}

	/**
	 * @see com.frank.mpnet.Session#getLocalAddress()
	 */
	@Override
	public SocketAddress getLocalAddress()
	{
		return currentSession().getLocalAddress();
	}

	/**
	 * @see com.frank.mpnet.multi.TransferPolicySelectable#update(java.lang.Object[])
	 */
	@Override
	public void update(Object... args)
	{
		policy = selector.select(args);
	}

	/**
	 * Set the socket port.
	 * 
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * Returns the current port.
	 * 
	 * @return the current port.
	 */
	public int getPort()
	{
		return port;
	}
}
