package com.example.ostaskoptional;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Το Activity στο οποίο εμφανίζονται πληροφορίες σχετικές με την μπαταρία, όπως
 * το ποσοστό διαθέσιμης μπαταρίας, την κατάσταση της(αν και πως φορτίζει), την
 * ζωή της, την τεχνολογία, την θερμοκρασία και την τάση μέσα σε ένα ListView
 * 
 * @author Θανάσης
 * @see ListView
 */
public class BatteryInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_info_layout);
		ListView listView = (ListView) findViewById(R.id.listView5);
		listView.setAdapter(new TwoLineListViewAdapter(this,
				getTitlesAndDescriptions()));
	}

	
	
	/**
	 * 
	 * @return Όλα τα δεδομένα για την μπαταρία, μαζί με τον τίτλο τους
	 * @see StringPair
	 */
	public List<StringPair> getTitlesAndDescriptions() {
		List<StringPair> titlesAndDescriptions = new ArrayList<StringPair>();

		// Get battery status
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = registerReceiver(null, ifilter);

		// Battery charge level
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = level / (float) scale;
		int batterPct100 = (int) (100 * batteryPct);

		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.battery_level), Integer.toString(batterPct100) + "%"));

		
		// Are we charging / charged?
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING)
				|| (status == BatteryManager.BATTERY_STATUS_FULL);

		
		// How are we charging?
		int chargePlug = batteryStatus.getIntExtra(
				BatteryManager.EXTRA_PLUGGED, -1);
		boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
		boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

		String chargingStateString = getResources().getString(
				R.string.charging_state);
		if (isCharging) {
			if (usbCharge) {
				titlesAndDescriptions.add(new StringPair(chargingStateString,
						getResources().getString(R.string.charging_usb)));
			} else if (acCharge) {
				titlesAndDescriptions.add(new StringPair(chargingStateString,
						getResources().getString(R.string.charging_ac)));
			} else {
				titlesAndDescriptions.add(new StringPair(chargingStateString,
						getResources().getString(R.string.charging_unknown)));
			}
		} else {
			titlesAndDescriptions.add(new StringPair(chargingStateString,
					getResources().getString(R.string.charging_not)));
		}

		
		// Battery Health
		int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH,
				BatteryManager.BATTERY_HEALTH_UNKNOWN);
		String healthString;
		switch (health) {
		case BatteryManager.BATTERY_HEALTH_DEAD:
			healthString = getResources().getString(R.string.battery_dead);
			break;
		case BatteryManager.BATTERY_HEALTH_GOOD:
			healthString = getResources().getString(R.string.battery_good);
			break;
		case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
			healthString = getResources().getString(
					R.string.battery_overvoltage);
			break;
		case BatteryManager.BATTERY_HEALTH_OVERHEAT:
			healthString = getResources().getString(R.string.battery_overheat);
			break;
		case BatteryManager.BATTERY_HEALTH_UNKNOWN:
			healthString = getResources().getString(R.string.unknown);
			break;
		case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
			healthString = getResources().getString(
					R.string.battery_unspecified_failure);
			break;
		default:
			healthString = getResources().getString(R.string.unknown);
			break;
		}

		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.battery_health), healthString));

		
		
		// Battery Technology
		String technology = batteryStatus
				.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.battery_technology), technology));

		
		
		// Battery Temperature
		int temperature = batteryStatus.getIntExtra(
				BatteryManager.EXTRA_TEMPERATURE, -1);
		String temperatureString;
		
		
		// Ελέγχει αν είναι σε δέκατα Celsius ή οχι
		if (temperature > 50) {
			// Αν είναι πολύ μεγάλη σημαίνει οτι τα μετράει σε δέκατα
			temperatureString = Float.toString(temperature / 10.0f);
		} else {
			temperatureString = Integer.toString(temperature);
		}
		// \u2103 einai to sima ton Ceslius
		temperatureString += "\u2103";
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.battery_temperature), temperatureString));

		
		// Battery voltage
		int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE,
				-1);
		String voltageString;
		// Ελέγχει αν είναι σε mV ή V
		if (voltage < 50) {
			voltageString = Integer.toString(voltage);
		} else {
			// Αν είναι πολυ μεγάλο σημαίνει οτι τα μετράει σε mV
			voltageString = Float.toString(voltage / 1000.0f);
		}
		voltageString += "V";
		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.battery_voltage), voltageString));

		
		return titlesAndDescriptions;
	}

}
