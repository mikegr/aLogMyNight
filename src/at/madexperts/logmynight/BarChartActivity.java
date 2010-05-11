package at.madexperts.logmynight;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import at.madexperts.logmynight.chart.BarChartDummyData;
import at.madexperts.logmynight.chart.BarChartView;

public class BarChartActivity extends Activity {

	private static final String TAG = BarChartActivity.class.getName();
	private SQLiteDatabase db;
	private BarChartView barChartView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		db = new DatabaseHelper(this).getReadableDatabase();

		setContentView(R.layout.barchart);
		barChartView = (BarChartView) findViewById(R.id.barChartView);
		barChartView.setData(BarChartDummyData.getTestData());
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		Log.d(TAG, "onRetainNonConfigurationInstance");
		return null;
	}

	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
}
