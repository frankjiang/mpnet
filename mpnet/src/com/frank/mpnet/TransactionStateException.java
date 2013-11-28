/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * ConfigureException.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

/**
 * The transaction state exception.
 * <p>
 * A transaction state exception refers to an exception for which the function
 * be recalled is not agreed with the current transaction state.
 * </p>
 * <p>
 * <strong>Example</strong><br>
 * <strong>1.UDP</strong><br>
 * call the {@linkplain Transaction#send(byte[], int, int) default sending}
 * method without connected to a remote socket address & port;<br>
 * <strong>2.TCP</strong><br>
 * call the {@linkplain Transaction#send(SocketAddress, byte[], int, int)
 * specified sending} method while the TCP server/client already connected to a
 * remote socket.
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class TransactionStateException extends RuntimeException
{
	/**
	 * serialVersionUID.
	 */
	private static final long	serialVersionUID	= -4854641796235620050L;

	/**
	 * Constructs a new transaction state exception with {@code null} as its
	 * detail message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 */
	public TransactionStateException()
	{
		super();
	}

	/**
	 * Constructs a new transaction state exception with the specified detail
	 * message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public TransactionStateException(String message)
	{
		super(message);
	}

	/**
	 * Constructs a new transaction state exception with the specified detail
	 * message and cause.
	 * <p>
	 * Note that the detail message associated with {@code cause} is <i>not</i>
	 * automatically incorporated in this runtime exception's detail message.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 * @since 1.4
	 */
	public TransactionStateException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a new transaction state exception with the specified cause and
	 * a detail message of <tt>(cause==null ? null : cause.toString())</tt>
	 * (which typically contains the class and detail message of <tt>cause</tt>
	 * ). This constructor is useful for runtime exceptions that are little more
	 * than wrappers for other throwables.
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 * @since 1.4
	 */
	public TransactionStateException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * Constructs a new transaction state exception with the specified detail
	 * message, cause, suppression enabled or disabled, and writable stack trace
	 * enabled or disabled.
	 * 
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause. (A {@code null} value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 * @param enableSuppression
	 *            whether or not suppression is enabled or disabled
	 * @param writableStackTrace
	 *            whether or not the stack trace should be writable
	 * @since 1.7
	 */
	protected TransactionStateException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
