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
package at.madexperts.logmynight.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Paint.Style;
import android.text.StaticLayout;
import android.text.TextPaint;
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

	private float NAME_LABEL_SPACE_TO_BAR = 2f;
	private float AMOUNT_LABEL_SPACE_TO_BAR = 5f;
	private float Y_LABEL_SPACE_TO_LINE = 5f;

	private Paint strokePaint;
	private Paint labelPaint;

	private Paint bar1Paint;
	private Paint bar2Paint;
	private Paint bar3Paint;
	private Paint bar4Paint;
	private Paint bar5Paint;
	private Paint bar6Paint;

	private Paint[] barPaints;

	private List<BarItem> barItems = new ArrayList<BarItem>();

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

		if (barItems != null && barItems.size() > 0) {
			Collections.sort(barItems);
			invalidate();
		}
	}

	private void drawBars(Canvas canvas, float width, float height,
			float stopX, float stopY, float lineWidth, float lineHeight) {

		int counter = 0;

		float spaceForFigureOnFirstBar = 20f;

		float heightOfOneDrink = 0;

		float space = lineWidth / 6f;
		float widthOfBar = space / 2f;
		float halfWidthOfBar = widthOfBar / 2f;

		// float leftFirstBar = STROKE_REDUCE_X_LEFT + spaceForFigureOnFirstBar;
		float heightOfFirstBar = stopY - STROKE_REDUCE_Y_TOP
				- spaceForFigureOnFirstBar;

		float pointerInWidth = stopX + space - halfWidthOfBar;

		for (BarItem barItem : barItems) {

			float amount = (float) barItem.getAmount();

			if (heightOfOneDrink == 0) {
				heightOfOneDrink = heightOfFirstBar / amount;
				Log.d(TAG, "heightOfOneDrink: " + heightOfOneDrink);
			}

			// float left = leftFirstBar;
			float heightOfBar = amount * heightOfOneDrink;

			float left = pointerInWidth - widthOfBar;
			float top = stopY - heightOfBar;
			float right = pointerInWidth;
			float bottom = stopY;
			final int mainColor = barPaints[counter].getColor();

			Log.d(TAG, "amount: " + amount + "left: " + left + " top: " + top
					+ " right: " + right + "bottom: " + bottom);

			// left <= right and top <= bottom

			canvas.drawRect(left, top, right, bottom, barPaints[counter]);
			
			
			paint3d(left, top, right, bottom, mainColor, canvas);
			/*
			Paint linePaint = new Paint();
			linePaint.setColor(Color.WHITE);
			canvas.drawLine(right, top, right+10, top-10, linePaint);
			*/

			// name
			canvas.save();
			
			float tempX = pointerInWidth - halfWidthOfBar;
			float tempY = stopY + NAME_LABEL_SPACE_TO_BAR;
			canvas.translate(tempX, tempY);
			StaticLayout layout = new StaticLayout(barItem.getName(), new TextPaint(labelPaint), (int)space-2,
					android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f,
					0f, true);
	
			layout.draw(canvas);
			canvas.restore();
			
//			canvas.drawText(barItem.getName(), pointerInWidth - halfWidthOfBar,
//					stopY + NAME_LABEL_SPACE_TO_BAR, labelPaint);

			// amount
			canvas.drawText(String.valueOf(barItem.getAmount()), pointerInWidth
					- halfWidthOfBar, top - AMOUNT_LABEL_SPACE_TO_BAR,
					labelPaint);

			// set pointer one bar up
			pointerInWidth += space;

			if (counter == 4)
				break;
			counter++;
		}

		// draw rest of the drinks
		float sumAmountOfRest = 0;
		for (int i = 5; i < barItems.size(); i++) {
			BarItem barItem = barItems.get(i);
			sumAmountOfRest += barItem.getAmount();
		}

		if (sumAmountOfRest != 0) {
			float heightOfBar = sumAmountOfRest * heightOfOneDrink;

			float left = pointerInWidth - widthOfBar;
			float top = stopY - heightOfBar;
			float right = pointerInWidth;
			float bottom = stopY;

			canvas.drawRect(left, top, right, bottom, barPaints[5]);
			paint3d(left, top, right, bottom, barPaints[5].getColor(), canvas);
			
			//name
			canvas.save();
			
			float tempX = pointerInWidth - halfWidthOfBar;
			float tempY = stopY + NAME_LABEL_SPACE_TO_BAR;
			canvas.translate(tempX, tempY);
			StaticLayout layout = new StaticLayout("Others", new TextPaint(labelPaint), (int)space-2,
					android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f,
					0f, true);
	
			layout.draw(canvas);
			canvas.restore();
			
//			canvas.drawText("Others", pointerInWidth - halfWidthOfBar, stopY
//					+ NAME_LABEL_SPACE_TO_BAR, labelPaint);

			// amount
			canvas.drawText(String.valueOf((int) sumAmountOfRest),
					pointerInWidth - halfWidthOfBar, top
							- AMOUNT_LABEL_SPACE_TO_BAR, labelPaint);
		}
	}

	
	private void paint3d(float left, float top, float right, float bottom, int mainColor, Canvas canvas) {
		Path topPath = new Path();
		topPath.moveTo(left, top);
		topPath.lineTo(left+10, top-10);
		topPath.lineTo(right+10, top-10);
		topPath.lineTo(right, top);
		Paint topPaint = new Paint();
		topPaint.setStyle(Style.FILL_AND_STROKE);
		topPaint.setColor(lighten(mainColor, 0.875f));
		canvas.drawPath(topPath, topPaint);
		
		Path sidePath = new Path(); 
		sidePath.moveTo(right, top);
		sidePath.lineTo(right+10, top-10);
		sidePath.lineTo(right+10, bottom-10);
		sidePath.lineTo(right, bottom);
		
		Paint sidePaint = new Paint();
		sidePaint.setStyle(Style.FILL_AND_STROKE);
		sidePaint.setColor(lighten(mainColor, 0.75f));
		canvas.drawPath(sidePath, sidePaint);

	} 
	
	
	private int lighten(int color, float newLightValue) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] = newLightValue;
		return Color.HSVToColor(hsv);
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

		// draw y label
		canvas.translate(stopX - Y_LABEL_SPACE_TO_LINE,
				(stopY - startYVertical) / 2 + startYVertical);
		canvas.rotate(-90);
		canvas.drawText("Menge", 0, 0, labelPaint);
		canvas.restore();

		drawBars(canvas, width, height, stopX, stopY, lineWidth, lineHeight);
	}
}
