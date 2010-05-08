package at.madexperts.logmynight.chart;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BarChartView extends View {

	private static final String TAG = BarChartView.class.getName();

	private float STROKE_REDUCE_Y_TOP = 40;
	private float STROKE_REDUCE_Y_BOTTOM = 100;
	private float STROKE_REDUCE_X_LEFT = 40;
	private float STROKE_REDUCE_X_RIGHT = 30;

	private float STROKE_WIDTH = 1;
	private float LABEL_WIDTH = 2;
	
	private float LABEL_SPACE_TO_BAR = 16f;

	private Paint strokePaint;
	private Paint labelPaint;
	
	private Paint bar1Paint;
	private Paint bar2Paint;
	private Paint bar3Paint;
	private Paint bar4Paint;
	private Paint bar5Paint;
	private Paint bar6Paint;

	private Paint[] barPaints;

	private List<BarItem> barItems;

	// ===========================================================
	// Constructors
	// ===========================================================
	public BarChartView(Context context) {
		super(context);
		initPaints();
	}

	public BarChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaints();
	}

	private void initPaints() {
		strokePaint = new Paint();
		strokePaint.setStrokeWidth(STROKE_WIDTH);
		strokePaint.setColor(Color.GRAY);
		
		labelPaint = new Paint();
		labelPaint.setStrokeWidth(LABEL_WIDTH);
		labelPaint.setColor(Color.GRAY);
		labelPaint.setTextAlign(Paint.Align.CENTER);

		bar1Paint = new Paint();
		bar1Paint.setStrokeWidth(STROKE_WIDTH);
		bar1Paint.setColor(Color.RED);

		bar2Paint = new Paint();
		bar2Paint.setStrokeWidth(STROKE_WIDTH);
		bar2Paint.setColor(Color.GREEN);

		bar3Paint = new Paint();
		bar3Paint.setStrokeWidth(STROKE_WIDTH);
		bar3Paint.setColor(Color.BLUE);

		bar4Paint = new Paint();
		bar4Paint.setStrokeWidth(STROKE_WIDTH);
		bar4Paint.setColor(Color.YELLOW);

		bar5Paint = new Paint();
		bar5Paint.setStrokeWidth(STROKE_WIDTH);
		bar5Paint.setColor(Color.MAGENTA);
		
		bar6Paint = new Paint();
		bar6Paint.setStrokeWidth(STROKE_WIDTH);
		bar6Paint.setColor(Color.CYAN);


		barPaints = new Paint[] { bar1Paint, bar2Paint, bar3Paint, bar4Paint,
				bar5Paint, bar6Paint };
	
	}

	public void setData(List<BarItem> barItems) {
		this.barItems = barItems;

		Collections.sort(barItems);
	}

	private void drawBars(Canvas canvas, float width, float height,
			float stopX, float stopY, float lineWidth, float lineHeight) {
		
		setData(BarChartDummyData.getTestData());
		
		int counter = 0;
		
		float spaceForFigureOnFirstBar = 20f;	
		
		float heightOfOneDrink = 0;
		
		float space = lineWidth/6f;
		float widthOfBar = space/2f;
		float halfWidthOfBar = widthOfBar / 2f;
		
		//float leftFirstBar = STROKE_REDUCE_X_LEFT + spaceForFigureOnFirstBar;
		float heightOfFirstBar = stopY - STROKE_REDUCE_Y_TOP - spaceForFigureOnFirstBar;
		
		float pointerInWidth = stopX + space - halfWidthOfBar;
		
		for (BarItem barItem : barItems) {
			
			float amount = (float)barItem.getAmount();
			
			if(heightOfOneDrink == 0) {
				heightOfOneDrink = heightOfFirstBar/amount;
				Log.d(TAG, "heightOfOneDrink: " + heightOfOneDrink);
			}
		
			//float left = leftFirstBar;
			float heightOfBar = amount * heightOfOneDrink;
			
			float left = pointerInWidth - widthOfBar;
			float top = stopY-heightOfBar;
			float right = pointerInWidth;
			float bottom = stopY;
			
			Log.d(TAG, "amount: " + amount + "left: " + left + " top: " + top + " right: " + right + "bottom: " + bottom);

			// left <= right and top <= bottom

			canvas.drawRect(left, top, right, bottom, barPaints[counter]);
			
			canvas.drawText(barItem.getName(), pointerInWidth - halfWidthOfBar, stopY + LABEL_SPACE_TO_BAR, labelPaint);
			
			//set pointer one bar up
			pointerInWidth += space;

			if (counter == 4)
				break;
			counter++;
		}
		
		//draw rest of the drinks
		float sumAmountOfRest = 0;
		for (int i = 5; i < barItems.size(); i++) {
			BarItem barItem = barItems.get(i);
			sumAmountOfRest += barItem.getAmount();
		}
		
		if(sumAmountOfRest != 0) {
			float heightOfBar = sumAmountOfRest * heightOfOneDrink;
			
			float left = pointerInWidth - widthOfBar;
			float top = stopY-heightOfBar;
			float right = pointerInWidth;
			float bottom = stopY;
			
			canvas.drawRect(left, top, right, bottom, barPaints[5]);			
			canvas.drawText("Others", pointerInWidth - halfWidthOfBar, stopY + LABEL_SPACE_TO_BAR, labelPaint);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float height = canvas.getHeight();
		float width = canvas.getWidth();

		Log.d(TAG, "canvas height: " + height);
		Log.d(TAG, "canvas width: " + width);

		// draw Y & X line
		float stopX = STROKE_REDUCE_X_LEFT;
		float stopY = height - STROKE_REDUCE_Y_BOTTOM;

		float lineWidth = width - STROKE_REDUCE_X_LEFT - STROKE_REDUCE_X_RIGHT;
		float lineHeight = height - STROKE_REDUCE_Y_BOTTOM
				- STROKE_REDUCE_Y_TOP;

		float startXHorizontal = width - STROKE_REDUCE_X_RIGHT;
		float startYHorizontal = stopY;

		Log.d(TAG, "Draw X line: " + startXHorizontal + "/" + startYHorizontal
				+ " - " + stopX + "/" + stopY);
		canvas.drawLine(startXHorizontal, startYHorizontal, stopX, stopY,
				strokePaint);

		float startXVertical = stopX;
		float startYVertical = STROKE_REDUCE_Y_TOP;

		Log.d(TAG, "Draw Y line: " + startXVertical + "/" + startYVertical
				+ " - " + stopX + "/" + stopY);
		canvas.drawLine(startXVertical, startYVertical, stopX, stopY
				+ STROKE_WIDTH / 2f, strokePaint);

		drawBars(canvas, width, height, stopX, stopY, lineWidth, lineHeight);

		// left <= right and top <= bottom

		// canvas.drawRect(left, top, right, bottom, strokePaint);

		// canvas.drawText("Menge", width / 2f + 5f, stopY + 3f, strokePaint);
		//		
		// canvas.drawCircle(200, 200, 50, strokePaint);
		// canvas.drawBitmap(BitmapFactory.decodeResource(mContext.getResources(),
		// R.drawable.icon), 184, 184, null);
		//invalidate();
	}
}
