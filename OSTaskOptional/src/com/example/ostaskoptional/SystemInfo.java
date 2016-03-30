package com.example.ostaskoptional;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Το Activity στο οποίο εμφανίζονται πληροφορίες του συστήματος, όπως η έκδοση
 * του Android και του πυρήνα, ο κατασκευαστής και το μοντέλο, το μέγεθος της
 * μνήμης Ram και το πόσο είναι διαθέσιμη, η ανάλυση της οθόνης και η πυκνότητα
 * της οθόνης
 * 
 * @author Θανάσης
 * 
 */
public class SystemInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_info_layout);

		ListView listview = (ListView) findViewById(R.id.listView4);
		listview.setAdapter(new TwoLineListViewAdapter(this,
				getTitlesAndDescriptions()));

	}

	/**
	 * 
	 * @return Όλα τα δεδομένα για το σύστημα, μαζί με τον τίτλο τους
	 * @see StringPair
	 */
	public List<StringPair> getTitlesAndDescriptions() {
		List<StringPair> titlesAndDescriptions = new ArrayList<StringPair>();

		// Android Version Stuff
		int apiVersion = Build.VERSION.SDK_INT;
		String release = Build.VERSION.RELEASE;
		String kernelVersion = System.getProperty("os.version");

		// Model Stuff
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;

		// Πληροφορίες για την μνήμη RAM απο το αρχείο /proc/meminfo
		String totalRamMBString = "Unknown";
		int totalRamMB = -1;
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
			String load = reader.readLine();
			String[] tokens = load.split(" ");
			int sizeKB = Integer.parseInt(tokens[tokens.length - 2]);
			totalRamMB = sizeKB / 1024;
			totalRamMBString = String.valueOf(totalRamMB) + " MB";
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		//Διαθέσιμη Μνήμη RAM
		String availableRamMBString = "Unknown";

		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.availMem / 1048576L;
		if (totalRamMB > 0 && availableMegs > 0) {
			int availableRamPercentage = (int) (100 * availableMegs / totalRamMB) ;
			availableRamMBString = String.valueOf((int) availableMegs) + " MB (" + availableRamPercentage + "%)";
		}
		
		//Διαστάσεις Οθόνης
		Display display = getWindowManager().getDefaultDisplay();
		Point screenDimensions = new Point();
		display.getSize(screenDimensions);
		int screenX = screenDimensions.x;
		int screenY = screenDimensions.y;

		//Πυκνότητα Οθόνης
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int screenDensity = metrics.densityDpi;

		
		
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.android_version), release));
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.kernel_version), kernelVersion));
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.manufacturer), manufacturer));
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.model), model));
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.total_ram), totalRamMBString));
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.available_ram), availableRamMBString));
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.screen_resolution), screenX + " x " + screenY));
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.screen_density), screenDensity + " dpi"));

		return titlesAndDescriptions;
	}

}
