package at.madexperts.logmynight;

import android.app.TabActivity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MyTabActivity extends TabActivity {

	TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.tablayout);

	    mTabHost = getTabHost();
	    
	    
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(getView("Loggen", R.drawable.icon_bier_small)).setContent(R.id.textview1));
	    
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(getView("Rechnung", R.drawable.icon_wein_small)).setContent(R.id.textview2));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator(getView("Aufrisse", R.drawable.icon_anti_small)).setContent(R.id.textview3));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_test4").setIndicator(getView("Lokale", R.drawable.icon_bier_small)).setContent(R.id.textview1));
	    
	    /*mTabHost.addTab(mTabHost.newTabSpec("tab_test5").setIndicator(getView(R.drawable.icon_bier_small)).setContent(R.id.textview1));
	      mTabHost.addTab(mTabHost.newTabSpec("tab_test6").setIndicator(getView(R.drawable.icon_bier_small)).setContent(R.id.textview1));
	    */
	    
	    TabWidget tabWidget = getTabWidget();
	    for(int i=0;i < tabWidget.getChildCount();i++) {
	    	View view = tabWidget.getChildAt(i);
	    	Log.d("MYTAB", view.getClass().getName());
	    }
	    mTabHost.setCurrentTab(0);
	}
	
	
	View getView(String name, int imageId) {
		View tab = getLayoutInflater().inflate(R.layout.tab, null);
		ImageView image = (ImageView) tab.findViewById(R.id.tabImage);
		Drawable d = getResources().getDrawable(imageId);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		image.setImageDrawable(d);
		TextView text = (TextView) tab.findViewById(R.id.tabText);
		text.setText("Drinks");
		return tab;
	}
}
