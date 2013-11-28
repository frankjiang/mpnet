/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * Session.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

import java.net.SocketAddress;

/**
 * The net transmission session.
 * <p>
 * In a transmission session, the target of the session is determined.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public abstract class Session implements java.io.Closeable
{
	/**
	 * Get a new transaction from the current session.
	 * 
	 * @return the new transaction
	 * @throws TransactionException
	 *             if any transaction error occurs
	 */
	public abstract Transaction beginTransaction() throws TransactionException;

	/**
	 * Get a new transaction with the specified socket address.
	 * 
	 * @param sa
	 *            the specified socket address to communicate with
	 * @return the new transaction
	 * @throws TransactionException
	 *             if fail to create a new transaction, or any transaction error
	 *             occurs
	 */
	public abstract Transaction beginTransaction(SocketAddress sa)
			throws TransactionException;

	/**
	 * Closes this session and releases any system resources associated with it.
	 * If the socket is already closed then invoking this method has no effect.
	 * 
	 * @see java.io.Closeable#close()
	 * @throws SessionException
	 *             if any exception occurs during closing the session
	 */
	public abstract void close() throws SessionException;

	/**
	 * Returns the underlying socket in the session.
	 * 
	 * @return the underlying socket
	 */
	public abstract Object getSocket();

	/**
	 * Returns the local address of the underlying socket in the current
	 * session, <code>null</code> if unbound.
	 * 
	 * @return the local address of the underlying socket in the current
	 *         session, <code>null</code> if unbound
	 */
	public abstract SocketAddress getLocalAddress();
}
