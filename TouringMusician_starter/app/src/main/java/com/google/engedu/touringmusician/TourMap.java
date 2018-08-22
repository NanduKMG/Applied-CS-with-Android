/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();

    //extension
    private CircularLinkedList list_beg = new CircularLinkedList();
    private CircularLinkedList list_small = new CircularLinkedList();
    private CircularLinkedList list_near = new CircularLinkedList();

    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
        linePaint.setStyle(Paint.Style.STROKE);

        Paint linePaint_beg = new Paint();
        linePaint_beg.setColor(Color.BLACK);
        linePaint_beg.setStrokeWidth(5);
        linePaint_beg.setStyle(Paint.Style.STROKE);

        Paint linePaint_small = new Paint();
        linePaint_small.setColor(Color.GREEN);
        linePaint_small.setStrokeWidth(5);
        linePaint_small.setStyle(Paint.Style.STROKE);

        Paint linePaint_near = new Paint();
        linePaint_near.setColor(Color.BLUE);
        linePaint_near.setStrokeWidth(5);
        linePaint_near.setStyle(Paint.Style.STROKE);



        Point curr = new Point(0,0);
        Point first = new Point(0,0);

//        for (Point p : list) {
//            /**
//             **
//             **  YOUR CODE GOES HERE
//             **
//             **/
//            canvas.drawCircle(p.x, p.y, 20, pointPaint);
//            if(curr.x==0 && curr.y==0){
//                curr=p;
//                first=p;
//            }
//            else
//            {
//                canvas.drawLine(curr.x,curr.y,p.x,p.y,linePaint);
//                curr=p;
//            }
//        }
        for (Point p : list_beg) {
            /**
             **
             **  YOUR CODE GOES HERE
             **
             **/
            canvas.drawCircle(p.x, p.y, 20, pointPaint);
            if(curr.x==0 && curr.y==0){
                curr=p;
                first=p;
            }
            else
            {
                canvas.drawLine(curr.x,curr.y,p.x,p.y,linePaint_beg);
                curr=p;
            }
        }
        canvas.drawLine(curr.x,curr.y,first.x,first.y,linePaint_beg);


        for (Point p : list_near) {
            /**
             **
             **  YOUR CODE GOES HERE
             **
             **/
            canvas.drawCircle(p.x, p.y, 20, pointPaint);
            if(curr.x==0 && curr.y==0){
                curr=p;
                first=p;
            }
            else
            {
                canvas.drawLine(curr.x+5,curr.y+5,p.x+5,p.y+5,linePaint_near);
                curr=p;
            }
        }
        canvas.drawLine(curr.x+5,curr.y+5,first.x+5,first.y+5,linePaint_near);

        for (Point p : list_small) {
            /**
             **
             **  YOUR CODE GOES HERE
             **
             **/
            canvas.drawCircle(p.x, p.y, 20, pointPaint);
            if(curr.x==0 && curr.y==0){
                curr=p;
                first=p;
            }
            else
            {
                canvas.drawLine(curr.x+10,curr.y+10,p.x+10,p.y+10,linePaint_small);
                curr=p;
            }
        }


        canvas.drawLine(curr.x+10,curr.y+10,first.x+10,first.y+10,linePaint_small);

        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    list.insertBeginning(p);
                }
                list_beg.insertBeginning(p);
                list_near.insertNearest(p);
                list_small.insertSmallest(p);


                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (message != null) {
                    message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        list_small.reset();
        list_near.reset();
        list_beg.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
