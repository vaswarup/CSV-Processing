package com.insight.process;

import com.insight.app.SaleData;

/**
 * This is a common interface for classes that process a record. During the processing of a record a processor returns success or failure 
 * to mark the processing status. All processors are executed on each record, until a failure status is received during processing. Upon 
 * receiving a failure status, the record being processed is discarded and the subsequent record is picked for processing. Upon 
 * receiving a success status, further processing of the record is continued for the remaining processors.
 * <br><br>
 * A processor can be configured in the configuration file using the key which is a combination of the prefix 
 * 'processor.' and the sequence of processor during processing. Eg. for a processor that needs to be executed third, the entry in the 
 * configuration file will be processor.2=xyz.processor.ThirdProcessor
 * <br>
 * Please note the processor sequence starts at '0' and continues further, '1', '2', '3' and so on. 
 * 
 */
public interface Processor {

	public boolean process(SaleData record)
		throws ProcessorException;
}
