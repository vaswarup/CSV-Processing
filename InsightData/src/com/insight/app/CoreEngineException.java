package com.insight.app;

/**
 * This captures all exceptions in the core engine.
 * 
 */
public class CoreEngineException extends Exception {
	private static final long serialVersionUID = 1L;

	public CoreEngineException(String message) {
		super(message);
	}

	public CoreEngineException(Exception e) {
		super(e);
	}
}
