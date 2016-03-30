package com.example.ostaskoptional;

import java.text.DecimalFormat;

/**
 * ������� ����������� ��� ��� ������ ��������� ��� ��������
 * @author �������
 *
 */
public class ProcessInfo {

	private int pid;
	
	private String name;
	
	private String memoryUsage;
	
	private String importance;
	
	/**
	 * 
	 * @param pid �� pid ��� ����������
	 * @param name �� ����� ��� ���������� 
	 * @param memoryUsage � ����� ������ ��� ��������
	 * @param importance �� importance ��� ����������
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
