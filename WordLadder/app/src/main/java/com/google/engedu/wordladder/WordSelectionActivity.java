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

package com.google.engedu.wordladder;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.engedu.worldladder.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


class pathFinder implements Runnable {
    ArrayList<String> path; // name of thread
    Thread t;
    public String end,start;
    public PathDictionary dictionary;
    pathFinder(PathDictionary dict,String begin,String stop) {
        t = new Thread(this);
        this.dictionary = dict;
        this.start = begin;
        this.end = stop;
         // Start the thread
    }
    // This is the entry point for thread.
    public void run() {
//        try {
            path = dictionary.findPath(start, end);
//        Log.d("Thread1",path.toString());

    }
}

public class WordSelectionActivity extends AppCompatActivity {

    private PathDictionary dictionary;
    public  ArrayList<String> words = new ArrayList<>();
    public static final String EXTRA_WORDS = "com.example.wordladder.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new PathDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public boolean onStart(View view) {
        Log.d("start","started shit");
        Toast toast = Toast.makeText(this, "finding path ...",
                Toast.LENGTH_SHORT);
        toast.show();

        final TextView startWordView = (TextView) findViewById(R.id.startWord);
        final TextView endWordView = (TextView) findViewById(R.id.endWord);
        String start = startWordView.getText().toString().toLowerCase();
        String end = endWordView.getText().toString().toLowerCase();

        pathFinder p1 = new pathFinder(dictionary,start,end);
        pathFinder p2 = new pathFinder(dictionary,end,start);

        long before = System.currentTimeMillis();

        p1.t.start();
        p2.t.start();

        while(true) {
            if (!(p1.t.isAlive() && p2.t.isAlive())) {
                if(p1.t.isAlive()&& p2.path!=null){
                    words = p2.path;
                    Log.d("Thread p2 first!",words.toString());
                    Log.d("Threadtime 2","Threads took: " + (System.currentTimeMillis() - before)+ " milsecs");

                    p1.t.interrupt();

                    break;
                }
                else if(p2.t.isAlive()&& p1.path!=null){
                    words = p1.path;
                    Log.d("Thread p1 first!",words.toString());
                    Log.d("Threadtime 1","Threads took: " + (System.currentTimeMillis() - before)+ " milsecs");

                    p2.t.interrupt();
                    break;
                }
                else if(!(p1.t.isAlive()||p2.t.isAlive())){
                    words = null;
                    Log.d("no path really!","NULL");

                    break;
                }

            }
        }

        Log.d("Threadtime","Threads took: " + (System.currentTimeMillis() - before)+ " milsecs");


        if (words != null) {
            Log.d("path",words.toString());
            // TODO: Launch new activity here
            Intent intent = new Intent(this, SolverActivity.class);
            intent.putExtra(EXTRA_WORDS, words);
            startActivity(intent);
        } else {
            Log.i("Word ladder", "Word combination is not possible");
             toast = Toast.makeText(this, "Couldn't find path between the two given words",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_word_selection, menu);
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
}
