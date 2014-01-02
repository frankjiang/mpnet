/*
 * Copyright (c) 2011, 2020, Frank Jiang and/or its affiliates. All rights reserved.
 * SelectByDataAmount.java is PROPRIETARY/CONFIDENTIAL built in 2013.
 * Use is subject to license terms.
 */
package com.frank.mpnet.multi;

/**
 * The {@linkplain TransferPolicy} selector according to the data amount in
 * transfer.
 * <p>
 * </p>
 * 
 * @author <a href="mailto:jiangfan0576@gmail.com">Frank Jiang</a>
 * @version 1.0.0
 */
public class SelectByDataAmount implements TransferPolicySelect
{
	/**
	 * The flag for whether the endpoint, if <code>true</code> the endpoint is a
	 * server service; otherwise is a client.
	 */
	protected boolean		isServer;
	/**
	 * The polynomial parameters of UDP function.
	 */
	private final double[]	UDP	= new double[] { -0.0007, 0.0123, -0.1369,
			0.9768, -4.3198, 11.0950, -14.5011, 7.8728 };
	/**
	 * The polynomial parameters of TCP function.
	 */
	private final double[]	TCP	= new double[] { 0.0004, -0.0043, 0.0268,
			-0.1029, 0.2302, -0.2652, 0.1166 };

	/**
	 * Construct an instance of <tt>SelectByDataAmount</tt>.
	 * 
	 * @param isServer
	 *            The flag for whether the endpoint, if <code>true</code> the
	 *            endpoint is a server service; otherwise is a client.
	 */
	public SelectByDataAmount(boolean isServer)
	{
		this.isServer = isServer;
	}

	/**
	 * @see com.frank.mpnet.multi.TransferPolicySelect#select(java.lang.Object[])
	 */
	@Override
	public TransferPolicy select(Object... args)
	{
		if (args.length < 1 || !(args[0] instanceof Number))
			throw new IllegalArgumentException(
					"The situation argument is not a number!");
		double x = ((Number) args[0]).doubleValue();
		double[] xs = new double[8];
		xs[xs.length - 1] = 1;
		for (int i = xs.length - 2; i >= 0; i--)
			xs[i] = xs[i + 1] * x;
		double udp = 0, tcp = 0;
		for (int i = 0; i < UDP.length; i++)
			udp += xs[i] * UDP[i];
		for (int i = 0; i < TCP.length; i++)
			tcp += xs[i] * TCP[i];
		if (udp < tcp)
			return TransferPolicy.UDP;
		else
			return isServer ? TransferPolicy.TCP_Server
					: TransferPolicy.TCP_Client;
	}
}
