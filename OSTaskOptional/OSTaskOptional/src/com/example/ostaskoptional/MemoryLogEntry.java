package com.example.ostaskoptional;


/**
 * ���������� ��� ���������� ��� ������
 * @author �������
 *
 */
public class MemoryLogEntry {

	private String freeInternalStorage;
	private String internalStorageSize;
	
	private String freeExternalStorage;
	private String externalStorageSize;
	
	private String date;

	/**
	 * 
	 * @param freeInternalStorage � ��������� ����� ���� ��������� �����
	 * @param internalStorageSize � ��������� ����� ���� ��������� �����
	 * @param freeExternalStorage � ��������� ����� ���� ��������� �����
	 * @param externalStorageSize � ��������� ����� ���� ��������� �����
	 * @param date � ���������� ��� ����� � ����������
	 */
	public MemoryLogEntry(String freeInternalStorage, String internalStorageSize,
			String freeExternalStorage, String externalStorageSize, String date) {
		this.freeInternalStorage = freeInternalStorage;
		this.internalStorageSize = internalStorageSize;
		this.freeExternalStorage = freeExternalStorage;
		this.externalStorageSize = externalStorageSize;
		this.date = date;
	}
	
	public String getFreeInternalStorage(){
		return freeInternalStorage;
	}
	
	public String getInternalStorageSize(){
		return internalStorageSize;
	}
	
	public String getFreeExternalStorage(){
		return freeExternalStorage;
	}
	
	public String getExternalStorageSize(){
		return externalStorageSize;
	}
	
	public String getDate(){
		return date;
	}
	
	
}
