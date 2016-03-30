package com.example.ostaskoptional;

/**
 * Μια απλή κλάση που κρατάει δύο Strings, έναν τιτλο και μία περιγραφή
 * @author Θανάσης
 *
 */
public class StringPair{
	
	public String title;
	public String description;
	
	/**
	 * 
	 * @param title Ο τίτλος
	 * @param description Η περιγραφή
	 */
	public StringPair(String title,String description){
		this.title = title;
		this.description = description;
	}
}
