package com.insight.normalize;

import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;

import com.insight.app.SaleData;

/**
 * This is a common interface for classes that normalize data and create a normalized record. The normalized record is subsequently used  
 * by the processors during record processing. Typically, one normalizer per data source is required.
 * <br><br>
 * A normalizer for a data source can be configured in the configuration file using the key which is a combination of the prefix 
 * 'normalizer.' and the csv file name. Eg. for a csv file name, abc.csv, the entry in the configuration file will be 
 * normalizer.abc.csv=xyz.normalizer.ABCNormalizer 
 * 
 */
public interface Normalizer {

	public SaleData normalize(List<Object> record)
		throws NormalizationException;
	
	public CellProcessor[] getFullCellProcessors();

	public CellProcessor[] getPartialCellProcessors();
}
