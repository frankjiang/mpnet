/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * TransferPolicySelect.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet.multi;

/**
 * The interface of selecting a transfer policy.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public interface TransferPolicySelect
{
	/**
	 * Select a transfer policy from the current situation.
	 * 
	 * @param args
	 *            the arguments of the current situation.
	 * @return the selected policy
	 */
	public TransferPolicy select(Object... args);
}
