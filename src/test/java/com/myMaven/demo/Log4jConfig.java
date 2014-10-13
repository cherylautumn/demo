package com.myMaven.demo;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

public class Log4jConfig {
	public Logger log = Logger.getLogger(Log4jConfig.class);
	public void info(String msg) {
		log.debug("Start count: "+msg);

    }
	public static void main(String[] args) throws FileNotFoundException {
		Log4jConfig cf = new Log4jConfig();
		cf.info("test");
	}
}
