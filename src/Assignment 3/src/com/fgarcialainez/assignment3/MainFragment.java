package com.fgarcialainez.assignment3;

import java.io.File;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class MainFragment extends Fragment {
	
	/**
	 * This method will only be called once when the retained
	 * Fragment is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}

	public void startImageDownload(Uri url) {
		// Start a new AsyncTask to download the image
		new DownloadImageAsyncTask().execute(url);
	}
	
	/**
	 * Download an image for a given URL and starts a new AsyncTask to filter it
	 */
	class DownloadImageAsyncTask extends AsyncTask<Uri, Void, Uri> {

        @Override
        protected Uri doInBackground(Uri... params) {
        	// Download image in background
        	Uri downloadedImageUri = null;
        	
        	if(params != null && params.length > 0) {
        		downloadedImageUri = Utils.downloadImage(getActivity(), params[0]);
        	}
        	
        	return downloadedImageUri;
        }

        @Override
        protected void onPostExecute(Uri result) {
        	// If the image was downloaded successfully then filter it
        	if(result != null) {
        		// Start a new AsyncTask to filter the image
        		new FilterImageAsyncTask().execute(result);
        	}
        	else {
        		// Display error message
    			Toast.makeText(getActivity(), R.string.error_downloading_image_message, Toast.LENGTH_SHORT).show();
        	}
        }
    }
	
	/**
	 *	Filter a downloaded image and view it in the image gallery
	 */
	class FilterImageAsyncTask extends AsyncTask<Uri, Void, Uri> {

        @Override
        protected Uri doInBackground(Uri... params) {
        	// Filter image in background
        	Uri filteredImageUri = null;
        	
        	if(params != null && params.length > 0) {
        		filteredImageUri = Utils.grayScaleFilter(getActivity(), params[0]);
        	}
        	
        	return filteredImageUri;
        }

        @Override
        protected void onPostExecute(Uri result) {
        	// If the image was filtered successfully then display it
        	if(result != null) {
        		// Display the filtered image
        		Intent galleryIntent = new Intent();
            	galleryIntent.setAction(Intent.ACTION_VIEW);
            	galleryIntent.setDataAndType(Uri.fromFile(new File(result.toString())), "image/*");
            	
            	startActivity(galleryIntent);
        	}
        	else {
        		// Display error message
    			Toast.makeText(getActivity(), R.string.error_filtering_image_message, Toast.LENGTH_SHORT).show();
        	}
        }
    }
}