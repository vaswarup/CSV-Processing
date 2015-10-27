package com.insight.normalize;

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.insight.app.SaleData;

/**
 * This is a default normalizer that logs a record being normalized and always returns an empty normalized record as an instance of 
 * <code>com.insight.app.SaleData</code>.
 * 
 */
public class DefaultNormalizer implements Normalizer {
	private static final Logger logger = Logger.getLogger("com.insight.normalize.DefaultNormalizer");

	public SaleData normalize(List<Object> record) {
		logger.log(Level.FINE, record.toString());
		SaleData saleData = new SaleData();
		return saleData;
	}

	public CellProcessor[] getFullCellProcessors() {
		CellProcessor[] ret = new CellProcessor[] { 
			new NotNull(), //id
			new NotNull(), //address
			new NotNull(), //prices
			new NotNull(), //date
			new Optional(), //bedrooms
			new Optional(), //bathrooms
			new Optional() //carparks
		};		
		return ret;
	}

	public CellProcessor[] getPartialCellProcessors() {
		CellProcessor[] ret = new CellProcessor[] { 
			new NotNull(), //id
			new NotNull(), //address
			new NotNull(), //prices
			null, //date
			null, //bedrooms
			null, //bathrooms
			null //carparks
		};		
		return ret;
	}
}
