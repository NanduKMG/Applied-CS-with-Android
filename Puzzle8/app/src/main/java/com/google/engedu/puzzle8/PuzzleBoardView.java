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

package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap,int v) {
        int width = getWidth();
        int numTiles= v;
        puzzleBoard = new PuzzleBoard(imageBitmap, width,numTiles);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            Random randomGenerator = new Random();
            int index,i=0;
            ArrayList<PuzzleBoard> possibles = new ArrayList<>();
            while(i<10) {
                possibles = puzzleBoard.neighbours();
                index = randomGenerator.nextInt(possibles.size());
                puzzleBoard = possibles.get(index);
                // Do something. Then:
                puzzleBoard.reset();
                invalidate();
                i++;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }
    public  static Comparator<PuzzleBoard> puzzleBoardComparator = new Comparator<PuzzleBoard>() {

        @Override
        public int compare(PuzzleBoard a, PuzzleBoard b) {
            return a.priority() - b.priority();
        }
    };

    public void solve() {
        PuzzleBoard temp;
        animation = new ArrayList<>();
        PriorityQueue<PuzzleBoard> pq = new PriorityQueue<PuzzleBoard>(1,puzzleBoardComparator);
        pq.add(puzzleBoard);
        puzzleBoard.printBoard();
        while(!pq.isEmpty()){
            Log.d("Pq size","before "+pq.size()+"");

            temp = pq.poll();
//            temp.printBoard();
            Log.d("Pq size","after :" +pq.size()+"");

            if(!temp.resolved()){
                for(PuzzleBoard pb:temp.neighbours()){
                  if(!pq.contains(pb)){
                      pq.add(pb);
//                      pq.add(temp);
//                      Log.d("Quesize",pq.size()+"yeaaaaaaaaaaaaaaaaaaaaaaaaaa");
//                      temp.previousBoard.printBoard();
//                      pb.printBoard();
                  }
                }
//                pq.addAll(temp.neighbours());
            }
            else{
                animation.add(temp);
                temp = temp.previousBoard;
                while(temp!=puzzleBoard) {
                    animation.add(temp);
                    Log.d("Solutions","below");
                    temp.printBoard();
                    temp = temp.previousBoard;
                }
                animation.add(temp);
                Collections.reverse(animation);
                break;
            }
        }
        invalidate();
    }
}
