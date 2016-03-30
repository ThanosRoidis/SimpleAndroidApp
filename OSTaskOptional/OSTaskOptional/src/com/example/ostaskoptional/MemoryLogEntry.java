package com.example.ostaskoptional;


/**
 * Αναπαριστά μια καταχώριση της Μνήμης
 * @author Θανάσης
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
	 * @param freeInternalStorage Ο ελεύθερος χώρος στην εσωτερική μνήμη
	 * @param internalStorageSize Ο συνολικός χώρος στην εσωτερική μνήμη
	 * @param freeExternalStorage ο ελεύθερος χώρος στην εξωτερική μνήμη
	 * @param externalStorageSize Ο συνολικός χώρος στην εξωτερική μνήμη
	 * @param date η ημερομηνία που έγινε η καταχώριση
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
