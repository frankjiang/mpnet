/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * SessionTCPClient.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * The net transmission session using TCP.
 * <p>
 * In this session the session is only the client point of the session. It can
 * drive one TCP link to a remote server socket.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class SessionTCPClient extends Session
{
	/**
	 * The client socket.
	 */
	protected Socket	client;

	/**
	 * Construct an instance of <tt>SessionTCPClient</tt> with unbound client
	 * socket.
	 */
	public SessionTCPClient()
	{
		client = new Socket();
	}

	/**
	 * Construct an instance of <tt>SessionTCPClient</tt> with specified
	 * underlying client socket.
	 * 
	 * @param client
	 *            the specified client socket
	 */
	SessionTCPClient(Socket client)
	{
		this.client = client;
	}

	/**
	 * Construct an instance of <tt>SessionTCPClient</tt> with a client socket
	 * bound with a specified socket address.
	 * 
	 * @param bindAddr
	 *            the socket address to connect
	 * @throws SessionException
	 *             if any exception occurs during creating an unbound server
	 *             socket session.
	 */
	public SessionTCPClient(SocketAddress bindAddr) throws SessionException
	{
		this();
		try
		{
			client.bind(bindAddr);
		}
		catch (IOException e)
		{
			throw new SessionException(String.format(
					"Unable to bind socket %s", bindAddr,
					e.getLocalizedMessage()), e);
		}
		//		if (remoteAddr instanceof InetSocketAddress)
		//		{
		//			InetSocketAddress isa = (InetSocketAddress) remoteAddr;
		//			try
		//			{
		//				client = new Socket(isa.getAddress(), isa.getPort());
		//			}
		//			catch (IOException e)
		//			{
		//				throw new SessionException(String.format(
		//						"Unable to connect the server socket %s: %s", remoteAddr,
		//						e.getLocalizedMessage()), e);
		//			}
		//		}
		//		else
		//			throw new SessionException("Unsupported socket type: "
		//					+ remoteAddr.toString());
	}

	/**
	 * Construct an instance of <tt>SessionTCPClient</tt> with bound the local
	 * socket address and connected the remote socket address.
	 * 
	 * @param localAddr
	 *            the local socket address
	 * @param remoteAddr
	 *            the remote socket address
	 * @throws SessionException
	 *             if any exception occurs during creating an unbound server
	 *             socket session.
	 */
	public SessionTCPClient(SocketAddress localAddr, SocketAddress remoteAddr)
			throws SessionException
	{
		client = new Socket();
		bind(localAddr);
		connect(remoteAddr);
	}

	/**
	 * Binds the <code>ServerSocket</code> to a specific address (IP address and
	 * port number).
	 * <p>
	 * If the address is <code>null</code>, then the system will pick up an
	 * ephemeral port and a valid local address to bind the socket.
	 * <p>
	 * 
	 * @param endpoint
	 *            The IP address & port number to bind to.
	 * @throws SessionException
	 *             if any exception occurs during binding.
	 * @throws IOException
	 *             if the bind operation fails, or if the socket is already
	 *             bound.
	 * @throws SecurityException
	 *             if a <code>SecurityManager</code> is present and its
	 *             <code>checkListen</code> method doesn't allow the operation.
	 * @throws IllegalArgumentException
	 *             if endpoint is a SocketAddress subclass not supported by this
	 *             socket
	 */
	public void bind(SocketAddress endpoint) throws SessionException
	{
		try
		{
			client.bind(endpoint);
		}
		catch (Exception e)
		{
			throw new SessionException("Unable to bind the client socket: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Connects this socket to the server.
	 * 
	 * @param endpoint
	 *            the <code>SocketAddress</code>
	 * @throws SessionException
	 *             if any exception occurs during connecting the server socket
	 * @throws IOException
	 *             if an error occurs during the connection
	 * @throws java.nio.channels.IllegalBlockingModeException
	 *             if this socket has an associated channel, and the channel is
	 *             in non-blocking mode
	 * @throws IllegalArgumentException
	 *             if endpoint is null or is a SocketAddress subclass not
	 *             supported by this socket
	 */
	public void connect(SocketAddress endpoint) throws SessionException
	{
		try
		{
			client.connect(endpoint);
		}
		catch (Exception e)
		{
			throw new SessionException(String.format(
					"Unable to connect the server socket %s: %s", endpoint,
					e.getLocalizedMessage()), e);
		}
	}

	/**
	 * @see com.frank.mpnet.Session#beginTransaction()
	 */
	@Override
	public Transaction beginTransaction() throws TransactionException
	{
		return new TransactionTCP(client, SocketType.TCP_CLIENT);
	}

	/**
	 * @see com.frank.mpnet.Session#beginTransaction(java.net.SocketAddress)
	 */
	@Override
	public Transaction beginTransaction(SocketAddress sa)
			throws TransactionException
	{
		if (client.isConnected()
				&& !NetUtils.socketAddressEquals(
						client.getRemoteSocketAddress(), sa))
			throw new TransactionStateException(
					String.format(
							"Failed to connect %s: the current client socket has already connect to server %s.",
							sa, client.getRemoteSocketAddress()));
		connect(sa);
		return new TransactionTCP(client, SocketType.TCP_CLIENT);
	}

	/**
	 * @see com.frank.mpnet.Session#close()
	 */
	@Override
	public void close()
	{
		try
		{
			client.close();
		}
		catch (IOException e)
		{
			throw new SessionException("Failed to close the session: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @see com.frank.mpnet.Session#getSocket()
	 */
	public Socket getSocket()
	{
		return client;
	}

	/**
	 * @see com.frank.mpnet.Session#getLocalAddress()
	 */
	@Override
	public SocketAddress getLocalAddress()
	{
		return client.getLocalSocketAddress();
	}
}
