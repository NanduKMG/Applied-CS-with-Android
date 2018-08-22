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

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

public class PathDictionary {
    private static final int MAX_WORD_LENGTH = 4;
    private static HashSet<String> words = new HashSet<>();
    private static  HashMap<String,ArrayList<String>> wordGraph =  new HashMap<>();

    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.i("Word ladder", "Loading dict and graph");
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LENGTH) {
                continue;
            }
            words.add(word);
        }
        populateGraph();
        neighbours("but");
    }

    private void populateGraph(){
        long before = System.currentTimeMillis();
        for (String word: words){
            Integer i =0;
            String filler,temp;
            ArrayList<String>  possibleSt ;
            Log.d("word",word);

            while(i< word.length() ){
                filler = word.substring(0,i)+"_"+word.substring(i+1,word.length());
                Log.d("filler",filler);
                possibleSt = new ArrayList<>();
                for(char c = 'a';c <='z';c++) {
                    temp = filler.replace('_',c);
                    if(words.contains(temp)){
                        possibleSt.add(temp);
                    }
                }
                Log.d("poss",possibleSt.toString());
                wordGraph.put(filler,possibleSt);
                i++;
            }
        }
        Log.d("time","CreateGraph took: " + (System.currentTimeMillis() - before)+ " milsecs");
    }

    public boolean isWord(String word) {
        return words.contains(word.toLowerCase());
    }

    private ArrayList<String> neighbours(String word) {
        ArrayList<String> neighSt = new ArrayList<>();
        ArrayList<String> possibleSt;
        int i=0;
        String filler;
        while( i< word.length() ){
            filler = word.substring(0,i)+"_"+word.substring(i+1,word.length());
            possibleSt = wordGraph.get(filler);
            neighSt.addAll(possibleSt);
            i++;
        }
        neighSt.removeAll(Collections.singleton(word));

        return neighSt;
    }

    public ArrayList<String> findPath(String start, String end) {
        long before = System.currentTimeMillis();

        if(!(words.contains(start)&&(words.contains(end))))
            return null;

        if(start.length()!=end.length())
            return null;
        ArrayDeque<ArrayList<String>> q = new ArrayDeque<ArrayList<String>>();
        ArrayList<String> already,temp,path = new ArrayList<String>();
        path.add(start);
        q.add(path);
        String tword;
        already = new ArrayList<String>();
        while(!q.isEmpty()){

            path = q.poll();
            if(path.size()==6){
                break; //4 level bfs
            }
            tword = path.get(path.size()-1);
            already.add(tword);
            if(neighbours(tword).contains(end)) {
                path.add(end);
                Log.d("path",path.toString());
                Log.d("time","Createpath took: " + (System.currentTimeMillis() - before)+ " milsecs");

                return path;
            }
            else{
                for(String word: neighbours(tword)){
                    if(word==start)
                        continue;
                    if(already.contains(word)){
//                        Log.d("skipped",word);
                        continue;
                    }
                    temp = new ArrayList<String>();
                    temp.addAll(path);
                    temp.add(word);
                    q.add(temp);
                }
            }
            Log.d("q",q.toString());
        }
        return null;
    }
}
