package com.insight.app;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

/**
 * Command line interface to abstract the actual engine.
 * 
 */
public class CoreEngineCLI {

	/**
	 * Executes the CoreEngine through a command line. Takes the path of the configuration file as a command 
	 * line argument. 
	 * 
	 * @param args - path of the configuration file and command to execute (merge or mode)
	 */
	public static void main(String[] args) {
		if(args.length < 2) {
			printUsage();
			return;
		}
		
		try {
			File configFile = new File(args[0]);
			if(!configFile.exists()) {
				throw new Exception("Configuration file does not exist - " + args[0]);
			}

			InputStream configFileFin= new FileInputStream(configFile);
			CoreEngine app = new CoreEngine(configFileFin);
			app.execute(args[1]);
		} catch(Exception e) {
			System.out.println("ERROR: Failure. Please see the stack trace below for details.");
			System.out.println();
			e.printStackTrace();
		}
	}
	
	private static void printUsage() {
		String str = "USAGE: \n\tcom.insight.app.CoreEngineCLI <configuration file path> <command to execute>";
		System.out.println(str);
	}

}
