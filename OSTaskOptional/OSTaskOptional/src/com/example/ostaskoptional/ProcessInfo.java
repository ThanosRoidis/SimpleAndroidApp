package com.example.ostaskoptional;

import java.text.DecimalFormat;

/**
 * Κρατάει πληροφορίες για μια ενεργή διεργασία της συσκευής
 * @author Θανάσης
 *
 */
public class ProcessInfo {

	private int pid;
	
	private String name;
	
	private String memoryUsage;
	
	private String importance;
	
	/**
	 * 
	 * @param pid Το pid της διεργασίας
	 * @param name Το όνομα της διεργασίας 
	 * @param memoryUsage Η χρήση μνήμης της συσκευής
	 * @param importance Το importance της διεργασίας
	 */
	public ProcessInfo(int pid,String name,String memoryUsage,String importance){
		this.pid = pid;
		this.name = name;
		this.memoryUsage = memoryUsage;
		this.importance = importance;
	}
	
	public int getPID(){
		return pid;
	}
	
	public String getName(){
		return name;
	}
	
	public String getMemoryUsage(){
		return memoryUsage;
	}
	
	
	public String getImportance(){
		return importance;
	}
	

	
}
