/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * TransferPolicySelectable.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet.multi;

/**
 * The interface for a component available for selecting a transfer policy.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public interface TransferPolicySelectable
{
	/**
	 * Update the current situation of the transfer policy.
	 * 
	 * @param args
	 *            the new situation
	 */
	public void update(Object... args);
}
