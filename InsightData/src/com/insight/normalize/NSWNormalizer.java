package com.insight.normalize;

import java.util.List;
import java.util.Date;
import java.text.ParseException;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.insight.app.SaleData;

/**
 * This normalizer normalizes data from CSV file, nsw.csv and returns a normalized record as an instance of <code>com.insight.app.SaleData</code>.
 * 
 */
public class NSWNormalizer implements Normalizer {
/*	private static final String COLUMN_HEADER_ID = "id";
	private static final String COLUMN_HEADER_ADDRESS = "address";
	private static final String COLUMN_HEADER_DATE = "date";
	private static final String COLUMN_HEADER_PRICE = "price";
	private static final String COLUMN_HEADER_BEDROOMS = "bedrooms";
	private static final String COLUMN_HEADER_BATHROOMS = "bathrooms";
	private static final String COLUMN_HEADER_CARPARKS = "carparks";
*/	
	public SaleData normalize(List<Object> record) 
		throws NormalizationException {
		try {
			SaleData saleData = new SaleData();
			if(record.get(0) != null)
				saleData.setSourceSaleID((String)record.get(0));
			
			if(record.get(1) != null)
				saleData.setAddress((String)record.get(1));
			
			if(record.get(2) != null) { 
				String tDate = SaleData.dateFormat.format((Date)record.get(2));
				saleData.setDate(tDate);
			}
			
			if(record.get(3) != null) {
				long lPrice = ((Long)record.get(3)).longValue();
				saleData.setPrice(lPrice);
			}
						
			if((record.size() >= 5) && (record.get(4) != null)) {
				int lBedrooms = ((Integer)record.get(4)).intValue();
				saleData.setBedrooms(lBedrooms);
			} 
			
			if((record.size() >= 6) && (record.get(5) != null)) {
				int lBathrooms = ((Integer)record.get(5)).intValue();
				saleData.setBathrooms(lBathrooms);
			}
			
			if((record.size() >= 7) && (record.get(6) != null)) {
				int lCarparks = ((Integer)record.get(6)).intValue();
				saleData.setCarparks(lCarparks);
			}
			
			return saleData;
		} catch(ParseException e) {
			throw new NormalizationException(e);
		}
	}

	public CellProcessor[] getFullCellProcessors() {
		CellProcessor[] ret = new CellProcessor[] { 
			new NotNull(), //COLUMN_HEADER_ID
			new NotNull(), //COLUMN_HEADER_ADDRESS
			new ParseDate("yyyy-MM-dd"), //COLUMN_HEADER_DATE
			new NotNull(new ParseLong()), //COLUMN_HEADER_PRICE
			new Optional(new ParseInt()), //COLUMN_HEADER_BEDROOMS
			new Optional(new ParseInt()), //COLUMN_HEADER_BATHROOMS
			new Optional(new ParseInt()) //COLUMN_HEADER_CARPARKS
		};		
		return ret;
	}

	public CellProcessor[] getPartialCellProcessors() {
		CellProcessor[] ret = new CellProcessor[] { 
			new NotNull(), //COLUMN_HEADER_ID
			new NotNull(), //COLUMN_HEADER_ADDRESS
			new ParseDate("yyyy-MM-dd"), //COLUMN_HEADER_DATE
			new NotNull(new ParseLong()) //COLUMN_HEADER_PRICE
		};		
		return ret;
	}
}
