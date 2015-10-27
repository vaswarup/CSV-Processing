package com.insight.app;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Holds a normalized record for processing. A normalized record is a normalized instance of a row from a CSV which is used during 
 * processing. The different fields of the normalized record are...source_sale_id, address, date, price, bedrooms, bathrooms and carparks.
 * The date field is stored in yyyy/mm/dd format. 
 * 
 * A normalized record is considered valid if and only if all processors executed on the normalized record return success. 
 * 
 */
public class SaleData {
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	public static final String COLUMN_HEADER_SOURCE_SALE_ID = "source_sale_id";
	public static final String COLUMN_HEADER_ADDRESS = "address";
	public static final String COLUMN_HEADER_DATE = "date";
	public static final String COLUMN_HEADER_PRICE = "price";
	public static final String COLUMN_HEADER_BEDROOMS = "bedrooms";
	public static final String COLUMN_HEADER_BATHROOMS = "bathrooms";
	public static final String COLUMN_HEADER_CARPARKS = "carparks";
	
	private String sourceSaleID;
	private String address;
	private long price;
	private String date;
	private int bedrooms;
	private int bathrooms;
	private int carparks;

	private Date internalDate;
	private boolean isValid = false;
	
	public String getSourceSaleID() {
		return sourceSaleID;
	}
	
	public void setSourceSaleID(String sourceSaleID) {
		this.sourceSaleID = sourceSaleID;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public long getPrice() {
		return price;
	}
	
	public void setPrice(long price) {
		this.price = price;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) 
		throws ParseException {
		internalDate = dateFormat.parse(date);
		this.date = date;
	}
	
	public int getBedrooms() {
		return bedrooms;
	}
	
	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}
	
	public int getBathrooms() {
		return bathrooms;
	}
	
	public void setBathrooms(int bathrooms) {
		this.bathrooms = bathrooms;
	}
	
	public int getCarparks() {
		return carparks;
	}
	
	public void setCarparks(int carparks) {
		this.carparks = carparks;
	}
	
	public int getYear() {
		if(internalDate == null) return -1;
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		return Integer.parseInt(yearFormat.format(internalDate));
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String toString() {
		StringBuilder strBuf = new StringBuilder();
		strBuf.append("[");
		strBuf.append(COLUMN_HEADER_SOURCE_SALE_ID + "=" + getSourceSaleID());
		strBuf.append(", " + COLUMN_HEADER_ADDRESS + "=" + getAddress());
		strBuf.append(", " + COLUMN_HEADER_PRICE + "=" + getPrice());
		strBuf.append(", " + COLUMN_HEADER_DATE + "=" + getDate());
		strBuf.append(", " + COLUMN_HEADER_BEDROOMS + "=" + getBedrooms());
		strBuf.append(", " + COLUMN_HEADER_BATHROOMS + "=" + getBathrooms());
		strBuf.append(", " + COLUMN_HEADER_CARPARKS + "=" + getCarparks());
		strBuf.append(", isValid=" + isValid());
		strBuf.append("]");
		return strBuf.toString();
	}
	
	public Map<String, Object> serialize(String headers[]) {
		Map<String, Object> ret = new HashMap<String, Object>();
		for(int i=0;i<headers.length;i++) {
			if(headers[i].equalsIgnoreCase(COLUMN_HEADER_SOURCE_SALE_ID))
				ret.put(COLUMN_HEADER_SOURCE_SALE_ID, getSourceSaleID());
			
			if(headers[i].equalsIgnoreCase(COLUMN_HEADER_ADDRESS))
				ret.put(COLUMN_HEADER_ADDRESS, getAddress());

			if(headers[i].equalsIgnoreCase(COLUMN_HEADER_PRICE))
				ret.put(COLUMN_HEADER_PRICE, getPrice());
			
			if(headers[i].equalsIgnoreCase(COLUMN_HEADER_DATE))
				ret.put(COLUMN_HEADER_DATE, getDate());
			
			if(headers[i].equalsIgnoreCase(COLUMN_HEADER_BEDROOMS))
				ret.put(COLUMN_HEADER_BEDROOMS, getBedrooms());
			
			if(headers[i].equalsIgnoreCase(COLUMN_HEADER_BATHROOMS))
				ret.put(COLUMN_HEADER_BATHROOMS, getBathrooms());
			
			if(headers[i].equalsIgnoreCase(COLUMN_HEADER_CARPARKS))
				ret.put(COLUMN_HEADER_CARPARKS, getCarparks());
		}
		return ret;
	}
	
	public static String[] getHeaders() {
		return new String[] {COLUMN_HEADER_SOURCE_SALE_ID, COLUMN_HEADER_ADDRESS, COLUMN_HEADER_DATE, COLUMN_HEADER_PRICE
		        			, COLUMN_HEADER_BEDROOMS, COLUMN_HEADER_BATHROOMS, COLUMN_HEADER_CARPARKS};
	}
	
	public static CellProcessor[] getCellProcessors() {
		CellProcessor[] ret = new CellProcessor[] { 
			new NotNull(), //COLUMN_HEADER_SOURCE_SALE_ID
			new NotNull(), //COLUMN_HEADER_ADDRESS
			new NotNull(), //COLUMN_HEADER_DATE
			new NotNull(new ParseLong()), //COLUMN_HEADER_PRICE
			new Optional(new ParseInt()), //COLUMN_HEADER_BEDROOMS
			new Optional(new ParseInt()), //COLUMN_HEADER_BATHROOMS
			new Optional(new ParseInt()) //COLUMN_HEADER_CARPARKS
		};		
		return ret;
	}
}
