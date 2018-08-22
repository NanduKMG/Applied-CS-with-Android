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

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private ArrayList<String> odd;
    private ArrayList<String> even;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        Random generator = new Random();
        if(prefix.isEmpty())
            return words.get(generator.nextInt(words.size()));

        //binary search
        String sub;
        int min = 0,max = words.size() - 1,mid;

        while(min<=max){
            mid = (min + max)/2 ;
            sub = (words.get(mid)).substring(0,prefix.length());
            Log.d("mid element:",String.valueOf(mid) + " " + words.get(mid) + "sub :"+sub);
            if(sub.equals(prefix)){
                return words.get(mid);
            }
            else if(sub.compareTo(prefix)<0){
                Log.d("comp neg",sub+" "+prefix);
                min = mid + 1;
            }
            else{
                Log.d("comp pos",sub+" "+prefix);
                max = mid-1;
            }
        }

        return null;
    }


    private int setAllStarting(String prefix){
        int pos,down,up ;
        String word;


        pos = getFirst(words,prefix);
        Log.d("Starting word",words.get(pos));
        //pos = BinarySearch(words,prefix);
        if (pos==-1)
            return -1;

        odd = new ArrayList<>();
        even = new ArrayList<>();
//        down = pos-1;
//
//        while(true){
//            word = words.get(down);
//            if(word.startsWith(prefix)){
//                down = down -1;
//            }
//            else break;
//        }
//        up = down+1;

        while(true){
            word = words.get(pos);
            if(word.startsWith(prefix)){
                if(word.length()%2==0)
                    even.add(word);
                else odd.add(word);
                pos = pos +1;
            }
            else break;
        }

        return 0;
    }

    private int BinarySearch(ArrayList<String> words,String prefix){

        String sub,word;
        int min = 0,max = words.size() - 1,mid;

        while(min<=max){
            mid = (min + max)/2 ;
            word = words.get(mid);

            sub = word.substring(0,prefix.length());
            Log.d("Binary Search","middle element:"+String.valueOf(mid)+ " " + words.get(mid));
            //word.startsWith(prefix) should be used pakshe below is for good words bad words extension
            if(word.equals(prefix)){
                Log.d("Binary Search","Found:"+String.valueOf(mid)+ " " + words.get(mid));
                return mid;
            }
            else if(sub.compareTo(prefix)<0){
                Log.d("Binary Search","prefix>word"+word+" "+prefix);
                min = mid + 1;
            }
            else{
                Log.d("Binary Search","prefix<word"+word+" "+prefix);
                max = mid-1;
            }
        }
        return -1;
    }

    private int getFirst(ArrayList<String> words,String prefix){

        String word;
        int first,min = 0,max = words.size() - 1,mid;
        first = -1;
        while(min<=max){
            mid = (min + max)/2 ;
            word = words.get(mid);
            if(word.compareTo(prefix)<0){
                min = mid + 1;
            }
            else{
                max = mid-1;
            }
            first = min;
        }
        return first;
    }




    @Override
    public String getGoodWordStartingWith(String prefix) {
        Random generator = new Random();
        String word;
        ArrayList<String> mixed = new ArrayList<>() ;
        if(prefix.isEmpty())
            return words.get(generator.nextInt(words.size()));

        //if(prefix.length()<4)
        if(setAllStarting(prefix)<0) return null;
        Log.d("odd:",odd.toString());
        Log.d("even",even.toString());
        mixed.addAll(even);
        mixed.addAll(odd);
        //Select an even lettered word if prefix is even and vice versa so that user will fail at some point
        //Eg; prefix is w, i.e odd taking welcome computer adds 'e' , and at some point user will have to fail since odd turns are users.


        if((prefix.length()%2==0)&&(even.size()>0)){

            word = even.get(generator.nextInt(even.size()));
            while(true) {
                Log.d("inter",word);
                if (BinarySearch(odd, prefix + word.substring(prefix.length(), prefix.length() + 1)) >= 0) {
                    word = even.get(generator.nextInt(even.size()));
                }
                else break;
            }
            Log.d("finally even",word);

            return word;

        }
        else if((prefix.length()%2!=0)&&odd.size()>0) {
            word = odd.get(generator.nextInt(odd.size()));
            while(true) {
                Log.d("inter",word);
                if (BinarySearch(even, prefix + word.substring(prefix.length(), prefix.length() + 1)) >= 0) {
                    word = odd.get(generator.nextInt(odd.size()));
                }
                else break;
            }
            Log.d("finally odd",word);
            return word;
        }
        else {
            Log.d("finally","something");
            if (mixed.size()==0) return null;
            else return mixed.get(generator.nextInt(mixed.size()));
        }
    }
}



