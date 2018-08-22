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

package com.google.engedu.wordstack;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2,scrambled;
    private Stack<LetterTile> placedTiles = new Stack();
    private String uword1="",uword2="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length()==5)
                    words.add(word);

            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);
                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                placedTiles.push(tile);
                return true;
            }
            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();

                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    if(v.getId()== R.id.word1){
                        uword1 += tile.getLetter();

                    }
                    else if(v.getId()== R.id.word2){
                        uword2 += tile.getLetter();
                    }
                    tile.moveToViewGroup((ViewGroup) v);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                        Log.d("words",uword1+ " and " +uword2);
                    }
                    placedTiles.push(tile);
                    return true;
            }
            return false;
        }
    }


    public boolean onStartGame(View view) {
        placedTiles.removeAllElements();
        View wordLinear1 = findViewById(R.id.word1);
        View wordLinear2 = findViewById(R.id.word2);
        ((ViewGroup) wordLinear1).removeAllViews();
        ((ViewGroup) wordLinear2).removeAllViews();
        stackedLayout.clear();

        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        Random random = new Random();
        word1 = words.get(random.nextInt(words.size()));
        word2 = words.get(random.nextInt(words.size()));
        Log.d("words",word1+' '+word2);

        //scramble add
        int cntWord1 = WORD_LENGTH;
        int cntWord2 = WORD_LENGTH;
        int choice = 0;
        scrambled = "";
        LetterTile letterTile = null;

        while ((cntWord1!=0)&&(cntWord2!=0)){
            choice = ThreadLocalRandom.current().nextInt(1, 2 + 1);
            //Log.d("words","choice"+choice);
            if(choice==1){
                scrambled += word1.charAt(WORD_LENGTH-cntWord1);
                cntWord1 -= 1;
            }
            else {
                scrambled += word2.charAt(WORD_LENGTH-cntWord2);
                cntWord2 -= 1;

            }
            //Log.d("words",scrambled);

        }

        if(cntWord1>0){
            while (cntWord1!=0){
                scrambled += word1.charAt(WORD_LENGTH - cntWord1);

                cntWord1 -= 1;
            }
        }

        if(cntWord2>0){
            while (cntWord2!=0){
                scrambled += word2.charAt(WORD_LENGTH - cntWord2);
                cntWord2 -= 1;
            }
        }


        //adding all letters to  stacklayout
        Log.d("words",scrambled);
        int sclen = scrambled.length()-1;
        while(sclen >= 0){
            Log.d("words",Character.toString(scrambled.charAt(sclen)));
            Log.d("words",Integer.toString(sclen));
            letterTile = new LetterTile(this,scrambled.charAt(sclen));
            stackedLayout.push(letterTile);
            sclen -= 1;

        }

        Log.d("words",scrambled);
        messageBox.setText(scrambled);

        return true;
    }

    public boolean onUndo(View view) {
        if(!placedTiles.empty()) {
            LetterTile tilePopped = (LetterTile) placedTiles.pop();
            tilePopped.moveToViewGroup(stackedLayout);
        }else{
            Toast toast = Toast.makeText(this, "Enthirdey.Nothing to undo", Toast.LENGTH_LONG);
            toast.show();
        }

        return true;
    }
}
