package com.insight.app.test;

import java.text.ParseException;

import junit.framework.TestCase;

import com.insight.app.SaleData;

public class SaleDataTest extends TestCase {
	private SaleData saleData;
	
	protected void setUp() throws Exception {
		super.setUp();
		saleData = new SaleData();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		saleData = null;
	}

	public void testSimpleGetSourceSaleID() {
		saleData.setSourceSaleID("1234567890abcDEFghi");
		assertEquals("1234567890abcDEFghi", saleData.getSourceSaleID());
	}

	public void testNullGetSourceSaleID() {
		saleData.setSourceSaleID(null);
		assertEquals(null, saleData.getSourceSaleID());
	}

	public void testEmptySimpleGetSourceSaleID() {
		saleData.setSourceSaleID("");
		assertEquals("", saleData.getSourceSaleID());
	}

	public void testSimpleGetAddress() {
		saleData.setAddress("123 abc def street xyz");
		assertEquals("123 abc def street xyz", saleData.getAddress());
	}

	public void testNullGetAddress() {
		saleData.setAddress(null);
		assertEquals(null, saleData.getAddress());
	}

	public void testEmptyGetAddress() {
		saleData.setAddress("");
		assertEquals("", saleData.getAddress());
	}

	public void testSimpleGetPrice() {
		saleData.setPrice(123124);
		assertEquals(123124, saleData.getPrice());
	}

	public void testNegativeGetPrice() {
		saleData.setPrice(-123124);
		assertEquals(-123124, saleData.getPrice());
	}

	public void testMinGetPrice() {
		saleData.setPrice(Long.MIN_VALUE);
		assertEquals(Long.MIN_VALUE, saleData.getPrice());
	}

	public void testMaxGetPrice() {
		saleData.setPrice(Long.MAX_VALUE);
		assertEquals(Long.MAX_VALUE, saleData.getPrice());
	}

	public void testValidFormatGetDate() 
		throws ParseException {
		saleData.setDate("2015/10/31");
		assertEquals("2015/10/31", saleData.getDate());
		assertEquals(2015, saleData.getYear());
	}

	public void testInvalidFormatGetDate() {
		try {
			saleData.setDate("31-10-15");
		} catch(ParseException e) {
			assertEquals(1,1);
			return;
		}
		assertEquals("Expected ParseException", 1, 0);
}

	public void testEmptyFormatGetDate() {
		try {
			saleData.setDate("");
		} catch(ParseException e) {
			assertEquals(1,1);
			return;
		}
		assertEquals("Expected ParseException", 1, 0);
	}

/*	public void testNullFormatGetDate() {
		SaleData saleData = new SaleData();
		try {
			saleData.setDate(null);
		} catch(ParseException e) {
			assertEquals(1,1);
			return;
		}
		assertEquals("Expected ParseException", null, 0);
	}*/

	public void testSimpleGetBedrooms() {
		saleData.setBedrooms(1231);
		assertEquals(1231, saleData.getBedrooms());
	}

	public void testNegativeGetBedrooms() {
		saleData.setBedrooms(-1231);
		assertEquals(-1231, saleData.getBedrooms());
	}

	public void testMinGetBedrooms() {
		saleData.setBedrooms(Integer.MIN_VALUE);
		assertEquals(Integer.MIN_VALUE, saleData.getBedrooms());
	}

	public void testMaxGetBedrooms() {
		saleData.setBedrooms(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, saleData.getBedrooms());
	}

	public void testSimpleGetBathrooms() {
		saleData.setBathrooms(1231);
		assertEquals(1231, saleData.getBathrooms());
	}

	public void testNegativeGetBathrooms() {
		saleData.setBathrooms(-1231);
		assertEquals(-1231, saleData.getBathrooms());
	}

	public void testMinGetBathrooms() {
		saleData.setBathrooms(Integer.MIN_VALUE);
		assertEquals(Integer.MIN_VALUE, saleData.getBathrooms());
	}

	public void testMaxGetBathrooms() {
		saleData.setBathrooms(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, saleData.getBathrooms());
	}


	public void testSimpleGetCarparks() {
		saleData.setCarparks(1231);
		assertEquals(1231, saleData.getCarparks());
	}

	public void testNegativeGetCarparks() {
		saleData.setCarparks(-1231);
		assertEquals(-1231, saleData.getCarparks());
	}

	public void testMinGetCarparks() {
		saleData.setCarparks(Integer.MIN_VALUE);
		assertEquals(Integer.MIN_VALUE, saleData.getCarparks());
	}

	public void testMaxGetCarparks() {
		saleData.setCarparks(Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, saleData.getCarparks());
	}

	public void testGetHeaders() {
		String headers[] = SaleData.getHeaders();
		assertEquals(7, headers.length);
	}

	public void testGetCellProcessors() {
		assertEquals(7, SaleData.getCellProcessors().length);
	}

}
