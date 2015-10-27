package com.insight.commands;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.Iterator;
import java.util.Properties;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.insight.app.SaleData;
import com.insight.app.Constants;
import com.insight.app.CoreEngineException;

/**
 * Calculates the mode of bedrooms by year. This class calculates the mode in three phases, viz., 
 * <ul>
 * 	<li>parse normalized data from the CSV file and populates the record index for mode calculation,</li>
 * 	<li>calculate mode using the record index, </li> 
 *  <li>save bedrooms mode by year into a CSV data file</li> 
 * </ul>
 * 
 * The record index uses a map with the sale year as the key and number of houses per number of bedrooms as the value. To calculate the mode 
 * of bedrooms by year, the number of bedrooms with the maximum number of houses is selected.
 * 
 * The normalized CSV data file that needs to be used for mode calculation can be configured in the configuration file using the key 'merge.csv'. 
 * Eg. if the normalized CSV data file is /usr/xyz/csv/final.csv the entry in the configuration file will be merge.csv=/usr/xyz/csv/final.csv
 * 
 * The CSV data file that will have the bedrooms mode by year can be configured in the configuration file using the key 'mode.csv'.  Eg. if 
 * the CSV data file is /usr/xyz/csv/final_mode.csv the entry in the configuration file will be mode.csv=/usr/xyz/csv/final_mode.csv
 * 
 */
public class ModeCommand {
	private static final Logger logger = Logger.getLogger("com.insight.commands.ModeCommand");
	private static final String COLUMN_HEADER_YEAR = "year";
	private static final String COLUMN_HEADER_BEDROOMS_MODE = "bedrooms_mode";
	private static final Map<String, Map<String, String>> recordEntryIndex = new HashMap<String, Map<String, String>>();
	private static final List<Map<String, Object>> modeEntryList = new ArrayList<Map<String, Object>>();

	private Properties config;
	
	public ModeCommand(Properties config) {
		this.config = config;
	}
	
	public void executeMode() 
		throws IOException, CoreEngineException {
		//***** Get the input file
		String mergeCSVPath = config.getProperty(Constants.MERGE_CSV_FILE);
		if((mergeCSVPath == null) || (mergeCSVPath.isEmpty())) {
			throw new CoreEngineException("No merge CSV file");
		}
		File dataFile = new File(mergeCSVPath);
		if(dataFile.exists() == false) {
			throw new CoreEngineException("Invalid merge CSV file - " + mergeCSVPath);
		}
		
		//***** Get the output file
		String modeCSVPath = config.getProperty(Constants.MODE_CSV_FILE);
		if((modeCSVPath == null) || (modeCSVPath.isEmpty())) {
			throw new CoreEngineException("No mode CSV file path");
		}
		
		//***** Calculate mode
		ICsvMapReader csvReader = null;
		ICsvMapWriter csvWriter = null;
		try {
			logger.log(Level.INFO, "========== Calulating mode using data file - " + dataFile.getAbsolutePath());
	
			//***** Parse the data file 
            csvReader = new CsvMapReader(new FileReader(dataFile), CsvPreference.STANDARD_PREFERENCE);
            String readHeaders[] = csvReader.getHeader(true);
            CellProcessor csvReadProcessors[] = getReadCellProcessors();
            
            //***** Iterate over each record, process each record & write to CSV
            Map<String, Object> record = null;
            while( (record = csvReader.read(readHeaders, csvReadProcessors)) != null ) {
            	populateRecordEntries(record);
            }	                
        	logger.log(Level.INFO, recordEntryIndex.toString());
        	calculateBedroomModePerYear();
        	logger.log(Level.INFO, modeEntryList.toString());
			
        	//***** Write records once the mode is calculated
			csvWriter = new CsvMapWriter(new FileWriter(modeCSVPath), CsvPreference.STANDARD_PREFERENCE);
            String writeHeaders[] = getHeaders();
            csvWriter.writeHeader(writeHeaders);
            CellProcessor[] csvWriteProcessors = getWriteCellProcessors();
            
            for(int i=0;i<modeEntryList.size();i++) {
            	Map<String, Object> modeEntry = (Map<String, Object>)modeEntryList.get(i);
            	csvWriter.write(modeEntry, writeHeaders, csvWriteProcessors);
            }

			logger.log(Level.INFO, "========== Successfully caculated mode using data file - " + dataFile.getAbsolutePath());
		} catch(Exception e) {
			logger.log(Level.SEVERE, "========== Failed caculating mode using  data file - " + dataFile.getAbsolutePath(), e);
			throw new CoreEngineException(e);
		} finally {
			if(csvReader != null)
				csvReader.close();
			if(csvWriter != null)
				csvWriter.close();
		}
	}

	private int getBedroomMode(Map<String, String> housesPerBedroomMap) {
		Set<Map.Entry<String, String>> bedroomEntrySet = housesPerBedroomMap.entrySet();
		Iterator<Map.Entry<String, String>> bedroomIter = bedroomEntrySet.iterator();
		int maxHouses = -1;
		String noOfBedroomsMax = null;
		while(bedroomIter.hasNext()) {
			Map.Entry<?, ?> bedroomEntry = (Map.Entry<?, ?>)bedroomIter.next();
			String housesPerBedroomStr = (String)bedroomEntry.getValue();
			int housesCount = Integer.parseInt(housesPerBedroomStr);
			if(housesCount > maxHouses) {
				maxHouses = housesCount;
				noOfBedroomsMax = (String)bedroomEntry.getKey();
			}
		}
		return Integer.parseInt(noOfBedroomsMax);
	}
	
	private void calculateBedroomModePerYear() {
		Set<Map.Entry<String, Map<String, String>>> yearEntrySet = recordEntryIndex.entrySet();
		Iterator<Map.Entry<String, Map<String, String>>> yearIter = yearEntrySet.iterator();
		while(yearIter.hasNext()) {
			Map.Entry yearEntry = (Map.Entry)yearIter.next();
			Map<String, String> housesPerBedroomMap = (Map<String, String>)yearEntry.getValue();
			int bedroomMode = getBedroomMode(housesPerBedroomMap);
			
			Map<String, Object> modeEntry = new HashMap<String, Object>();
			modeEntry.put(COLUMN_HEADER_YEAR, yearEntry.getKey());
			modeEntry.put(COLUMN_HEADER_BEDROOMS_MODE, ""+bedroomMode);
			
			modeEntryList.add(modeEntry);
		}
	}
	
	private void populateRecordEntries(Map<String, Object> record) {
		Date saleDate = (Date)record.get(SaleData.COLUMN_HEADER_DATE);
		int bedroomsCount = ((Integer)record.get(SaleData.COLUMN_HEADER_BEDROOMS)).intValue();

		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		String saleYear = yearFormat.format(saleDate);

		Map<String, String> housesPerBedroomMap = recordEntryIndex.get(saleYear);
		if(housesPerBedroomMap == null) {
			housesPerBedroomMap = new HashMap<String, String>();
			recordEntryIndex.put(saleYear, housesPerBedroomMap);
		} 
		
		String housesCountStr = housesPerBedroomMap.get((""+bedroomsCount));
		if(housesCountStr == null) {
			housesCountStr = "1";
			housesPerBedroomMap.put((""+bedroomsCount), housesCountStr);
		} else {
			int housesCount = Integer.parseInt(housesCountStr);
			housesCount++;
			housesPerBedroomMap.put((""+bedroomsCount), (""+housesCount));
		}
	}
	
	private static String[] getHeaders() {
		return new String[] {COLUMN_HEADER_YEAR, COLUMN_HEADER_BEDROOMS_MODE};
	}
	
	private static CellProcessor[] getWriteCellProcessors() {
		CellProcessor[] ret = new CellProcessor[] { 
			new NotNull(new ParseInt()), //COLUMN_HEADER_YEAR
			new NotNull(new ParseInt()) //COLUMN_HEADER_BEDROOMS_MODE
		};		
		return ret;
	}

	private static CellProcessor[] getReadCellProcessors() {
		CellProcessor[] ret = new CellProcessor[] { 
			null, //COLUMN_HEADER_SOURCE_SALE_ID
			null, //COLUMN_HEADER_ADDRESS
			new ParseDate("yyyy/MM/dd"), //COLUMN_HEADER_DATE
			null, //COLUMN_HEADER_PRICE
			new Optional(new ParseInt()), //COLUMN_HEADER_BEDROOMS
			null, //COLUMN_HEADER_BATHROOMS
			null //COLUMN_HEADER_CARPARKS
		};		
		return ret;
	}
}
