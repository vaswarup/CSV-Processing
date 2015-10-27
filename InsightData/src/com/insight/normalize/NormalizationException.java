package com.insight.normalize;

import com.insight.app.CoreEngineException;

/**
 * This captures all exceptions in the normalizers.
 * 
 */
public class NormalizationException extends CoreEngineException {
	private static final long serialVersionUID = 2L;

	public NormalizationException(String message) {
		super(message);
	}

	public NormalizationException(Exception e) {
		super(e);
	}
}
