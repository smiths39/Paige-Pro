/*
    Name:   MemoStyle.java
    Author: Sean Smith
    Date:   28 December 2013

    The design of the memo interface within the application is defined.
*/

package com.project.paigepro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class MemoStyle extends EditText {

    private Rect rect;
    private Paint paint;

    public MemoStyle(Context context, AttributeSet attrs) {

        super(context, attrs);

        rect = new Rect();
        paint = new Paint();

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int lineCounter = getHeight() / getLineHeight();
        int lineBase = getLineBounds(0, rect) + 1;

        if (getLineCount() > lineCounter) {
            lineCounter = getLineCount();
        }

        for (int index = 0; index < lineCounter; index++) {

            canvas.drawLine(rect.left, lineBase, rect.right, lineBase, paint);
            lineBase += getLineHeight() - 1;

            super.onDraw(canvas);
        }
    }
}
