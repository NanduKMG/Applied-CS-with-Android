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

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView text ;
    private TextView label ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            dictionary = new FastDictionary(assetManager.open("words.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
//        Log.d("onStart:",String.valueOf(dictionary.isWord("aardvarke")));
        text = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);
        userTurn = random.nextBoolean();
        text.setText("");
        if (userTurn) {
            label.setText(USER_TURN);
            Log.d("onStart:","userTurn");
        } else {
            label.setText(COMPUTER_TURN);
            Log.d("onStart:","computer Turn");

            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        String word = "" + text.getText();
        String poss;
        if(dictionary.isWord(word)){
            label.setText("Computer Wins");
//            onStart(null);
        }
        else {
            //get possible longer
            poss = dictionary.getGoodWordStartingWith(word);

            if (poss == null)
                label.setText("No such word!");
            else {
                Log.d("computer word:",poss);
                word = word + poss.substring(word.length(), word.length() + 1);
                text.setText(word);
                userTurn = true;
                label.setText(USER_TURN);
            }

            // Do computer turn stuff then make it the user's turn again


        }
    }

    public void onChallenge(View v){
        Log.d("Ethi","poy");
        if(dictionary.isWord(text.getText()+"")){
            label.setText("You win!");
        }
        else{
            String st = dictionary.getGoodWordStartingWith(text.getText()+"");
            if(st!=null){
                label.setText("Computer wins!");
                text.setText(st);
            }
            else{
                label.setText("You win!");
            }
        }



    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Character ch;
        String word;

        if((keyCode<=54)&&(keyCode>=29)){
            ch = (char) event.getUnicodeChar();
            Log.d("charac",ch.toString());
            text.setText(text.getText()+ch.toString());
            computerTurn();
//            word = "" + text.getText();
//            if(dictionary.isWord(word)){
//                label.setText("tholvi");
//            }
            return  true;
        }

        return super.onKeyUp(keyCode, event);
    }
}
