package at.madexperts.logmynight.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BarChartView extends View {

	// ===========================================================
	// Constructors
	// ===========================================================
	public BarChartView(Context context) {
		super(context);
	}

	public BarChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint myPaint = new Paint();
		myPaint.setStrokeWidth(3);
		myPaint.setColor(0xFF097286);
		canvas.drawCircle(200, 200, 50, myPaint);
//		canvas.drawBitmap(BitmapFactory.decodeResource(mContext.getResources(),
//				R.drawable.icon), 184, 184, null);
		invalidate();
	}
}
