package com.google.engedu.wordladder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.engedu.worldladder.R;

import java.util.ArrayList;

public class SolverActivity extends AppCompatActivity {
    public ArrayList<String> words;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_solver);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        Log.d("avt","ivdethi");
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        words = intent.getStringArrayListExtra(WordSelectionActivity.EXTRA_WORDS);
//        String[] words = intent.getStringExtra(WordSelectionActivity.EXTRA_WORDS);

        // Capture the layout's TextView and set the string as its text
        TextView start = (TextView)findViewById(R.id.startTextView);
        TextView end = new TextView(this);
        end.setTextAppearance(this,R.style.TextAppearance_AppCompat_Large);

        Log.d("act",words.toString());
        start.setText(words.get(0));
        Log.d("edit","aaah");

        for(int i=0;i<words.size()-2;i++){
            Log.d("edit","yes");

            EditText editText = new EditText(this);
            editText.setTag(i);
            editText.setId(i);
            layout.addView(editText);
            Log.d("edit",String.valueOf(i));
        }
        end.setText(words.get(words.size()-1));
        layout.addView(end);
    }

    public void onSolve(View view){
        for(int i=0;i<words.size()-2;i++){
            EditText editText = (EditText) findViewById(i);
            editText.setText(words.get(i+1));
            editText.setEnabled(false);
            editText.setFocusable(false);
            editText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }


}
