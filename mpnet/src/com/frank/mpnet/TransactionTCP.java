/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * TransactionTCP.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

/**
 * The transaction for a TCP transmission.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class TransactionTCP extends Transaction
{
	/**
	 * The type of the transaction.
	 */
	protected SocketType	type;
	/**
	 * The underlying TCP client socket.
	 */
	protected Socket		socket;
	/**
	 * The input stream from the socket.
	 */
	protected InputStream	in;
	/**
	 * The output stream from the socket.
	 */
	protected OutputStream	out;
	/**
	 * The remote address.
	 */
	protected SocketAddress	remoteAddr;
	/**
	 * The length of previous read.
	 */
	private int				readLen	= 0;
	/**
	 * The flag for doing {@linkplain OutputStream#flush() flush} for each
	 * {@link #send(SocketAddress, byte[], int, int) send} action.
	 */
	private boolean			doFlush;

	/**
	 * Construct an instance of <tt>TransactionTCP</tt>.
	 * <p>
	 * In this situation, the construction is used for a connected socket from
	 * {@linkplain java.net.ServerSocket#accept() accept} method to get input &
	 * output stream from the specified socket; or this can be a driving client
	 * socket to call a TCP link.
	 * </p>
	 * 
	 * @param socket
	 *            the underlying socket
	 */
	protected TransactionTCP(Socket socket, SocketType type)
	{
		this.socket = socket;
		if (socket.isConnected())
			remoteAddr = socket.getRemoteSocketAddress();
		this.type = type;
	}

	/**
	 * Binds the socket to a local address.
	 * <P>
	 * If the address is <code>null</code>, then the system will pick up an
	 * ephemeral port and a valid local address to bind the socket.
	 * 
	 * @param sa
	 *            the <code>SocketAddress</code> to bind to
	 * @throws TransactionException
	 *             if the bind operation fails, or if the socket is already
	 *             bound. Otherwise, if <code>sa</code> is a SocketAddress
	 *             subclass not supported by this socket
	 */
	public void bind(SocketAddress sa) throws TransactionException
	{
		if (socket == null)
			socket = new Socket();
		try
		{
			socket.bind(sa);
		}
		catch (Exception e)
		{
			throw new TransactionException(
					"Unable to bind the current socket: "
							+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Connects this socket to the server.
	 * 
	 * @param endpoint
	 *            the <code>SocketAddress</code>
	 * @throws TransactionException
	 *             if any exception occurs during connecting
	 * @throws IOException
	 *             if an error occurs during the connection
	 * @throws java.nio.channels.IllegalBlockingModeException
	 *             if this socket has an associated channel, and the channel is
	 *             in non-blocking mode
	 * @throws IllegalArgumentException
	 *             if endpoint is null or is a SocketAddress subclass not
	 *             supported by this socket
	 */
	public void connect(SocketAddress endpoint) throws TransactionException
	{
		try
		{
			socket.connect(endpoint);
			remoteAddr = endpoint;
		}
		catch (Exception e)
		{
			throw new TransactionException(
					"Unable to connect from the current socket: "
							+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Connects this socket to the server with a specified timeout value. A
	 * timeout of zero is interpreted as an infinite timeout. The connection
	 * will then block until established or an error occurs.
	 * 
	 * @param endpoint
	 *            the <code>SocketAddress</code>
	 * @param timeout
	 *            the timeout value to be used in milliseconds.
	 * @throws TransactionException
	 *             if any exception occurs during connecting
	 * @throws IOException
	 *             if an error occurs during the connection
	 * @throws SocketTimeoutException
	 *             if timeout expires before connecting
	 * @throws java.nio.channels.IllegalBlockingModeException
	 *             if this socket has an associated channel, and the channel is
	 *             in non-blocking mode
	 * @throws IllegalArgumentException
	 *             if endpoint is null or is a SocketAddress subclass not
	 *             supported by this socket
	 */
	public void connect(SocketAddress endpoint, int timeout)
			throws TransactionException
	{
		try
		{
			socket.connect(endpoint, timeout);
			remoteAddr = endpoint;
		}
		catch (Exception e)
		{
			throw new TransactionException(
					"Unable to connect from the current socket: "
							+ e.getLocalizedMessage(), e);
		}
	}

	private void send(byte[] b, int offset, int length, boolean doFlush)
	{
		try
		{
			if (out == null)
				out = socket.getOutputStream();
			out.write(b, offset, length);
			if (doFlush)
				out.flush();
		}
		catch (Exception e)
		{
			throw new TransactionException("Fail to send: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @see com.frank.mpnet.Transaction#send(byte[], int, int)
	 */
	@Override
	public void send(byte[] b, int offset, int length)
			throws TransactionStateException, TransactionException
	{
		send(b, offset, length, doFlush);
	}

	/**
	 * Sending the buffered data from the current socket.
	 * <p>
	 * The specified socket address <code>sa</code> will be ignored, it has the
	 * same effect as {@linkplain #send(byte[], int, int)}.
	 * </p>
	 * 
	 * @see com.frank.mpnet.Transaction#send(java.net.SocketAddress, byte[],
	 *      int, int)
	 */
	@Override
	public void send(SocketAddress sa, byte[] b, int offset, int length)
			throws TransactionStateException, TransactionException
	{
		send(b, offset, length);
	}

	/**
	 * @see com.frank.mpnet.Transaction#receive(java.nio.ByteBuffer)
	 */
	@Override
	public SocketInfo receive(ByteBuffer buffer) throws IOException
	{
		if (in == null)
			in = socket.getInputStream();
		int rem = buffer.remaining();
		int i = 0, r = 0;
		do
		{
			r = in.read();
			if (r == -1)
				break;
			buffer.put((byte) r);
			i++;
		}
		while (i < rem);
		readLen = r == -1 ? -1 : i;
		return new SocketInfo(type, remoteAddr);
	}

	/**
	 * @see com.frank.mpnet.Transaction#getType()
	 */
	@Override
	public SocketType getType()
	{
		return type;
	}

	/**
	 * @see com.frank.mpnet.Transaction#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		if (socket == null || socket.isClosed())
			return false;
		return in == null ? true : readLen >= 0;
	}

	/**
	 * @see com.frank.mpnet.Transaction#allocateReceiveBuffer()
	 */
	@Override
	public ByteBuffer allocateReceiveBuffer() throws TransactionException
	{
		if (socket.isClosed())
			return ByteBuffer.allocate(0);
		try
		{
			return ByteBuffer.allocate(socket.getReceiveBufferSize());
		}
		catch (Exception e)
		{
			throw new TransactionException("Failed to allocate the buffer: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * @see com.frank.mpnet.Transaction#close()
	 */
	@Override
	public void close()
	{
		if (socket == null)
			return;
		try
		{
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
		}
		catch (IOException e)
		{
			throw new TransactionException(
					"Failed while closing the transaction: "
							+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Returns an input stream for this socket.
	 * <p>
	 * If this socket has an associated channel then the resulting input stream
	 * delegates all of its operations to the channel. If the channel is in
	 * non-blocking mode then the input stream's <tt>read</tt> operations will
	 * throw an {@link java.nio.channels.IllegalBlockingModeException}.
	 * <p>
	 * Under abnormal conditions the underlying connection may be broken by the
	 * remote host or the network software (for example a connection reset in
	 * the case of TCP connections). When a broken connection is detected by the
	 * network software the following applies to the returned input stream :-
	 * <ul>
	 * <li>
	 * <p>
	 * The network software may discard bytes that are buffered by the socket.
	 * Bytes that aren't discarded by the network software can be read using
	 * {@link java.io.InputStream#read read}.
	 * <li>
	 * <p>
	 * If there are no bytes buffered on the socket, or all buffered bytes have
	 * been consumed by {@link java.io.InputStream#read read}, then all
	 * subsequent calls to {@link java.io.InputStream#read read} will throw an
	 * {@link java.io.IOException IOException}.
	 * <li>
	 * <p>
	 * If there are no bytes buffered on the socket, and the socket has not been
	 * closed using {@link #close close}, then
	 * {@link java.io.InputStream#available available} will return
	 * <code>0</code>.
	 * </ul>
	 * <p>
	 * Closing the returned {@link java.io.InputStream InputStream} will close
	 * the associated socket.
	 * 
	 * @return an input stream for reading bytes from this socket.
	 * @exception IOException
	 *                if an I/O error occurs when creating the input stream, the
	 *                socket is closed, the socket is not connected, or the
	 *                socket input has been shutdown
	 */
	public InputStream getInputStream() throws IOException
	{
		return (in == null) ? socket.getInputStream() : in;
	}

	/**
	 * Returns an output stream for this socket.
	 * <p>
	 * If this socket has an associated channel then the resulting output stream
	 * delegates all of its operations to the channel. If the channel is in
	 * non-blocking mode then the output stream's <tt>write</tt> operations will
	 * throw an {@link java.nio.channels.IllegalBlockingModeException}.
	 * <p>
	 * Closing the returned {@link java.io.OutputStream OutputStream} will close
	 * the associated socket.
	 * 
	 * @return an output stream for writing bytes to this socket.
	 * @exception IOException
	 *                if an I/O error occurs when creating the output stream or
	 *                if the socket is not connected.
	 */
	public OutputStream getOutputStream() throws IOException
	{
		return (out == null) ? socket.getOutputStream() : out;
	}

	/**
	 * Returns <code>true</code> if the transaction will
	 * {@linkplain OutputStream#flush() flush} for each
	 * {@link #send(SocketAddress, byte[], int, int) send} action.
	 * 
	 * @return doFlush flag
	 */
	protected synchronized boolean isDoFlush()
	{
		return doFlush;
	}

	/**
	 * Set doFlush flag.
	 * <p>
	 * <code>true</code> if the transaction will
	 * {@linkplain OutputStream#flush() flush} for each
	 * {@link #send(SocketAddress, byte[], int, int) send} action.
	 * </p>
	 * 
	 * @param doFlush
	 *            the doFlush flag
	 */
	protected synchronized void setDoFlush(boolean doFlush)
	{
		this.doFlush = doFlush;
	}
}
