package at.madexperts.logmynight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager.OnActivityResultListener;

import com.codecarpet.fbconnect.FBFeedActivity;
import com.codecarpet.fbconnect.FBLoginActivity;
import com.codecarpet.fbconnect.FBSession;
import com.codecarpet.fbconnect.FBSession.FBSessionDelegate;


public class FacebookHelper {

	public static final int MESSAGE_PUBLISHED = 1834950434;
    private static final String API_KEY = "06893827c978510877e95202f7412ebe"; // "<YOUR API KEY>";

	// Enter either your API secret or a callback URL (as described in documentation):
    private static final String API_SECRET = "13a0adee6b8f69b9723ddfa0d657f2fe"; // "<YOUR SECRET KEY>";

    
	private Activity ctx;
	private Handler mHandler;
	FBSession mSession;
	
	public FacebookHelper(Activity ctx) {
		this.ctx = ctx;
	}
	
	public void publish(String message) {
		if (mSession == null || ! mSession.isConnected()) {
			mSession = FBSession.getSessionForApplication_secret(API_KEY, API_SECRET, new FBSessionDelegateImpl());
			mHandler = new Handler();
			Intent intent = new Intent(ctx, FBLoginActivity.class);
	        ctx.startActivity(intent);
		}
		else {
			postMessage();	
		}
	}
	
	private class FBSessionDelegateImpl extends FBSessionDelegate {

        public void sessionDidLogin(FBSession session, Long uid) {
            mHandler.post(new Runnable() {
				
				public void run() {
					postMessage();
				}
			});
        }

        public void sessionDidLogout(FBSession session) {
        	mHandler.post(new Runnable() {
				public void run() {
					mSession = null;
				}
			});
        }
	}
	
	private void postMessage() {
		Intent intent = new Intent(ctx, FBFeedActivity.class);
		intent.putExtra("userMessagePrompt", "Add how you feel about this");
		intent.putExtra("attachment",
						"{\"name\":\"LogMyNight for Android\"," +
						 "\"href\":\"http://www.facebook.com/pages/LogMyNight/126857323995698/\"," +
						 "\"caption\":\"Caption\"," +
						 "\"description\":\"Description\"," +
						 "\"media\":[{" +
						 	"\"type\":\"image\"," +
						 	"\"src\":\"http://img40.yfrog.com/img40/5914/iphoneconnectbtn.jpg\"," +
						 	"\"href\":\"http://developers.facebook.com/connect.php?tab=iphone/\"" +
						 	"}]," +
						 "\"properties\":{\"another link\":{\"text\":\"Facebook home page\",\"href\":\"http://www.facebook.com\"}}" +
						"}");
		ctx.startActivityForResult(intent, MESSAGE_PUBLISHED);
	}

}
