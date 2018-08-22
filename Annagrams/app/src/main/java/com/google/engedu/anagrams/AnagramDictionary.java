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

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String ,ArrayList> lettersToWord = new HashMap<>();
    private HashMap<Integer ,ArrayList> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;
    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;


        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sorted = sortLetters(word);

            if(sizeToWords.get(word.length())!=null){
                ArrayList<String> sizeWords = sizeToWords.get(word.length());
                sizeWords.add(word);
                sizeToWords.put(word.length(),sizeWords);
            }
            else{
                ArrayList<String> sizeWords = new ArrayList<>();
                sizeWords.add(word);
                sizeToWords.put(word.length(),sizeWords);
            }

            if(lettersToWord.get(sorted)!=null){
                ArrayList<String> wordList = lettersToWord.get(sorted);
                wordList.add(word);
                lettersToWord.put(sorted,wordList);
//                Log.d("letters2word","added word :" + word);
            }
            else{
                ArrayList<String> wordList = new ArrayList<>();
                wordList.add(word);
                lettersToWord.put(sorted,wordList);

            }


        }
    }

    public boolean isGoodWord(String word, String base) {

        Log.d("onemore",String.valueOf(wordSet.size()));
        if((wordSet.contains(word))&&( !word.contains(base))){
            Log.d("onemore","returned true good word!");
            return true;
        }

        Log.d("onemore","returned false good word because");
        Log.d("onemore",String.valueOf(wordSet.contains(word)));
        Log.d("onemore",String.valueOf(!word.contains(base)));
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        for (String wordIter: wordList){
            String iterSort = sortLetters(wordIter);
            String wordSort = sortLetters(targetWord);

            if(iterSort.equals(wordSort)){
                result.add(wordIter);
            }
        }

        return result;
    }

    public String sortLetters(String word){
        String sorted = word;
        char[] charArray = sorted.toCharArray();
        Arrays.sort(charArray);
        sorted = new String(charArray);
        //System.out.println(sorted);
        return sorted;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        Log.d("onemore",word);
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        ArrayList<String> annagrams = null;

        for (char letter:alphabet) {

            String newWord = word + letter;
            newWord = sortLetters(newWord);
            Log.d("onemore",newWord);
            if((annagrams = lettersToWord.get(newWord))!=null){
                Log.d("onemore","!!! Annagram ondathre!");
                for (String annagram:annagrams) {
                    Log.d("onemore",annagram);
                    if(isGoodWord(annagram,word)){
                        Log.d("onemore","Its a good word also!");
                        result.add(annagram);

                    }
                }
            }

        }

        return result;
    }

    public String pickGoodStarterWord() {
        String goodWord = new String();
        List<String> annagrams = null;


        do {
            int randIndex = random.nextInt(wordList.size());

            goodWord  = wordList.get(randIndex);

            annagrams = getAnagramsWithOneMoreLetter(goodWord);


        }
        while ((annagrams.size()<MIN_NUM_ANAGRAMS) || (goodWord.length()!=wordLength));

        if(wordLength<MAX_WORD_LENGTH){
            wordLength++;
        }

        Log.d("wordLength",String.valueOf(goodWord.length()));
        Log.d("wordLength",String.valueOf(wordLength));
        return goodWord;
    }
}
