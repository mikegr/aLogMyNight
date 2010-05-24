/*
 	LogMyNight - Android app for logging night activities. 
 	Copyright (c) 2010 Michael Greifeneder <mikegr@gmx.net>, Oliver Selinger <oliver.selinger@gmail.com>
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
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
