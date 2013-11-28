/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * TransactionUDP.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * The transaction implementation for UDP.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class TransactionUDP extends Transaction
{
	/**
	 * The policy of been involved in a situation that sending a UDP packet
	 * whose size is larger than the {@linkplain TransactionUDP#maxLength
	 * maximum packet length}.
	 * 
	 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
	 * @version 1.0.0
	 */
	public static enum Policy
	{
		/**
		 * Throws a {@linkplain TransactionException} if the packet size is too
		 * large.
		 */
		Exception,
		/**
		 * Subcontracts the over-size packet into several equal size packets if
		 * the packet size is too large.
		 */
		Subcontracting;
	}

	/**
	 * The protocol supported maximum UDP packet length.
	 * <p>
	 * The maximum length of UDP packet is <code>0xffff</code>, removes the IP
	 * header(20) and the UDP header(8), the body of UDP packet maximum length
	 * is shown as <code>0xffff - 28</code>.
	 * </p>
	 */
	public static final int			MAX_UDP_PACKET	= 0xffff - 28;
	/**
	 * The customize maximum UDP datagram length.
	 */
	protected volatile int			maxLength;
	/**
	 * The type of the transaction.
	 */
	private static final SocketType	TYPE			= SocketType.UDP;
	/**
	 * The datagram socket in the net transmission session.
	 */
	protected DatagramSocket		socket;
	/**
	 * The connected remote socket address.
	 */
	protected SocketAddress			remoteAddr;
	/**
	 * The policy of dealing packet size overflow.
	 */
	protected volatile Policy		policy;
	/**
	 * The policy lock.
	 */
	private Object					policyLock		= new Object();

	/**
	 * Construct an instance of <tt>TransactionUDP</tt> with specified
	 * underlying socket and packet size overflow dealing policy.
	 * 
	 * @param socket
	 *            the datagram socket in the net transmission session
	 * @param maxLength
	 *            the maximum length of the packet
	 * @param policy
	 *            the packet size overflow dealing policy, <code>null</code> if
	 *            use default policy (subcontracting)
	 * @throws IllegalArgumentException
	 *             if uses the <code>maxLength</code> is not a positive number,
	 *             or larger than {@linkplain #MAX_UDP_PACKET}
	 */
	protected TransactionUDP(DatagramSocket socket, int maxLength, Policy policy)
			throws IllegalArgumentException
	{
		this.socket = socket;
		remoteAddr = socket.isConnected() ? socket.getRemoteSocketAddress()
				: null;
		this.policy = policy == null ? Policy.Subcontracting : policy;
		if (maxLength < 1 || maxLength > MAX_UDP_PACKET)
			throw new IllegalArgumentException(String.format(
					"The maxLength(%d) shall be positive and within %d.",
					maxLength, MAX_UDP_PACKET));
		this.maxLength = maxLength;
	}

	/**
	 * Construct an instance of <tt>TransactionUDP</tt> with specified
	 * underlying socket.
	 * 
	 * @param socket
	 *            the datagram socket in the net transmission session
	 */
	protected TransactionUDP(DatagramSocket socket)
	{
		this(socket, MAX_UDP_PACKET, Policy.Subcontracting);
	}

	/**
	 * Returns the policy of dealing packet size overflow.
	 * 
	 * @return the policy of dealing packet size overflow
	 */
	public Policy getPolicy()
	{
		synchronized (policyLock)
		{
			return policy;
		}
	}

	/**
	 * Set the policy of dealing packet size overflow.
	 * 
	 * @param policy
	 *            the policy to set
	 */
	public void setPolicy(Policy policy)
	{
		synchronized (policyLock)
		{
			this.policy = policy;
		}
	}

	/**
	 * @see com.frank.mpnet.Transaction#send(byte[], int, int)
	 */
	@Override
	public void send(byte[] b, int offset, int length)
			throws TransactionStateException, TransactionException
	{
		if (remoteAddr == null)
			throw new TransactionStateException(
					"The current socket has not connected to a remote address.");
		switch (getPolicy())
		{
			default:
			case Subcontracting:
				sendWrap(remoteAddr, b, offset, length);
				break;
			case Exception:
				sendDirect(remoteAddr, b, offset, length);
				break;
		}
	}

	/**
	 * @see com.frank.mpnet.Transaction#send(java.net.SocketAddress, byte[],
	 *      int, int)
	 */
	@Override
	public void send(SocketAddress sa, byte[] b, int offset, int length)
			throws TransactionStateException, TransactionException
	{
		if (remoteAddr != null && sa != null
				&& NetUtils.socketAddressEquals(remoteAddr, sa))
			throw new TransactionStateException(String.format(
					"Current socket is connected to %s, cannot bind %s.",
					remoteAddr, sa));
		switch (getPolicy())
		{
			default:
			case Subcontracting:
				sendWrap(sa == null ? remoteAddr : sa, b, offset, length);
				break;
			case Exception:
				sendDirect(sa == null ? remoteAddr : sa, b, offset, length);
				break;
		}
	}

	/**
	 * Wrap the maximum data buffer and send the buffer.
	 * <p>
	 * While sending the buffered data, the buffer may be divided into several
	 * equal size parts accordingly, if the buffer size is larger than the
	 * customized maximum packet size.
	 * </p>
	 * 
	 * @param sa
	 *            the socket address to send to
	 * @param b
	 *            the data buffer to send
	 * @param offset
	 *            the offset of the buffered data to send
	 * @param length
	 *            the length of the maximum length to send
	 * @throws TransactionException
	 */
	private void sendWrap(SocketAddress sa, byte[] b, int offset, int length)
			throws TransactionException
	{
		try
		{
			int max = getMaxLength();
			if (length < max)
				socket.send(new DatagramPacket(b, offset, length, sa));
			else
			{
				int n = (int) Math.ceil(length
						/ Math.ceil(length / (double) max));
				byte[] buff = new byte[n];
				while (length > 0)
				{
					System.arraycopy(b, offset, buff, 0, n);
					socket.send(new DatagramPacket(buff, 0, n, sa));
					offset += n;
					if (n >= length)
					{
						n = length;
						length = 0;
					}
					else
					{
						length -= n;
						if (n > length)
							n = length;
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new TransactionException("Unable to send data: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Send the buffered data directly.
	 * <p>
	 * If <code>length</code> is larger than the {@linkplain #maxLength
	 * customized packet size}, a {@linkplain TransactionException} will be
	 * thrown.
	 * </p>
	 * 
	 * @param sa
	 *            the socket address to send to
	 * @param b
	 *            the data buffer to send
	 * @param offset
	 *            the offset of the buffered data to send
	 * @param length
	 *            the length of the maximum length to send
	 * @throws TransactionException
	 *             if <code>length</code> is larger than the
	 *             {@linkplain #maxLength customized packet size}
	 */
	private void sendDirect(SocketAddress sa, byte[] b, int offset, int length)
			throws TransactionException
	{
		int max = getMaxLength();
		if (length > max)
			throw new TransactionException(
					String.format(
							"The size of buffer to send(%d) is larger than the maximum length(%d).",
							length, max));
	}

	/**
	 * @see com.frank.mpnet.Transaction#receive(java.nio.ByteBuffer)
	 */
	@Override
	public SocketInfo receive(ByteBuffer buffer) throws IOException
	{
		int offset = buffer.position();
		int length = buffer.remaining();
		DatagramPacket p = new DatagramPacket(buffer.array(), offset, length);
		socket.receive(p);
		buffer.put(p.getData(), p.getOffset(), p.getLength());
		return new SocketInfo(TYPE, p.getSocketAddress());
	}

	/**
	 * @see com.frank.mpnet.Transaction#getType()
	 */
	@Override
	public SocketType getType()
	{
		return TYPE;
	}

	/**
	 * @see com.frank.mpnet.Transaction#hasNext()
	 */
	@Override
	public boolean hasNext() throws TransactionException
	{
		if (socket == null || socket.isClosed())
			return false;
		return true;
	}

	/**
	 * @see com.frank.mpnet.Transaction#allocateReceiveBuffer()
	 */
	@Override
	public ByteBuffer allocateReceiveBuffer() throws TransactionException
	{
		if (socket.isClosed())
			return ByteBuffer.allocate(0);
		if (socket == null)
			throw new TransactionException(
					"The underlying socket is not defined.");
		try
		{
			return ByteBuffer.allocate(socket.getReceiveBufferSize());
		}
		catch (SocketException e)
		{
			return ByteBuffer.allocate(MAX_UDP_PACKET);
		}
		catch (Exception e)
		{
			throw new TransactionException("Unable to allocate byte buffer: "
					+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Customize the maximum UDP packet length.
	 * 
	 * @param maxLength
	 *            the maximum UDP packet length
	 * @throws IllegalArgumentException
	 *             if <code>maxLength</code> is less than 1 or larger than
	 *             {@linkplain #MAX_UDP_PACKET}.
	 */
	protected synchronized void setMaxLength(int maxLength)
			throws IllegalArgumentException
	{
		if (maxLength < 1 || maxLength > MAX_UDP_PACKET)
			throw new IllegalArgumentException(String.format(
					"Packet length:%d is not supported by the UDP.", maxLength));
		this.maxLength = maxLength;
	}

	/**
	 * Returns the customized maximum UDP packet length.
	 * 
	 * @return the packet length
	 */
	public synchronized int getMaxLength()
	{
		return maxLength;
	}

	/**
	 * Close the current transaction.
	 * <p>
	 * If the underlying datagram socket is connect to any remote socket
	 * address, disconnect it, otherwise do nothing.
	 * </p>
	 * <p>
	 * If need to close the underlying datagram socket, please
	 * {@linkplain com.frank.mpnet.Session#close() close session}.
	 * </p>
	 * 
	 * @see com.frank.mpnet.Transaction#close()
	 * @see com.frank.mpnet.Session#close()
	 */
	@Override
	public void close()
	{
		if (socket != null)
			socket.disconnect();
	}
}
