/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * SessionTCPServer.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

/**
 * The net transmission session using TCP.
 * <p>
 * In this session the session is only the server point of the session. It will
 * passively waiting for a connection from the client point. If
 * {@linkplain #beginTransaction()} is called, the thread will be blocked until
 * a connection is started.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class SessionTCPServer extends Session
{
	/**
	 * The server socket.
	 */
	protected ServerSocket	server;

	/**
	 * Construct an instance of <tt>SessionTCPServer</tt> with unbound server
	 * socket.
	 * 
	 * @throws SessionException
	 *             if any exception occurs during creating an unbound server
	 *             socket session.
	 */
	public SessionTCPServer() throws SessionException
	{
		try
		{
			server = new ServerSocket();
		}
		catch (IOException e)
		{
			throw new SessionException(
					"Unable to create an unbound server socket: "
							+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Construct an instance of <tt>SessionTCPServer</tt> with unbound server
	 * socket.
	 * 
	 * @param bindAddr
	 *            the socket address to bind
	 * @throws SessionException
	 *             if any exception occurs during creating an unbound server
	 *             socket session.
	 */
	public SessionTCPServer(SocketAddress bindAddr) throws SessionException
	{
		try
		{
			server = new ServerSocket();
			bind(bindAddr);
		}
		catch (IOException e)
		{
			throw new SessionException("Unable to create a server socket: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Construct an instance of <tt>SessionTCPServer</tt> with it binding local
	 * port.
	 * 
	 * @param port
	 *            the port number, or 0 to use a port number that is
	 *            automatically allocated
	 * @throws SessionException
	 *             if any exception occurs during creating an unbound server
	 *             socket session.
	 */
	public SessionTCPServer(int port) throws SessionException
	{
		try
		{
			server = new ServerSocket(port);
		}
		catch (IOException e)
		{
			throw new SessionException("Unable to create a server socket: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Construct an instance of <tt>SessionTCPServer</tt> with it binding local
	 * port and listen backlog.
	 * 
	 * @param port
	 *            the port number, or 0 to use a port number that is
	 *            automatically allocated
	 * @param backlog
	 *            requested maximum length of the queue of incoming connections
	 * @throws SessionException
	 *             if any exception occurs during creating an unbound server
	 *             socket session.
	 */
	public SessionTCPServer(int port, int backlog) throws SessionException
	{
		try
		{
			server = new ServerSocket(port, backlog);
		}
		catch (IOException e)
		{
			throw new SessionException("Unable to create a server socket: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Construct an instance of <tt>SessionTCPServer</tt> with specified
	 * underlying server socket.
	 * 
	 * @param server
	 *            the specified server socket
	 */
	SessionTCPServer(ServerSocket server)
	{
		this.server = server;
	}

	/**
	 * Construct an instance of <tt>SessionTCPServer</tt> with a server socket
	 * bound with a specified socket address and listen backlog.
	 * 
	 * @param bindAddr
	 *            the socket address to bind
	 * @param backlog
	 *            requested maximum length of the queue of incoming connections
	 * @throws SessionException
	 *             if any exception occurs during creating an unbound server
	 *             socket session.
	 */
	public SessionTCPServer(SocketAddress bindAddr, int backlog)
			throws SessionException
	{
		if (bindAddr instanceof InetSocketAddress)
		{
			InetSocketAddress isa = (InetSocketAddress) bindAddr;
			try
			{
				server = new ServerSocket(isa.getPort(), backlog,
						((InetSocketAddress) bindAddr).getAddress());
			}
			catch (IOException e)
			{
				throw new SessionException("Unable to create a server socket: "
						+ e.getLocalizedMessage(), e);
			}
		}
		else
			throw new SessionException("Unsupported socket type: "
					+ bindAddr.toString());
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
			server.bind(endpoint);
		}
		catch (Exception e)
		{
			throw new SessionException("Unable to bind the server socket: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Binds the <code>ServerSocket</code> to a specific address (IP address and
	 * port number).
	 * <p>
	 * If the address is <code>null</code>, then the system will pick up an
	 * ephemeral port and a valid local address to bind the socket.
	 * <P>
	 * The <code>backlog</code> argument is the requested maximum number of
	 * pending connections on the socket. Its exact semantics are implementation
	 * specific. In particular, an implementation may impose a maximum length or
	 * may choose to ignore the parameter altogther. The value provided should
	 * be greater than <code>0</code>. If it is less than or equal to
	 * <code>0</code>, then an implementation specific default will be used.
	 * 
	 * @param endpoint
	 *            The IP address & port number to bind to.
	 * @param backlog
	 *            requested maximum length of the queue of incoming connections.
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
	public void bind(SocketAddress endpoint, int backlog)
			throws SessionException
	{
		try
		{
			server.bind(endpoint, backlog);
		}
		catch (Exception e)
		{
			throw new SessionException("Unable to bind the server socket: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @see com.frank.mpnet.Session#beginTransaction()
	 */
	@Override
	public Transaction beginTransaction() throws TransactionException
	{
		try
		{
			return new TransactionTCP(server.accept(), SocketType.TCP_SERVER);
		}
		catch (IOException e)
		{
			throw new TransactionException("Unable to begin a transaction: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Get a new transaction from the current session.
	 * <p>
	 * While the TCP server session need no remote address, thus, the specified
	 * socket address will be ignored, it will have the same effect as
	 * {@linkplain #beginTransaction()}.
	 * </p>
	 * 
	 * @see com.frank.mpnet.Session#beginTransaction(java.net.SocketAddress)
	 */
	@Override
	public Transaction beginTransaction(SocketAddress sa)
			throws TransactionException
	{
		return beginTransaction();
	}

	/**
	 * @see com.frank.mpnet.Session#close()
	 */
	@Override
	public void close()
	{
		try
		{
			server.close();
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
	@Override
	public ServerSocket getSocket()
	{
		return server;
	}

	/**
	 * @see com.frank.mpnet.Session#getLocalAddress()
	 */
	@Override
	public SocketAddress getLocalAddress()
	{
		return (server == null) ? null : server.getLocalSocketAddress();
	}
}
