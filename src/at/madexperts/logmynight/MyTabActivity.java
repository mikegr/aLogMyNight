package at.madexperts.logmynight;

import android.app.TabActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;

public class MyTabActivity extends TabActivity {

	TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tablayout);

	    mTabHost = getTabHost();
	    
	    
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(getImageView(R.drawable.icon_bier_small)).setContent(R.id.textview1));
	    
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(getImageView(R.drawable.icon_wein_small)).setContent(R.id.textview2));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator(getImageView(R.drawable.icon_anti_small)).setContent(R.id.textview3));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test4").setIndicator(getImageView(R.drawable.icon_bier_small)).setContent(R.id.textview1));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test5").setIndicator(getImageView(R.drawable.icon_bier_small)).setContent(R.id.textview1));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test6").setIndicator(getImageView(R.drawable.icon_bier_small)).setContent(R.id.textview1));
	    
	    TabWidget tabWidget = getTabWidget();
	    for(int i=0;i < tabWidget.getChildCount();i++) {
	    	View view = tabWidget.getChildAt(i);
	    	Log.d("MYTAB", view.getClass().getName());
	    }
	    mTabHost.setCurrentTab(0);
	}
	
	ImageView getImageView(int id) {
		ImageView image = new ImageView(this);
		image.setBackgroundDrawable(getResources().getDrawable(id));
		return image;
	}
}
