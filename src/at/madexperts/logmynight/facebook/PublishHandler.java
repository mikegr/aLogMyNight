/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.madexperts.logmynight.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import at.madexperts.logmynight.HistoryActivity;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;

/**
 * A handler for the stream page. It's responsible for
 * fetching the stream data from the API and storing it
 * in a local file based cache. It uses the helper class
 * StreamRenderer to render the stream.
 *  
 * @author yariv
 */
public class PublishHandler extends Handler {

	private static final String TAG = PublishHandler.class.getName();
    private static final String CACHE_FILE = "cache.txt";
    
    /**
     * Called by the dispatcher to render the stream page.
     */
    public void go() {
    	Facebook fb = Session.restore(getActivity()).getFb();
        AsyncFacebookRunner runner = new AsyncFacebookRunner(fb);
        Bundle params = new Bundle();
        String message = getActivity().getIntent().getStringExtra("message");
        
        params.putString("message", message);
        params.putString("link", "http://github.com/mikegr/alogmynight");
        params.putString("name", "Testname");
        params.putString("caption", "Test caption");
        params.putString("description", "<b>HTML Test for message</b>");
        
        
        runner.request("me/feed", params, "POST", new AsyncRequestListener() {

            public void onComplete(JSONObject obj) {
                String html;
                Log.d(TAG, "onComplete");
                getActivity().finish();
            }
        });
        
    }
}
