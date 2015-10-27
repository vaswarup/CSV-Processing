package com.insight.app;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

import com.insight.commands.MergeCommand;
import com.insight.commands.ModeCommand;
import com.insight.process.Processor;
import com.insight.process.ProcessorException;

/**
 * Executes the CoreEngine. This is a command based engine that takes its input from a configuration file. 
 * <br><br>
 * To execute a command on this engine, first an instance of the engine is created and then the command is executed using 
 * the <code>execute(String command)</code>. The list of valid commands are <code>merge</code> and <code>mode</code>.
 */
public class CoreEngine {
	private Properties config = new Properties();

	public CoreEngine(InputStream configFileFin)
		throws CoreEngineException {
		try {
			config.load(configFileFin);
		} catch(IOException e) {
			throw new CoreEngineException(e);
		}
	}
	
	public void execute(String command) 
		throws CoreEngineException {
		if((command == null) || (command.isEmpty())) {
			throw new CoreEngineException("No command to execute.");
		}
		
		try {
			//***** Load all processors
			loadProcessors();
			
			if(command.equalsIgnoreCase(Constants.COMMAND_MERGE)) {
				MergeCommand merge = new MergeCommand(config);
				merge.executeMerge();
			} else if(command.equalsIgnoreCase(Constants.COMMAND_MODE)) {
				ModeCommand mode = new ModeCommand(config);
				mode.executeMode();
			} else {
				throw new CoreEngineException("Invalid command - " + command);
			}
		} catch(Exception e) {
			throw new CoreEngineException(e);
		}
	}
	
	private void loadProcessors() 
		throws ProcessorException {
		int count = 0;
		while(true) {
			try {
				//***** Check the registry for the processor; if it does not exist create one & add to registry
				Processor processor = ObjectRegistry.getProcessor(""+count);
				if(processor == null) {
					String processorKey = ObjectRegistry.getProcessorKey(""+count);
					String processorClsName = config.getProperty(processorKey);
					if(processorClsName == null) return;
			
					Class<?> cls = Class.forName(processorClsName);
					processor = (Processor)cls.newInstance();
					ObjectRegistry.registerProcessor(""+count, processor);
				}
			} catch(Exception e) {
				throw new ProcessorException(e);
			}
			count++;
		}
	}
}
