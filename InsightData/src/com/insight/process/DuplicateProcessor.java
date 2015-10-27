package com.insight.process;

import java.util.Map;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.insight.app.SaleData;

/**
 * This processor checks for duplicate records. It returns success if the record is unique; else it returns a failure. A record is 
 * considered a duplicate if sale address, sale date and sale price are same. Only a single instance of the duplicate record is allowed and
 * subsequent instances are discarded. 
 * <br</br>
 * This class tracks duplicates through a map with a composite key, of sale date and sale price and the value as a distinct list of hash 
 * values of sale address.
 * 
 */
public class DuplicateProcessor implements Processor {
	private static final Logger logger = Logger.getLogger("com.insight.process.DuplicateProcessor");
	private static final String DELIMITER = "!___!";
	private static final Map<String, SortedSet<String>> recordIndex = new HashMap<String, SortedSet<String>>();
	
	public boolean process(SaleData record) {
		String key = record.getDate() + DELIMITER + record.getPrice();
		String addressHash = "" + record.getAddress().hashCode();
		
		synchronized(recordIndex) {
			SortedSet<String> addressHashSet = recordIndex.get(key);
			if(addressHashSet == null) {
				addressHashSet = new TreeSet<String>();
				recordIndex.put(key, addressHashSet);
			}
			
			boolean isDuplicate = false;
			if(addressHashSet.contains(addressHash)) {
				isDuplicate = true;
			}
			
			if(isDuplicate == false) {
				addressHashSet.add(addressHash);
				return true;
			} else {
				logger.log(Level.WARNING, "!!!!! Duplicate record found - " + record.getSourceSaleID() + ", " +record.getDate() + ", " + record.getPrice() + ", " + record.getAddress());
				return false;
			}
		}
	}
}
