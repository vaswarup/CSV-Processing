package com.insight.process;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.insight.app.SaleData;

/**
 * This processor checks if the sale year of the record is 2016 or later. It returns success if the sale year is before 2016; 
 * else it returns a failure. 
 * 
 */
public class SaleYearProcessor implements Processor {
	private static final Logger logger = Logger.getLogger("com.insight.process.SaleYearProcessor");
	private static final int SALE_UPPER_LIMIT = 2016;
	
	public boolean process(SaleData record) {
		int saleYear = record.getYear();
		if(saleYear < SALE_UPPER_LIMIT) {
			return true;
		} else {
			logger.log(Level.WARNING, "!!!!! Sale year after " + SALE_UPPER_LIMIT + " - " + record.getSourceSaleID() + ", " + record.getYear());
			return false;
		}
	}
}
