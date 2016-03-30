package com.example.ostaskoptional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Το Fragment στο οποίο εμφανίζονται όλες τις ενεργές διεργασίες στην συσκευή σε ένα ListView 
 * @author Θανάσης
 *
 */
public class ProcessesInfoFragment extends ListFragment {

	
	ActivityManager activityManager;
	ArrayList<ProcessInfo> processesInfo;
	ListView listView;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		processesInfo = getAllProcessesInfo();
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_processes_info,
				container, false);
		listView = (ListView)view.findViewById(android.R.id.list);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new MyProcessInfoAdapter(getActivity(), processesInfo));
	}
	
	

	/**
	 * Επιστρέφει σε ένα ArrayList τις πληροφορίες για κάθε μια ενεργή διεργασία 
	 * @return Το ArrayList με τις πληροφορίες της κάθε διεργασίας
	 * @see ProcessInfo
	 */
	public ArrayList<ProcessInfo> getAllProcessesInfo() {
		ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningProcesses = activityManager
				.getRunningAppProcesses();
		
		
		ArrayList<ProcessInfo> processesInfo = new ArrayList<ProcessInfo>();
		for(RunningAppProcessInfo runningProcess : runningProcesses){
			
			//To pid της διεργασίας
			int pid = runningProcess.pid;
			
			//Το όνομα της διεργασίας
			String processName = runningProcess.processName;
			
			//Υπολογισμός της μνήμης που χρησιμοποιεί η διεργασία
			android.os.Debug.MemoryInfo[] memNFO = activityManager
					.getProcessMemoryInfo(new int[] { pid });
			int usage = memNFO[0].getTotalPss();		
			String usageString = null;
			if (usage >= 1) {
				float usageMB = usage / 1024.0f;
				 usageString =  String.format("%.2f", usageMB) + " MB";
			} else {
				if (usage > 0) {
					DecimalFormat format = new DecimalFormat("000.00");
					 usageString = format.format(usage) + " KB";
				} else {
					usageString =  "0";
				}
			}
			
			int importance = runningProcess.importance;
			String importanceString = null;
			switch (importance) {
			case RunningAppProcessInfo.IMPORTANCE_BACKGROUND:
				 importanceString = "BACKGROUND";
				break;
			case RunningAppProcessInfo.IMPORTANCE_FOREGROUND:
				 importanceString = "FOREGROUND";
				break;
			case RunningAppProcessInfo.IMPORTANCE_EMPTY:
				 importanceString = "EMPTY";
				break;
			case RunningAppProcessInfo.IMPORTANCE_SERVICE:
				 importanceString ="SERVICE";
				break;
			case RunningAppProcessInfo.IMPORTANCE_VISIBLE:
				 importanceString ="VISIBLE";
				break;
			default:
				 importanceString ="UNKNOWN";
				break;
			}
			
			ProcessInfo processInfo = new ProcessInfo(pid,processName,usageString,importanceString);
			processesInfo.add(processInfo);
		}
		
		return processesInfo;
	}

	
	
	
	public class MyProcessInfoAdapter extends BaseAdapter {
		private ArrayList<ProcessInfo> runningProcesses;

		private LayoutInflater mInflater;

		public MyProcessInfoAdapter(Context context,
				ArrayList<ProcessInfo> results) {
			runningProcesses = results;
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return runningProcesses.size();
		}

		public Object getItem(int position) {
			return runningProcesses.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.process_view, null);
				holder = new ViewHolder();
				holder.processName = (TextView) convertView
						.findViewById(R.id.tvProcessName);
				holder.processPID = (TextView) convertView
						.findViewById(R.id.tvProcessPID);
				holder.processMemory = (TextView) convertView
						.findViewById(R.id.tvProcessMemory);
				holder.processImportance = (TextView) convertView
						.findViewById(R.id.tvProcessImportance);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			ProcessInfo processInfo = processesInfo.get(position);
			
			String name = processInfo.getName();
			int pid = processInfo.getPID();
			String memoryUsage = processInfo.getMemoryUsage();
			String importance = processInfo.getImportance();
			

			holder.processName.setText(name);
			holder.processPID.setText("PID: " + pid);
			holder.processMemory.setText("Mem " + memoryUsage);
			holder.processImportance.setText(importance);
			return convertView;
		}

		private class ViewHolder {
			TextView processName;
			TextView processPID;
			TextView processMemory;
			TextView processImportance;
			
		}
	}

	
	
		
	

}
