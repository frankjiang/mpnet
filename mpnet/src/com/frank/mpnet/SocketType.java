/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * SocketType.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet;

/**
 * The socket type of a multi-socket.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public enum SocketType
{
	/**
	 * Infers the socket type is a multi-protocol socket.
	 */
	MultiSocket,
	/**
	 * Infers the socket type is a TCP server socket.
	 */
	TCP_SERVER,
	/**
	 * Infers the socket type is a TCP client socket.
	 */
	TCP_CLIENT,
	/**
	 * Infers the socket type is a UDP socket.
	 */
	UDP;
}
