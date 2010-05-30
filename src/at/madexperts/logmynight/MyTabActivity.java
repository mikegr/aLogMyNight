package at.madexperts.logmynight;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
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

public class MyTabActivity extends Activity {

	TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.buttonbar);
	    
	    updateView(findViewById(R.id.buttonBar1), "Rechnung", R.drawable.icon_wein_small, HistoryActivity.class);
	    updateView(findViewById(R.id.buttonBar2), "Aufrisse", R.drawable.icon_cocktail_small, GalleryActivity.class);
	    
	    
	}
	
	
	View updateView(View tab, String name, int imageId, final Class activity) {
		
		
		ImageView image = (ImageView) tab.findViewById(R.id.tabImage);
		Drawable d = getResources().getDrawable(imageId);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		image.setImageDrawable(d);
		TextView text = (TextView) tab.findViewById(R.id.tabText);
		text.setText(name);
		tab.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(MyTabActivity.this, activity);
				startActivity(i);
			}
		});
		
		return tab;
	}
}
