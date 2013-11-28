/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * Transaction.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * The transaction interface for net transmission.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class Transaction
{
	/**
	 * Send specified data to the default socket.
	 * <p>
	 * Process a sending transaction, transfer the specified part of the
	 * specified data using the underlying socket.
	 * </p>
	 * <p>
	 * <strong>Notice</strong><br>
	 * In UDP, there is a maximum datagram packet limit. The sub-class which
	 * implements {@linkplain Transaction} with UDP should deal with this
	 * exception.<br>
	 * For suggestion, one could divide the original data array into several
	 * part and sending it without order guarantee; other, may just throw an
	 * {@link TransactionException exception} for the illegal input.
	 * </p>
	 * 
	 * @param b
	 *            the source data to transfer
	 * @param offset
	 *            the offset of data to transfer
	 * @param length
	 *            the maximum length to transfer
	 * @throws TransactionStateException
	 *             if the transaction state does not agree
	 * @throws TransactionException
	 *             if any other transaction error occurs, such as an IO error
	 */
	public abstract void send(byte[] b, int offset, int length)
			throws TransactionStateException, TransactionException;

	/**
	 * Send specified data to the default socket.
	 * <p>
	 * Process a sending transaction, transfer all of the specified data using
	 * the underlying socket.
	 * </p>
	 * 
	 * @param b
	 *            the source data to transfer
	 * @throws TransactionStateException
	 *             if the transaction state does not agree
	 * @throws TransactionException
	 *             if any other transaction error occurs, such as an IO error
	 * @see #send(byte[], int, int)
	 */
	public void send(byte[] b) throws TransactionStateException,
			TransactionException
	{
		send(b, 0, b.length);
	}

	/**
	 * Send a string from this socket to default endpoint with the string
	 * encoded by specified character set.
	 * 
	 * @param s
	 *            the string to send
	 * @param cs
	 *            the encoding character set
	 * @throws TransactionStateException
	 *             if the transaction state does not agree
	 * @throws TransactionException
	 *             if any other transaction error occurs, such as an IO error
	 */
	public void send(String s, Charset cs) throws TransactionStateException,
			TransactionException
	{
		send(s.getBytes(cs));
	}

	/**
	 * Send a string from this socket to default endpoint with the string
	 * encoded by default character set.
	 * 
	 * @param s
	 *            the string to send
	 * @throws TransactionStateException
	 *             if the transaction state does not agree
	 * @throws TransactionException
	 *             if any other transaction error occurs
	 */
	public void send(String s) throws TransactionStateException,
			TransactionException
	{
		send(s.getBytes());
	}

	/**
	 * Send specified data to a specified socket.
	 * <p>
	 * Process a sending transaction, transfer the specified part of the
	 * specified data using the underlying socket.
	 * </p>
	 * <p>
	 * <strong>Notice</strong><br>
	 * In UDP, there is a maximum datagram packet limit. The sub-class which
	 * implements {@linkplain Transaction} with UDP should deal with this
	 * exception.<br>
	 * For suggestion, one could divide the original data array into several
	 * part and sending it without order guarantee; other, may just throw an
	 * {@link TransactionException exception} for the illegal input.
	 * </p>
	 * 
	 * @param sa
	 *            the socket address(usually an
	 *            {@linkplain java.net.InetAddress Internet address} with a
	 *            {@linkplain Integer port number}).
	 * @param b
	 *            the source data to transfer
	 * @param offset
	 *            the offset of data to transfer
	 * @param length
	 *            the maximum length to transfer
	 * @throws TransactionStateException
	 *             if the transaction state does not agree
	 * @throws TransactionException
	 *             if any other transaction error occurs
	 */
	public abstract void send(SocketAddress sa, byte[] b, int offset, int length)
			throws TransactionStateException, TransactionException;

	/**
	 * Send specified data to a specified socket.
	 * <p>
	 * Process a sending transaction, transfer all of the specified data using
	 * the underlying socket.
	 * </p>
	 * 
	 * @param sa
	 *            the socket address(usually an
	 *            {@linkplain java.net.InetAddress Internet address} with a
	 *            {@linkplain Integer port number}).
	 * @param b
	 *            the source data to transfer
	 * @throws TransactionStateException
	 *             if the transaction state does not agree
	 * @throws TransactionException
	 *             if any other transaction error occurs, such as an IO error
	 * @see #send(SocketAddress, byte[], int, int)
	 */
	public void send(SocketAddress sa, byte[] b)
			throws TransactionStateException, TransactionException
	{
		send(sa, b, 0, b.length);
	}

	/**
	 * Send a string from this socket to specified endpoint with the string
	 * encoded by specified character set.
	 * 
	 * @param sa
	 *            the socket address to send to
	 * @param s
	 *            the string to send
	 * @param cs
	 *            the encoding character set
	 * @throws TransactionStateException
	 *             if the transaction state does not agree
	 * @throws TransactionException
	 *             if any other transaction error occurs, such as an IO error
	 */
	public void send(SocketAddress sa, String s, Charset cs)
			throws TransactionStateException, TransactionException
	{
		send(sa, s.getBytes(cs));
	}

	/**
	 * Send a string from this socket to specified endpoint with the string
	 * encoded by default character set.
	 * 
	 * @param sa
	 *            the socket address to send to
	 * @param s
	 *            the string to send
	 * @throws TransactionStateException
	 *             if the transaction state does not agree
	 * @throws TransactionException
	 *             if any other transaction error occurs, such as an IO error
	 */
	public void send(SocketAddress sa, String s)
			throws TransactionStateException, TransactionException
	{
		send(sa, s.getBytes());
	}

	/**
	 * Receive the transfered data and fill the specified buffer area with
	 * transfered data.
	 * 
	 * @param buffer
	 *            the byte buffer to fill
	 * @return the socket information bean which contains the source data
	 * @throws IOException
	 *             if IO error occurs
	 */
	public abstract SocketInfo receive(ByteBuffer buffer) throws IOException;

	/**
	 * Receive the transfered data and fill the specified
	 * {@linkplain StringBuffer}.
	 * <p>
	 * <strong>Warning</strong><br>
	 * Pay attention to the encoding character set of the data. If there is any
	 * special characters in the transfer data, use
	 * {@linkplain #receive(ByteBuffer)} instead.
	 * </p>
	 * 
	 * @param buffer
	 *            the string buffer to fill
	 * @param maxLen
	 *            the maximum length to read
	 * @return the source socket information
	 * @throws IOException
	 */
	public SocketInfo receive(StringBuffer buffer, int maxLen)
			throws IOException
	{
		ByteBuffer bbuf = ByteBuffer.allocate(maxLen);
		SocketInfo si = receive(bbuf);
		buffer.append(bbuf.asCharBuffer());
		return si;
	}

	/**
	 * Returns the underlying socket type in the transaction.
	 * <p>
	 * The subclass which implements it shall returns the transaction type of
	 * the current using socket.
	 * </p>
	 * 
	 * @return the underlying socket type
	 */
	public abstract SocketType getType();

	/**
	 * Returns <code>true</code> if there is anything left to receive.
	 * 
	 * @return <code>true</code> if there is anything left to receive.
	 * @throws TransactionException
	 *             if any exception occurs during retrieving current receiving
	 *             state
	 */
	public abstract boolean hasNext() throws TransactionException;

	/**
	 * Allocate a {@linkplain ByteBuffer} for receiving transmission data
	 * according to the underlying socket implementation.
	 * <p>
	 * In this method, the buffer size cannot guarantee that the packet will not
	 * be truncated, it will give a normal capacity that can catch most of the
	 * packet, for example, the receive buffer size of the underlying socket. In
	 * order to guarantee that the receiver will never truncate the packet, the
	 * sender should guarantee that the packet size will not be larger than the
	 * buffer size. Thus, use {@linkplain ByteBuffer#allocate(int)} instead.
	 * </p>
	 * 
	 * @return the receiving buffer
	 * @throws TransactionException
	 *             if any exception occurs during retrieving the buffer size or
	 *             allocating the memories.
	 */
	public abstract ByteBuffer allocateReceiveBuffer()
			throws TransactionException;

	/**
	 * Close the current transaction and release any system resource bound with
	 * it.
	 * 
	 * @throws TransactionException
	 *             if any exception occurs during closing the transaction
	 */
	public abstract void close() throws TransactionException;
}
