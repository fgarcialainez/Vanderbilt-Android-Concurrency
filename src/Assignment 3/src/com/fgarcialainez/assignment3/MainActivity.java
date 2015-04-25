package com.fgarcialainez.assignment3;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG_ASYNC_TASKS_FRAGMENT = "async_tasks_fragment";

	private MainFragment mAsyncTasksFragment;  
	private EditText mUrlEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mUrlEditText = (EditText)findViewById(R.id.url_edit_text);
		
		FragmentManager fm = getFragmentManager();
		mAsyncTasksFragment = (MainFragment) fm.findFragmentByTag(TAG_ASYNC_TASKS_FRAGMENT);

	    // If the Fragment is non-null, then it is currently being
	    // retained across a configuration change.
	    if (mAsyncTasksFragment == null) {
	    	mAsyncTasksFragment = new MainFragment();
	    	fm.beginTransaction().add(mAsyncTasksFragment, TAG_ASYNC_TASKS_FRAGMENT).commit();
	    }
	}
	
	// Referenced in XML
	public void downloadImageAction(View v) {
		
		String url = mUrlEditText.getText().toString();
		
		if(url == null || url.length() == 0) {
			url = getResources().getString(R.string.default_image_url);
		}
		
		if(URLUtil.isValidUrl(url)) {
			// Start image download
			mAsyncTasksFragment.startImageDownload(Uri.parse(url));
		}
		else {
			// Reset entered text
			mUrlEditText.setText("");
			
			// Display error message
			Toast.makeText(this, R.string.invalid_url_message, Toast.LENGTH_SHORT).show();
		}
	}
}
