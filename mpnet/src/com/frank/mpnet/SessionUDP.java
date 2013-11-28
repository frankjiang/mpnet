/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * SessionUDP.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * The net transmission session using UDP.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class SessionUDP extends Session
{
	/**
	 * The underlying transmission UDP socket.
	 */
	protected DatagramSocket	socket;
	/**
	 * The remote socket address.
	 */
	protected SocketAddress		remoteAddr;

	/**
	 * Construct an instance of <tt>SessionUDP</tt> with an unbound UDP socket.
	 * 
	 * @throws SessionException
	 *             if the socket could not be opened, or the socket could not
	 *             bind to the specified local port.
	 */
	public SessionUDP() throws SessionException
	{
		try
		{
			socket = new DatagramSocket();
		}
		catch (SocketException | SecurityException e)
		{
			throw new SessionException("Error create session: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Construct an instance of <tt>SessionUDP</tt> and bind it to the specified
	 * socket address(usually an {@linkplain java.net.InetAddress Internet
	 * address} with a {@linkplain Integer port number}).
	 * 
	 * @param bindAddr
	 *            the socket address to bind
	 * @throws SessionException
	 *             if the socket could not be opened, or the socket could not
	 *             bind to the specified local port.
	 */
	public SessionUDP(SocketAddress bindAddr) throws SessionException
	{
		try
		{
			socket = new DatagramSocket(bindAddr);
		}
		catch (SocketException e)
		{
			throw new SessionException("Error create session: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Construct an instance of <tt>SessionUDP</tt> with specified underlying
	 * datagram socket.
	 * 
	 * @param socket
	 *            the specified datagram socket
	 */
	SessionUDP(DatagramSocket socket)
	{
		this.socket = socket;
	}

	/**
	 * Binds underlying socket of the current session to a specific address &
	 * port.
	 * <p>
	 * If the address is <code>null</code>, then the system will pick up an
	 * ephemeral port and a valid local address to bind the socket.
	 * <p>
	 * 
	 * @param sa
	 *            The address & port to bind to.
	 * @throws SessionException
	 *             if any error happens during the bind, such as the socket is
	 *             already bound, a security manager exists and its
	 *             <code>checkListen</code> method doesn't allow the operation,
	 *             or <code>sa</code> is a SocketAddress subclass not supported
	 *             by this socket.
	 */
	public void bind(SocketAddress sa) throws SessionException
	{
		try
		{
			socket.bind(sa);
		}
		catch (SocketException | SecurityException | IllegalArgumentException e)
		{
			throw new SessionException("Unable to bind socket address: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @see com.frank.mpnet.Session#beginTransaction()
	 */
	@Override
	public Transaction beginTransaction() throws TransactionException
	{
		return new TransactionUDP(socket);
	}

	/**
	 * @see com.frank.mpnet.Session#close()
	 */
	@Override
	public void close() throws SessionException
	{
		try
		{
			socket.close();
		}
		catch (Exception e)
		{
			throw new SessionException("Failed to close the session: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @see com.frank.mpnet.Session#beginTransaction(java.net.SocketAddress)
	 */
	@Override
	public Transaction beginTransaction(SocketAddress sa)
			throws TransactionException
	{
		try
		{
			socket.connect(sa);
		}
		catch (SocketException | SecurityException | IllegalArgumentException e)
		{
			throw new TransactionException("Unable to connect socket address: "
					+ e.getLocalizedMessage(), e);
		}
		return new TransactionUDP(socket);
	}

	/**
	 * Returns the exact underlying datagram socket in the session.
	 * 
	 * @return the datagram socket
	 */
	public DatagramSocket getSocket()
	{
		return socket;
	}

	/**
	 * @see com.frank.mpnet.Session#getLocalAddress()
	 */
	@Override
	public SocketAddress getLocalAddress()
	{
		return (socket == null) ? null : socket.getLocalSocketAddress();
	}
}
