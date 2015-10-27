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
 * This normalizer normalizes data from CSV file, sa.csv and returns a normalized record as an instance of <code>com.insight.app.SaleData</code>.
 * 
 */
public class SANormalizer implements Normalizer {
/*	private static final String COLUMN_HEADER_ID = "evidence_id";
	private static final String COLUMN_HEADER_DATE = "sale_date";
	private static final String COLUMN_HEADER_PRICE = "settlement_price";
	private static final String COLUMN_HEADER_BEDROOMS = "beds";
	private static final String COLUMN_HEADER_BATHROOMS = "baths";
	private static final String COLUMN_HEADER_CARPARKS = "parking";
	private static final String COLUMN_HEADER_ADDRESS = "full_street_address";
*/	
	public SaleData normalize(List<Object> record) 
		throws NormalizationException {
		try {
			SaleData saleData = new SaleData();
			if(record.get(0) != null)
				saleData.setSourceSaleID((String)record.get(0));
			
			if(record.get(1) != null) { 
				String tDate = SaleData.dateFormat.format((Date)record.get(1));
				saleData.setDate(tDate);
			}
			
			if(record.get(2) != null) {
				long lPrice = ((Long)record.get(2)).longValue();
				saleData.setPrice(lPrice);
			}
			
			if(record.size() == getFullCellProcessors().length) {
				if((record.size() >= 4) && (record.get(3) != null)) {
					int lBedrooms = ((Integer)record.get(3)).intValue();
					saleData.setBedrooms(lBedrooms);
				}
				
				if((record.size() >= 5) && (record.get(4) != null)) {
					int lBathrooms = ((Integer)record.get(4)).intValue();
					saleData.setBathrooms(lBathrooms);
				}
				
				if((record.size() >= 6) && (record.get(5) != null)) {
					int lCarparks = ((Integer)record.get(5)).intValue();
					saleData.setCarparks(lCarparks);
				}
				
				if(record.get(6) != null)
					saleData.setAddress((String)record.get(6));
			} else {
				if(record.get(3) != null)
					saleData.setAddress((String)record.get(3));
			}
			return saleData;
		} catch(ParseException e) {
			throw new NormalizationException(e);
		}
	}

	public CellProcessor[] getFullCellProcessors() {
		CellProcessor[] ret = new CellProcessor[] { 
			new NotNull(), //COLUMN_HEADER_ID
			new ParseDate("dd/MM/yyyy"), //COLUMN_HEADER_DATE
			new NotNull(new ParseLong()), //COLUMN_HEADER_PRICE
			new Optional(new ParseInt()), //COLUMN_HEADER_BEDROOMS
			new Optional(new ParseInt()), //COLUMN_HEADER_BATHROOMS
			new Optional(new ParseInt()), //COLUMN_HEADER_CARPARKS
			new NotNull() //COLUMN_HEADER_ADDRESS
		};		
		return ret;
	}

	public CellProcessor[] getPartialCellProcessors() {
		CellProcessor[] ret = new CellProcessor[] { 
			new NotNull(), //COLUMN_HEADER_ID
			new ParseDate("dd/MM/yyyy"), //COLUMN_HEADER_DATE
			new NotNull(new ParseLong()), //COLUMN_HEADER_PRICE
			new NotNull() //COLUMN_HEADER_ADDRESS
		};		
		return ret;
	}
}
