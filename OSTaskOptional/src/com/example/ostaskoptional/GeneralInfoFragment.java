package com.example.ostaskoptional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Το Fragment στο οποίο εμφανίζονται γενικές πληροφορίες για το κινητό σε ένα ListView, όπως το
 * μέγεθος της εσωτερικής και της εξωτερικής μνήμης, η κατάσταση συνδεσιμότητας,
 * και ο χρόνος uptime, όπως επίσης μπορείς να ανοίξεις και τα Activity για τις
 * πληροφορίες συστήματος(SystemInfo) και τις πληροφορίες Μπαταρίας(BatteryInfo)
 * 
 * @author Θανάσης
 * @see BatteryInfo
 * @see SystemInfo
 * @see ListView
 */
public class GeneralInfoFragment extends Fragment {
	ListView listView;
	List<StringPair> titlesAndDescriptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		titlesAndDescriptions = getTitlesAndDescriptions();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_general_info, container,
				false);
		listView = (ListView) view.findViewById(android.R.id.list);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		listView.setAdapter(new MyGeneralInfoAdapter(getActivity(),
				titlesAndDescriptions));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					Intent openSystemInfo = new Intent(
							"com.example.ostaskoptional.SYSTEMINFO");
					getActivity().startActivity(openSystemInfo);
				} else if (position == 1) {
					Intent openBatteryInfo = new Intent(
							"com.example.ostaskoptional.BATTERYINFO");
					getActivity().startActivity(openBatteryInfo);
				}
			}
		});
	}

	/**
	 * 
	 * @return Γενικές πληροφορίες για την συσκευή μαζί με τον τίτλο τους
	 * @see StringPair
	 */
	public List<StringPair> getTitlesAndDescriptions() {
		List<StringPair> titlesAndDescriptions = new ArrayList<StringPair>();

		String sysInfoStr = getResources().getString(R.string.system_info);
		String sysInfoDescStr = getResources().getString(
				R.string.system_info_description);
		titlesAndDescriptions.add(new StringPair(sysInfoStr, sysInfoDescStr));

		String batteryInfoStr = getResources().getString(R.string.battery_info);
		String batteryInfoDescStr = getResources().getString(
				R.string.battery_info_description);
		titlesAndDescriptions.add(new StringPair(batteryInfoStr,
				batteryInfoDescStr));

		// Internal Storage Size
		File internalStorageFile = Environment.getDataDirectory();
		StatFs stat1 = new StatFs(internalStorageFile.getPath());
		long blockSizeInternal = stat1.getBlockSize();
		long blocksCountInternal = stat1.getBlockCount();
		String internalStorageSize = Formatter.formatFileSize(getActivity(),
				blocksCountInternal * blockSizeInternal);

		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.internal_storage), internalStorageSize));

		// External Storage Size
		File externalStorageFile = Environment.getExternalStorageDirectory();
		StatFs stat2 = new StatFs(externalStorageFile.getPath());
		long blockSizeExternal = stat2.getBlockSize();
		long blocksCountExternal = stat2.getBlockCount();
		String externalStorageSize = Formatter.formatFileSize(getActivity(),
				blocksCountExternal * blockSizeExternal);

		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.external_storage), externalStorageSize));

		// Connectivity State
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		String connectionStateString = getResources().getString(
				R.string.connection_state);
		String connectionTypeString = getResources().getString(
				R.string.connection_type);
		if (isConnected) {

			titlesAndDescriptions.add(new StringPair(connectionStateString,
					getResources().getString(R.string.connected)));

			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
				titlesAndDescriptions.add(new StringPair(connectionTypeString,
						"Wifi"));
			} else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
				titlesAndDescriptions.add(new StringPair(connectionTypeString,
						"3G"));
			} else {
				titlesAndDescriptions.add(new StringPair(connectionTypeString,
						getResources().getString(R.string.unknown)));
			}
		} else {
			titlesAndDescriptions.add(new StringPair(connectionStateString,
					getResources().getString(R.string.disconnected)));
		}

		// O xronos uptime (xoris ton xrono pou koimatai)
		long uptimeMil = SystemClock.uptimeMillis();

		long uptimeSec = uptimeMil / 1000;
		uptimeMil = uptimeMil % 1000;
		long uptimeMin = uptimeSec / 60;
		uptimeSec = uptimeSec % 60;
		long uptimeHours = uptimeMin / 60;
		uptimeMin = uptimeMin % 60;
		String uptime = Long.toString(uptimeHours) + ':'
				+ Long.toString(uptimeMin) + ':' + Long.toString(uptimeSec);

		// O xronos uptime (mazi me ton xrono pou koimatai)
		long elapsedMil = SystemClock.elapsedRealtime();

		long elapsedSec = elapsedMil / 1000;
		elapsedMil = elapsedMil % 1000;
		long elapsedMin = elapsedSec / 60;
		elapsedSec = elapsedSec % 60;
		long elapsedHours = elapsedMin / 60;
		elapsedMin = elapsedMin % 60;
		String elapsed = Long.toString(elapsedHours) + ':'
				+ Long.toString(elapsedMin) + ':' + Long.toString(elapsedSec);

		titlesAndDescriptions.add(new StringPair(getResources().getString(
				R.string.uptime), elapsed + "(" + uptime + ")"));

		return titlesAndDescriptions;
	}

	public class MyGeneralInfoAdapter extends BaseAdapter {
		private List<StringPair> titlesAndDescriptions;

		private LayoutInflater mInflater;

		public MyGeneralInfoAdapter(Context context, List<StringPair> results) {
			titlesAndDescriptions = results;
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return titlesAndDescriptions.size();
		}

		public Object getItem(int position) {
			return titlesAndDescriptions.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.general_info_view,
						null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.tvTitle);
				holder.description = (TextView) convertView
						.findViewById(R.id.tvDescription);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			StringPair titleAndDesciption = titlesAndDescriptions.get(position);
			
			//An einai sta prwta 2(dld sto System Info kai sto Battery Info, 8a emfanizontai me aspra grammata gia na fainetai oti patiountai
			if (position <= 1) {
				holder.title.setTextColor(getResources()
						.getColor(R.color.white));
			}
			holder.title.setText(titleAndDesciption.title);
			holder.description.setText(titleAndDesciption.description);
			return convertView;
		}

		private class ViewHolder {
			TextView title;
			TextView description;
		}
	}

}
