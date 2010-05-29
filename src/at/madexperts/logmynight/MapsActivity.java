package at.madexperts.logmynight;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import at.madexperts.logmynight.chart.BarItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapsActivity extends MapActivity {

	private static final String TAG = MapsActivity.class.getName();

	private SQLiteDatabase db;
	private MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		
		db = new DatabaseHelper(this).getReadableDatabase();
		
		Cursor cursor = db
		.rawQuery(
				"SELECT d.name as name, d.longitude as longitude, d.latitude as latitude "
						+ "FROM location d",
				null);
		
		/* Get the indices of the Columns we will need */
		int nameColumn = cursor.getColumnIndex("name");
		int latitudeColumn = cursor.getColumnIndex("latitude");
		int longitudeColumn = cursor.getColumnIndex("longitude");
		
		List<OverlayItem> items = new ArrayList<OverlayItem>();
		
		
		Double latitudeE6 = null;
		Double longitudeE6 = null;
		
		Double minLatitude = 81*1E6;;
		Double maxLatitude = -81*1E6;;
		
		Double minLongitude = 180*1E6;
		Double maxLongitude = -180*1E6;

		/* Check if our result was valid. */
		if (cursor != null) {
			/* Check if at least one Result was returned. */
			if (cursor.moveToFirst()) {
				int i = 0;
				/* Loop through all Results */
				do {
					i++;
					/*
					 * Retrieve the values of the Entry the Cursor is pointing
					 * to.
					 */
					String name = cursor.getString(nameColumn);
					float latitude = cursor.getFloat(latitudeColumn);
					float longitude = cursor.getFloat(longitudeColumn);
					
					/* Add current location to map. */
					Log.d(TAG, "Name: " + name + " latitude: " + latitude + " longitude: " + longitude);
					
					latitudeE6 = latitude*1E6;
					longitudeE6 = longitude*1E6;
					
					minLatitude = (latitudeE6 < minLatitude) ? latitudeE6 : minLatitude;
					maxLatitude = (latitudeE6 > maxLatitude) ? latitudeE6 : maxLatitude;
					
					minLongitude = (longitudeE6 < minLongitude) ? longitudeE6 : minLongitude;
					maxLongitude = (longitudeE6 > maxLongitude) ? longitudeE6 : maxLongitude;

					items.add(new OverlayItem(new GeoPoint(latitudeE6.intValue(), longitudeE6.intValue()), name, "adsf"));
					
				} while (cursor.moveToNext());
			}

			cursor.close();
		}
		
		//add LocationsOverlay to map
		Drawable drawable = getResources().getDrawable(R.drawable.marker);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		mapView.getOverlays().add(new LocationsOverlay(drawable, items));
		
		MapController controller = mapView.getController();
		
		Double centerLatitude = (maxLatitude + minLatitude) / 2;
		Double centerLongitude = (maxLongitude + minLongitude) / 2;
		controller.setCenter(new GeoPoint(centerLatitude.intValue(), centerLongitude.intValue()));
		
		Double spanLatitude = (maxLatitude - minLatitude) * 1.1;
		Double spanLongitude = (maxLongitude - minLongitude) * 1.1;
		
		controller.zoomToSpan(spanLatitude.intValue(), spanLongitude.intValue());
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
