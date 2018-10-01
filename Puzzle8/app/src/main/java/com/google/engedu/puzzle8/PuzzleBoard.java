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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles = new ArrayList<>();
    private int tileLen;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getWidth());
        bitmap = Bitmap.createScaledBitmap(bitmap,parentWidth,parentWidth,false);
        tileLen = parentWidth/NUM_TILES;
        int i;
        Log.d("tileSplit","split begins");

        for(i=0;i<NUM_TILES*NUM_TILES-1;i++){
            tiles.add(new PuzzleTile(Bitmap.createBitmap(bitmap,(i%NUM_TILES)*tileLen,(i/NUM_TILES)*tileLen,tileLen,tileLen),i));
            Log.d("tileSplitting",i+"");

        }
        Log.d("tileSplit","split success");

        tiles.add(null);
    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        //loc empty square
        ArrayList<PuzzleBoard> neigbours = new ArrayList<>();
        PuzzleBoard temp;
        int i=0,x=0,y=0;
        for(i=0;i<NUM_TILES*NUM_TILES;i++){
            x = i%NUM_TILES;
            y = i/NUM_TILES;
            if(tiles.get(i)==null)
                break;
        }
        Log.d("tileShuffle",i+"");

        //consider all neighb tiles
        int[][] neighs = NEIGHBOUR_COORDS.clone();
        i=0;
        for(int[] ar:NEIGHBOUR_COORDS){
//          Log.d("arr", Arrays.toString(ar));
            if((x+ar[0]<NUM_TILES)&&(y+ar[1]<NUM_TILES)&&(x+ar[0]>-1)&&(y+ar[1]>-1)){
                Log.d("arr", Arrays.toString(ar));
                temp = new PuzzleBoard(this);
                temp.swapTiles(XYtoIndex(x,y),XYtoIndex(x+ar[0],y+ar[1]));
                neigbours.add(temp);
            }
        }




        return neigbours;
    }

    public int priority() {
        return 0;
    }

}
