package at.madexperts.logmynight;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class LocationsOverlay extends ItemizedOverlay<OverlayItem> {

	private static final String TAG = LocationsOverlay.class.getName();
	private List<OverlayItem> items;
	private Drawable marker;

	public LocationsOverlay(Drawable marker, List<OverlayItem> items) {
		super(marker);
		this.marker = marker;
		this.items = items;

		populate();
	}

	@Override
	protected OverlayItem createItem(int arg0) {
		Log.d(TAG, "create overlay " + arg0);
		return items.get(arg0);
	}

	@Override
	public int size() {
		return items.size();
	}

}
