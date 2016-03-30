package com.example.ostaskoptional;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.ContactsContract.DeletedContacts;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * Είναι το Fragment στο οποίο φαίνονται όλες οι καταχωρίσεις για την κατάσταση
 * της μνήμης στην βάση δεδομένων αε ένα ListView
 * 
 * @author Θανάσης
 * @see MemoryLog
 * @see MemoryLogEntry
 */
public class MemoryLogFragment extends ListFragment {

	static MemoryLog memoryLog;
	ListView listView;
	static MyMemoryLogAdapter memoryLogAdapter;
	static ArrayList<MemoryLogEntry> entries;

	String internalStorageSize;
	String freeInternalStorage;

	String externalStorageSize;
	String freeExternalStorage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStorageVariables();
		memoryLog = new MemoryLog(getActivity());
		saveMemoryState();
		entries = getAllEntries();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_memory_log, container,
				false);
		listView = (ListView) view.findViewById(android.R.id.list);

		// Sets the top textviews
		TextView intStorage = (TextView) view
				.findViewById(R.id.tvIntStorageSize);
		String internalStorageString = getResources().getString(
				R.string.internal_storage);
		intStorage.setText(internalStorageString + "\n(" + internalStorageSize
				+ ')');

		TextView extStorage = (TextView) view
				.findViewById(R.id.tvExtStorageSize);
		String externalStorageString = getResources().getString(
				R.string.external_storage);
		extStorage.setText(externalStorageString + "\n(" + externalStorageSize
				+ ')');

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		memoryLogAdapter = new MyMemoryLogAdapter(getActivity(), entries);
		listView.setAdapter(memoryLogAdapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				DialogFragment newFragment = AlertDialogFragment.newInstance(
						position, entries.get(position).getDate());
				newFragment.show(getFragmentManager(), "dialog");
				return true;
			}

		});

	}

	/**
	 * Βρίσκει και θέτει στις αντίστοιχες μεταβλητές το συνολικό μέγεθος της
	 * εξωτερικής και της εσωτερικής μνήμης
	 */
	private void setStorageVariables() {

		File internalStorageFile = Environment.getDataDirectory();
		StatFs stat1 = new StatFs(internalStorageFile.getPath());
		long blockSizeInternal = stat1.getBlockSize();
		long blocksCountInternal = stat1.getBlockCount();
		internalStorageSize = Formatter.formatFileSize(getActivity(),
				blocksCountInternal * blockSizeInternal);
		long availableBlocksInternal = stat1.getAvailableBlocks();
		freeInternalStorage = Formatter.formatFileSize(getActivity(),
				availableBlocksInternal * blockSizeInternal);

		// External Storage Stuff
		File externalStorageFile = Environment.getExternalStorageDirectory();
		StatFs stat2 = new StatFs(externalStorageFile.getPath());
		long blockSizeExternal = stat2.getBlockSize();
		long blocksCountExternal = stat2.getBlockCount();
		externalStorageSize = Formatter.formatFileSize(getActivity(),
				blocksCountExternal * blockSizeExternal);
		long availableBlocksExternal = stat2.getAvailableBlocks();
		freeExternalStorage = Formatter.formatFileSize(getActivity(),
				availableBlocksExternal * blockSizeExternal);

	}

	/**
	 * 
	 * @return Όλες οι καταχωρίσεις στη βάση δεδομένων
	 */
	public ArrayList<MemoryLogEntry> getAllEntries() {
		memoryLog.open();
		ArrayList<MemoryLogEntry> memoryLogEntries = memoryLog.getData();
		memoryLog.close();
		return memoryLogEntries;
	}

	/**
	 * Αποθηκεύει στην βάση δεδομένων την κατάστσση της Εσωτερικής και της
	 * Εξωτερικής Μνήμης
	 */
	public void saveMemoryState() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		String date = "";
		if (day < 10) {
			date += "0";
		}
		date += Integer.toString(day) + '/';
		if (month < 10) {
			date += "0";
		}
		date += Integer.toString(month) + '/' + Integer.toString(year);

		memoryLog.open();
		MemoryLogEntry entry = new MemoryLogEntry(freeInternalStorage,
				internalStorageSize, freeExternalStorage, externalStorageSize,
				date);
		memoryLog.addEntry(entry);
		memoryLog.close();
	}

	/**
	 * Διαγράφει μια καταχώριση απο την βάση δεδομένων, αλλά και απο το ListView
	 * 
	 * @param deletePosition
	 *            Η θέση της καταχώρησης μέσα στο ListView που πρέπει να
	 *            διαγραφθεί
	 */
	public static void deleteItem(int deletePosition) {
		MemoryLogEntry entry = entries.remove(deletePosition);
		memoryLog.open();
		memoryLog.deleteEntry(entry);
		memoryLog.close();
		memoryLogAdapter.notifyDataSetChanged();
		memoryLogAdapter.notifyDataSetInvalidated();
	}

	/**
	 * 
	 * Το Fragment για τον διάλογο που εμφανίζεται όταν θέλει ο χρήστης να
	 * διαγραφθεί κάποια καταχώρηση
	 * 
	 * @author Θανάσης
	 * @see DialogFragment
	 * 
	 */
	public static class AlertDialogFragment extends DialogFragment {

		public static AlertDialogFragment newInstance(int position, String date) {
			AlertDialogFragment frag = new AlertDialogFragment();
			Bundle args = new Bundle();
			args.putInt("position", position);
			args.putString("date", date);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final int position = getArguments().getInt("position");
			String date = getArguments().getString("date");

			return new AlertDialog.Builder(getActivity())

					.setTitle(getResources().getString(R.string.delete))
					.setMessage(
							getResources().getString(R.string.delete_message)
									+ ' ' + date + '?')
					.setPositiveButton(getResources().getString(R.string.yes),
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TOD O Auto-generated method stub

									// main code on after clicking yes

									deleteItem(position);

								}
							})
					.setNegativeButton(getResources().getString(R.string.no),
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).create();

		}
	}

	public class MyMemoryLogAdapter extends BaseAdapter {
		private ArrayList<MemoryLogEntry> memoryLogEntries;

		private LayoutInflater mInflater;
		private Context context;

		public MyMemoryLogAdapter(Context context,
				ArrayList<MemoryLogEntry> results) {
			memoryLogEntries = results;
			this.context = context;
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return memoryLogEntries.size();
		}

		public Object getItem(int position) {
			return memoryLogEntries.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.memory_entry_view,
						null);
				holder = new ViewHolder();
				holder.date = (TextView) convertView.findViewById(R.id.tvDate);
				holder.internalStorageInfo = (TextView) convertView
						.findViewById(R.id.tvInt);
				holder.externalStorageInfo = (TextView) convertView
						.findViewById(R.id.tvExt);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MemoryLogEntry entry = memoryLogEntries.get(position);
			holder.date.setText(entry.getDate());

			String freeString = context.getResources().getString(R.string.free);
			String sizeString = context.getResources().getString(R.string.size);
			holder.internalStorageInfo.setText(freeString + ":"
					+ entry.getFreeInternalStorage() + '\n' + sizeString + ":"
					+ entry.getInternalStorageSize());
			holder.externalStorageInfo.setText(freeString + ":"
					+ entry.getFreeExternalStorage() + '\n' + sizeString + ":"
					+ entry.getExternalStorageSize());

			return convertView;
		}

		private class ViewHolder {
			TextView date;
			TextView internalStorageInfo;
			TextView externalStorageInfo;
		}
	}

}
