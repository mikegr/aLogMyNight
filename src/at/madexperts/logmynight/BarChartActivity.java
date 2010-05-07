package at.madexperts.logmynight;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}
}
