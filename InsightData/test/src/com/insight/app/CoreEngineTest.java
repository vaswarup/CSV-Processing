package com.insight.app;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import junit.framework.TestCase;

import com.insight.app.CoreEngine;

public class CoreEngineTest extends TestCase {
	private CoreEngine engine = null;
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		engine = null;
	}

/*	public void testNullExecute() {
		try {
			CoreEngine engine = new CoreEngine(null);
		} catch(CoreEngineException e) {
			assertEquals(1, 1);
			return;
		}
		assertEquals("Expected CoreEngineException", 1, 0);
	}
*/
	public void testMergeExecute() 
		throws CoreEngineException, FileNotFoundException {
		InputStream fin = new FileInputStream("./config/InsightData-MergeTest.cfg");
		engine = new CoreEngine(fin);
		engine.execute(Constants.COMMAND_MERGE);
	}

	public void testModeExecute() 
		throws CoreEngineException, FileNotFoundException {
		InputStream fin = new FileInputStream("./config/InsightData-ModeTest.cfg");
		engine = new CoreEngine(fin);
		engine.execute(Constants.COMMAND_MODE);
	}

	public void testInvalidCommandExecute() 
		throws CoreEngineException, FileNotFoundException {
		try {
			InputStream fin = new FileInputStream("./config/InsightData-MergeTest.cfg");
			engine = new CoreEngine(fin);
			engine.execute("ABC");
		} catch(CoreEngineException e) {
			assertEquals(1, 1);
			return;
		}
		assertEquals("Expected CoreEngineException", 1, 0);
	}

	public void testInvalidDataDirExecute() 
		throws CoreEngineException, FileNotFoundException {
		try {
			InputStream fin = new FileInputStream("./config/InsightData-DataDirTest.cfg");
			engine = new CoreEngine(fin);
			engine.execute(Constants.COMMAND_MERGE);
		} catch(CoreEngineException e) {
			assertEquals(1, 1);
			return;
		}
		assertEquals("Expected CoreEngineException", 1, 0);
	}

	public void testInvalidMergeCSVExecute() 
		throws CoreEngineException, FileNotFoundException {
		try {
			InputStream fin = new FileInputStream("./config/InsightData-MergeCSVTest.cfg");
			engine = new CoreEngine(fin);
			engine.execute(Constants.COMMAND_MERGE);
		} catch(CoreEngineException e) {
			assertEquals(1, 1);
			return;
		}
		assertEquals("Expected FileNotException", 1, 0);
	}

	public void testInvalidModeCSVExecute() 
		throws CoreEngineException, FileNotFoundException {
		try {
			InputStream fin = new FileInputStream("./config/InsightData-ModeCSVTest.cfg");
			engine = new CoreEngine(fin);
			engine.execute(Constants.COMMAND_MODE);
		} catch(CoreEngineException e) {
			assertEquals(1, 1);
			return;
		} catch(FileNotFoundException ex) {
			assertEquals(1, 1);
			return;
		}
		assertEquals("Expected CoreEngineException", 1, 0);
	}

/*	public void testInvalidNormalizerExecute() 
		throws CoreEngineException, FileNotFoundException {
		try {
			InputStream fin = new FileInputStream("./config/InsightData-NormalizerTest.cfg");
			engine = new CoreEngine(fin);
			engine.execute(Constants.COMMAND_MERGE);
		} catch(CoreEngineException e) {
			assertEquals(1, 1);
			return;
		}
		assertEquals("Expected CoreEngineException", 1, 0);
	}
*/
/*	public void testInvalidProcessorExecute() 
		throws CoreEngineException, FileNotFoundException {
		try {
			InputStream fin = new FileInputStream("./config/InsightData-ProcessorTest.cfg");
			engine = new CoreEngine(fin);
			engine.execute(Constants.COMMAND_MERGE);
		} catch(CoreEngineException e) {
			assertEquals(1, 1);
			return;
		}
		assertEquals("Expected CoreEngineException", 1, 0);
	}
*/
}
