package com.insight.process;

import com.insight.app.CoreEngineException;

/**
 * This captures all exceptions in the processors.
 * 
 */
public class ProcessorException extends CoreEngineException {
	private static final long serialVersionUID = 3L;

	public ProcessorException(String message) {
		super(message);
	}

	public ProcessorException(Exception e) {
		super(e);
	}
}
