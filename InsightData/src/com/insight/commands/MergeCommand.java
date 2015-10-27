package com.insight.commands;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.insight.app.ObjectRegistry;
import com.insight.app.SaleData;
import com.insight.app.Constants;
import com.insight.app.CoreEngineException;
import com.insight.normalize.Normalizer;
import com.insight.normalize.NormalizationException;
import com.insight.process.Processor;

/**
 * Merges all CSV data files of a given directory into a single normalized CSV data file after processing. This class merges CSV data 
 * in three phases, viz., 
 * <ul>
 * 	<li>parse data from each CSV file,</li>
 * 	<li>normalize each record and process each record completely,</li>
 *  <li>merge the records into a single CSV data file</li> 
 * </ul>
 * 
 * In case of an error in any phase, the error is logged and it moves to the subsequent data file or record. A record is merged only when 
 * it is considered valid. 
 * 
 * The various normalizers and processors can be configured in the configuration file using the normalizer and processor keys described in 
 * <code>com.insight.normlaize.Normalizer</code> and <code>com.insight.normlaize.Processor</code> respectively.
 *  
 * The directory that has the CSV data files can be configured in the configuration file using the key 'data.dir'. Eg. if the CSV data files are stored in 
 * /usr/xyz/data the entry in the configuration file will be data.dir=/usr/xyz/data
 * 
 * The normalized CSV data can be configured in the configuration file using the key 'merge.csv'. Eg. if the normalized CSV data file is 
 * /usr/xyz/csv/final.csv the entry in the configuration file will be merge.csv=/usr/xyz/csv/final.csv
 * 
 */
public class MergeCommand {
	private static final Logger logger = Logger.getLogger("com.insight.commands.MergeCommand");
	private Properties config;
	
	public MergeCommand(Properties config) {
		this.config = config;
	}
	
	public void executeMerge() 
		throws IOException, CoreEngineException {
		ICsvMapWriter csvWriter = null;
		try {
			//***** Get the list of all files in the given folder
			File dataFiles[] = getDataFiles();

			//***** Write records to merge file
			String mergeCSVPath = config.getProperty(Constants.MERGE_CSV_FILE);
			if((mergeCSVPath == null) || (mergeCSVPath.isEmpty())) {
				throw new CoreEngineException("No merge CSV file");
			}
			csvWriter = new CsvMapWriter(new FileWriter(mergeCSVPath), CsvPreference.STANDARD_PREFERENCE);
            String writeHeaders[] = SaleData.getHeaders();
            csvWriter.writeHeader(writeHeaders);
            CellProcessor[] csvWriteProcessors = SaleData.getCellProcessors();
			
			//***** Process each file
			for(int i=0;i<dataFiles.length;i++) {
				File dataFile = dataFiles[i];
				ICsvListReader csvReader = null;
				try {
					logger.log(Level.INFO, "========== Merging data file - " + dataFile.getAbsolutePath());
					//***** Load the normalizer
					Normalizer normalizer = loadNormalizer(dataFile);
					
					//***** Parse the data file 
	                csvReader = new CsvListReader(new FileReader(dataFile), CsvPreference.STANDARD_PREFERENCE);
	                csvReader.getHeader(true);
	                
	    			//***** Iterate over each record, process each record & write to CSV
	                List<Object> record = null;
	                while(csvReader.read() != null) {
		                CellProcessor csvReadFullProcessors[] = normalizer.getFullCellProcessors();
		                CellProcessor csvReadPartialProcessors[] = normalizer.getPartialCellProcessors();
		                
		                if(csvReader.length() == csvReadFullProcessors.length) {
		                	record = csvReader.executeProcessors(csvReadFullProcessors);
		                	
		                } else if(csvReader.length() == csvReadPartialProcessors.length) {
		                	record = csvReader.executeProcessors(csvReadPartialProcessors);
		                } else {
		                	logger.log(Level.WARNING, "!!!!! Insufficient number of columns in row ");
		                	continue;
		                }
						SaleData saleData = processRecord(record, normalizer);	
						if((saleData == null) || (saleData.isValid() == false)) continue;
						csvWriter.write(saleData.serialize(writeHeaders), writeHeaders, csvWriteProcessors);
	                }	                

					logger.log(Level.INFO, "========== Successfully merged data file - " + dataFile.getAbsolutePath());
				} catch(Exception e) {
					logger.log(Level.SEVERE, "========== Failed merging data file - " + dataFile.getAbsolutePath(), e);
				} finally {
					if(csvReader != null)
						csvReader.close();
				}
			}
		} catch(Exception e) {
			throw new CoreEngineException(e);
		} finally {
			if(csvWriter != null)
				csvWriter.close();
		}
	}

	private File[] getDataFiles() 
		throws CoreEngineException {
		String dataDir = config.getProperty(Constants.DATA_DIR);
		File f = new File(dataDir);
		if(!f.exists()) {
			throw new CoreEngineException("Invalid data directory - " + dataDir);
		}
			
		return f.listFiles();
	}

	private Normalizer loadNormalizer(File dataFile) 
		throws NormalizationException {
		Normalizer normalizer = null;
		try {
			//***** Check the registry for the normalizer; if it does not exist create one & add to registry
			normalizer = ObjectRegistry.getNormalizer(dataFile.getName());
			if(normalizer == null) {
				String normalizerKey = ObjectRegistry.getNormalizerKey(dataFile.getName()) ;
				String normalizerClsName = config.getProperty(normalizerKey);
				if(normalizerClsName == null) {
					throw new NormalizationException("Normalizer class not defined for " + dataFile.getName());
				};
				
				Class<?> cls = Class.forName(normalizerClsName);
				normalizer = (Normalizer)cls.newInstance();
				ObjectRegistry.registerNormalizer(dataFile.getName(), normalizer);
			}
		} catch(Exception e) {
			throw new NormalizationException(e);
		}
		return normalizer;
	}

	private SaleData processRecord(List<Object> record, Normalizer normalizer)
		throws CoreEngineException {
		SaleData saleData = null;
		try {
			//***** Normalize the record & convert to a data object
			saleData = normalizer.normalize(record);
			
			//***** Process the data object
			logger.log(Level.INFO, "---------- Processing record - " + saleData.getSourceSaleID());
			List<Processor> processors = ObjectRegistry.getProcessors();
			for(int i=0;i<processors.size();i++) {
				Processor processor = processors.get(i);
				boolean processRecordFurther = processor.process(saleData);
				saleData.setValid(processRecordFurther);
				if(processRecordFurther == false) break;
			}
			logger.log(Level.INFO, "---------- Successfully processed record - " + saleData.getSourceSaleID());
		} catch(Exception e) {
			logger.log(Level.SEVERE, "---------- Failed to process record", e);
		}
		return saleData;
	}
}
