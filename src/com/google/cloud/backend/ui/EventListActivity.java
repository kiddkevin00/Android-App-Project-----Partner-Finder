package com.google.cloud.backend.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.google.cloud.backend.R;
import com.google.cloud.backend.core.CloudBackendAsync;
import com.google.cloud.backend.core.CloudBackendFragment;
import com.google.cloud.backend.core.CloudBackendMessaging;
import com.google.cloud.backend.core.CloudCallbackHandler;
import com.google.cloud.backend.core.CloudEntity;
import com.google.cloud.backend.core.CloudQuery;
import com.google.cloud.backend.core.CloudQuery.Order;
import com.google.cloud.backend.core.CloudQuery.Scope;

/**
 * Activity to show all the items from cloud storage in a list
 *
 */
public class EventListActivity extends Activity {

	//Variable declaration 
	private CloudBackendAsync cloudBackend;
	private List<CloudEntity> mPosts;
	private ListView listView;
	private ProgressDialog progressDialog;
	private int index=0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlist_screen);

		//Initializing UI components
		listView = (ListView) findViewById(R.id.events_listview);

		progressDialog = ProgressDialog.show(EventListActivity.this, "", "Please wait...");

		//CloudBackendAsync class instance creation to get the list of data
		cloudBackend = new CloudBackendAsync(EventListActivity.this);

		//Query to get the data from particular Kind/table
		CloudQuery cq = new CloudQuery(CloudBackendMessaging.KIND_NAME_CLOUD_MESSAGES);
		//Setting scope
		cq.setScope(Scope.FUTURE_AND_PAST);
		//Setting order of the list item according to date of creation
		cq.setSort("date",Order.DESC);//(CloudEntity.PROP_CREATED_AT, Order.DESC);

		//Executing Cloud query
		cloudBackend.list(cq, new CloudCallbackHandler<List<CloudEntity>>() {

			@Override
			public void onComplete(List<CloudEntity> results) {
				mPosts = results;

				deleteEvent(mPosts);
				
				progressDialog.dismiss();

				//Calling EventListAdaptor to create list
				EventListAdaptor eventListAdaptor = new EventListAdaptor(EventListActivity.this, mPosts);
				listView.setAdapter(eventListAdaptor);
				eventListAdaptor.notifyDataSetChanged();

				
			}
		});
	}
	
	public void deleteEvent(List<CloudEntity> list){
		final CloudBackendMessaging cloudBackendMessaging = new CloudBackendMessaging(EventListActivity.this);

		
		for(int i=0;i<list.size();i++){
			index = i;
			CloudEntity ce = list.get(i);
			
			HashMap<String, Object> map = (HashMap<String, Object>) ce.getProperties();
			
			final String id = ce.getId();
			
			String eventDate = (String) map.get("date"); 
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd", new Locale("US"));
			String currentDate = sdf.format(new Date());
			
			if(eventDate.compareTo(currentDate)<0){
				//Thread to delete all records from the Kind/table. Used only once as thread is not using start() method now
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							cloudBackendMessaging.delete(CloudBackendMessaging.KIND_NAME_CLOUD_MESSAGES, id);
							mPosts.remove(index);
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}).start();
			}
		}
		
	}
}
