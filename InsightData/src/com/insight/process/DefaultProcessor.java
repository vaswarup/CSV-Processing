package com.insight.process;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.insight.app.SaleData;

/**
 * This is a default processor that logs a record being processed and always returns success.
 * 
 */
public class DefaultProcessor implements Processor {
	private static final Logger logger = Logger.getLogger("com.insight.process.DefaultProcessor");

	public boolean process(SaleData record) {
		logger.log(Level.FINE, record.toString());
		return true;
	}
}
