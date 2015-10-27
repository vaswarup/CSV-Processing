package com.insight.app;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.insight.normalize.Normalizer;
import com.insight.process.Processor;

/**
 * Registry to hold normalizers and processors. 
 * 
 */
public class ObjectRegistry {
	private static final Map<String, Normalizer> normalizerRegistry = new HashMap<String, Normalizer>();
	private static final Map<String, Processor> processorRegistry = new HashMap<String, Processor>();

	private ObjectRegistry() {
	}

	public static String getNormalizerKey(String normalizerName) {
		return Constants.NORMALIZER_KEY_PREFIX + normalizerName;
	}
	
	public static void registerNormalizer(String normalizerName, Normalizer normalizer) {
		synchronized(normalizerRegistry) {
			normalizerRegistry.put(getNormalizerKey(normalizerName), normalizer);
		}
	}
	
	public static Normalizer unregisterNormalizer(String normalizerName) {
		synchronized(normalizerRegistry) {
			return normalizerRegistry.remove(getNormalizerKey(normalizerName));
		}
	}

	public static Normalizer getNormalizer(String normalizerName) {
		return normalizerRegistry.get(getNormalizerKey(normalizerName));
	}

	public static List<Normalizer> getNormalizers() {
		synchronized(normalizerRegistry) {
			List<Normalizer> ret = new ArrayList<Normalizer>();
			ret.addAll(normalizerRegistry.values());
			return ret;
		}
	}

	public static String getProcessorKey(String processorName) {
		return Constants.PROCESSOR_KEY_PREFIX + processorName;
	}
	
	public static void registerProcessor(String processorName, Processor processor) {
		synchronized(processorRegistry) {
			processorRegistry.put(getProcessorKey(processorName), processor);
		}
	}
	
	public static Processor unregisterProcessor(String processorName) {
		synchronized(processorRegistry) {
			return processorRegistry.remove(getProcessorKey(processorName));
		}
	}

	public static Processor getProcessor(String processorName) {
		return processorRegistry.get(getProcessorKey(processorName));
	}

	public static List<Processor> getProcessors() {
		synchronized(processorRegistry) {
			List<Processor> ret = new ArrayList<Processor>();
			ret.addAll(processorRegistry.values());
			return ret;
		}
	}
}
