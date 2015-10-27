package com.insight.process;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.insight.app.SaleData;

/**
 * This processor checks if  the sale price of the record is more than 10000. It returns success if the sale price is more than 10000; 
 * else it returns a failure. 
 * 
 */
public class SalePriceProcessor implements Processor {
	private static final Logger logger = Logger.getLogger("com.insight.process.SalePriceProcessor");
	private static final long SALE_LOWER_LIMIT = 10000;
	
	public boolean process(SaleData record) {
		long salePrice = record.getPrice();
		if(salePrice > SALE_LOWER_LIMIT) {
			return true;
		} else {
			logger.log(Level.WARNING, "!!!!! Sale price is less than " + SALE_LOWER_LIMIT + " - " + record.getSourceSaleID() + ", " + record.getPrice());
			return false;
		}
	}
}
