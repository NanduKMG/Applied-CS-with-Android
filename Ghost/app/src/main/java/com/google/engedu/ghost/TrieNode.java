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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode temp = this;
//        Log.d("TrieNode:",String.valueOf(this.children.size()));
        for (int i = 0; i < s.length(); i++){
            String c = String.valueOf(s.charAt(i));

            if (temp.children.get(c)!=null){
                temp = temp.children.get(c);
            }
            else {

                temp.children.put(c,new TrieNode());
                temp = temp.children.get(c);
            }

        }
//        Log.d("TrieNode:","WORD END");

        temp.isWord = true;
    }

    public boolean isWord(String s) {
        TrieNode temp = this;
        for (int i = 0; i < s.length(); i++){
            String c = String.valueOf(s.charAt(i));

            if (temp.children.get(c)!=null){
                temp = temp.children.get(c);
            }
            else {
                return false;
            }

        }
        if(temp.isWord) return true;

      return false;
    }

    public String getAnyWordStartingWith(String prefix) {
        Random generator = new Random();
        String c,word;
        TrieNode temp = this;
        Set keyset = temp.children.keySet();

        ArrayList<String> lettersArray = new ArrayList<>();
        if(prefix.isEmpty()) {
            lettersArray.addAll(keyset);
            prefix = lettersArray.get(generator.nextInt(lettersArray.size()));
        }
        word = prefix;
        //iterate till end of prefix
        for (int i = 0; i < word.length(); i++){
            c = String.valueOf(word.charAt(i));

            if (temp.children.get(c)!=null){
                temp = temp.children.get(c);
            }
            else {
                //no word starting with this prefix
                return null;
            }

        }
        //get some random word under this prefix
        while (true){
            keyset = temp.children.keySet();
            lettersArray= new ArrayList<>();
            lettersArray.addAll(keyset);
            c = lettersArray.get(generator.nextInt(lettersArray.size()));//random character
            word = word + c;
            temp = temp.children.get(c);
            if (temp.isWord) return word;
            else continue;
        }

    }

    public String getGoodWordStartingWith(String prefix) {
        Random generator = new Random();
        String c, word;
        TrieNode temp = this;
        Set keyset = temp.children.keySet();

        ArrayList<String> lettersArray = new ArrayList<>();
        ArrayList<String> lettersTemp = new ArrayList<>();
        if (prefix.isEmpty()) {
            lettersArray.addAll(keyset);
            prefix = lettersArray.get(generator.nextInt(lettersArray.size()));
        }
        word = prefix;
        //iterate till end of prefix
        for (int i = 0; i < word.length(); i++) {
            c = String.valueOf(word.charAt(i));

            if (temp.children.get(c) != null) {
                temp = temp.children.get(c);
            } else {
                //no word starting with this prefix
                return null;
            }

        }
        //get good starting

        keyset = temp.children.keySet();
        lettersArray = new ArrayList<>();
        lettersArray.addAll(keyset);
        lettersTemp.addAll(keyset);
        for (String letter : lettersTemp) {
            if (temp.children.get(letter).isWord) lettersArray.remove(letter);
        }
        if (lettersArray.isEmpty()) {
            //no possible great word . computer fails if user challenges
            c = lettersTemp.get(generator.nextInt(lettersTemp.size()));
            word = word + c;
            temp = temp.children.get(c);
        } else {
            c = lettersArray.get(generator.nextInt(lettersArray.size()));
            word = word + c;
            temp = temp.children.get(c);
        }
        Log.d("good word",lettersArray.toString());
        Log.d("all words",lettersTemp.toString());
        Log.d("word now",word);
        Log.d("all words",String .valueOf(temp.children.size()));

        //continue search
        while (true) {
            keyset = temp.children.keySet();
            if(keyset.size()==0){
                return word;
            }
            lettersArray = new ArrayList<>();
            lettersArray.addAll(keyset);
            c = lettersArray.get(generator.nextInt(lettersArray.size()));//random character
            word = word + c;
            temp = temp.children.get(c);
            if (temp.isWord) return word;
            else continue;
        }
    }


}
