package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();
    
    /**
     * Post changes to UI Thread
     */
    private final Handler mHandler = new Handler();
    
    /**
     * Name of the parameter containing the Url of the downloaded image.
     */
    public static final String DOWNLOADED_IMAGE_URL_INTENT_PARAM = "DOWNLOADED_IMAGE_URL";

    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // @@ TODO -- you fill in here.
    	super.onCreate(savedInstanceState);
    	
    	Log.d(TAG, "onCreate(): activity created anew");

        // Get the URL associated with the Intent data.
        // @@ TODO -- you fill in here.
    	final String imageUrl = getIntent().getDataString();

    	if(imageUrl != null) {
    		// Download the image in the background, create an Intent that
    		// contains the path to the image file, and set this as the
    		// result of the Activity.
    		new Thread(new Runnable() {
				@Override
				public void run() {
					final Uri downloadedImageUrl = DownloadUtils.downloadImage(DownloadImageActivity.this, Uri.parse(imageUrl));

			        // @@ TODO -- you fill in here using the Android "HaMeR"
			        // concurrency framework.  Note that the finish() method
			        // should be called in the UI thread, whereas the other
			        // methods should be called in the background thread.
		    		if(downloadedImageUrl != null) {
		    			// Return downloaded image Url
		    			Intent resultIntent = new Intent();
		    			resultIntent.putExtra(DOWNLOADED_IMAGE_URL_INTENT_PARAM, downloadedImageUrl);
		                setResult(Activity.RESULT_OK, resultIntent);
		    		}
		    		else {
		    			// Return nothing
		    			setResult(Activity.RESULT_CANCELED);
		    		}
		    		
		    		// Finish Activity in UI Thread
		    		mHandler.post(new Runnable() {
						@Override
						public void run() {
							// Finish Activity
			                finish();
						}
		    		});
				}
    		}).start();
    	}
    	else {
    		// Return nothing
			setResult(Activity.RESULT_CANCELED);
			
			// Finish Activity
            finish();
    	}
    }
}
