/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * TransferPolicy.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet.multi;

/**
 * The transfer policy.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public enum TransferPolicy
{
	/**
	 * Use TCP as server.
	 */
	TCP_Server,
	/**
	 * Use TCP as client.
	 */
	TCP_Client,
	/**
	 * Use UDP.
	 */
	UDP;
}
