package at.madexperts.logmynight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawActivity extends Activity implements OnTouchListener {

	private static final String TAG = DrawActivity.class.getName();
	
	private MyView view; 
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view =  new MyView(this);
		view.setOnTouchListener(this);
		setContentView(view);
		
	};
	
	private boolean isDraw = false;
	private float x = 0;
	private float y = 0;
	private float w = 0;
	private float h = 0;
	
	public boolean onTouch(View v, MotionEvent event) {
		Log.d(TAG, "X=" + event.getX() + ";Y=" + event.getY());
		if (isDraw) {
			w = event.getX();
			h = event.getY();
			isDraw = false;
			view.postInvalidate();
		}
		else {
			x = event.getX();
			y = event.getY();
			isDraw = true;
		}		
		
		return false;
	}
	
	private class MyView extends View {
			public MyView(Context ctx) {
				super(ctx);
			}
			
			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
					Log.d("", "Drawing");
				Paint p = new Paint();
				p.setColor(Color.CYAN);
				p.setStrokeWidth(4);
				canvas.drawRect(x, y, w, h, p);
			}
	}
	
}
