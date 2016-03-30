package com.example.ostaskoptional;

import java.util.List;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Ένας βασικός BaseAdapter για ListViews που κάθε View έχει μόνο δύο TextViews, το ένα κάτω απο το άλλο
 * @author Θανάσης
 * @see ListView
 * @see TextView
 * @see BaseAdapter
 */
public class TwoLineListViewAdapter extends BaseAdapter {
	private List<StringPair> titlesAndDescriptions;

	private LayoutInflater mInflater;

	public TwoLineListViewAdapter(Context context,
			List<StringPair> results) {
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
			convertView = mInflater.inflate(R.layout.general_info_view, null);
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
		holder.title.setText(titleAndDesciption.title);
		holder.description.setText(titleAndDesciption.description);
		return convertView;
	}

	private class ViewHolder {
		TextView title;
		TextView description;
	}
}