package com.google.cloud.backend.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.backend.R;
import com.google.cloud.backend.core.CloudEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * List Adaptor to display list
 */
public class EventListAdaptor extends BaseAdapter {


	//Variable declaration
	private LayoutInflater mInflater;
	private Activity _activity;
	private List _list;


	public EventListAdaptor(Activity eventListActivity, List list) {
		this._activity = eventListActivity;
		this._list = list;
		mInflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return _list.size();
	}

	@Override
	public Object getItem(int position) {
		return _list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		ViewHolder holder;

		//Get Cloud Entity from list at particular index
		final CloudEntity ce = (CloudEntity) getItem(position);

		//String id = ce.getId();

		//Get all properties of the cloud entity
		final Map<String, Object> eventValues = ce.getProperties();

		//View Holder to create list item view
		holder = new ViewHolder();

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.event_row, null);

			//Initialization of UI components
			holder._event_listrow = (LinearLayout) convertView.findViewById(R.id.row);
			holder._nameText = (TextView) convertView.findViewById(R.id.nameTextView);
			holder._placeText = (TextView) convertView.findViewById(R.id.placeTextView);
			holder._dateText = (TextView) convertView.findViewById(R.id.dateTextView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//Click listener on each list item 
		holder._event_listrow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(_activity, ShowEventActivity.class);

				//Creating bundle instance to send event values
				Bundle bundle = new Bundle();
				//putting the value in bundle 
				bundle.putSerializable("eventInfo", (Serializable) eventValues);

				//add the bundle to intent
				intent.putExtras(bundle);

				//starting activity
				_activity.startActivity(intent);
			}
		});

		//Checking if these items are available or not in properties
		if(eventValues.containsKey("name") && eventValues.containsKey("date") && eventValues.containsKey("place")){

			String name = eventValues.get("name").toString();
			String place = eventValues.get("place").toString();
			String date =eventValues.get("date").toString();

			holder._nameText.setText(name);
			holder._placeText.setText(place);
			holder._dateText.setText(date);
		}

		return convertView;

	}



	static class ViewHolder {
		LinearLayout _event_listrow;
		TextView _nameText;
		TextView _dateText;
		TextView _placeText;

	}

}
