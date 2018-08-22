package com.example.scarnesdice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static int scUserOverall = 0;
    public static int scUserTurn = 0;
    public static int scCompOverall = 0;
    public static int scCompTurn = 0;
    public TextView textView;
    public static int compDice;
    public String computerState = "";
    //


    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        public void run() {
            Log.d("diceNo","Ivdethi!");
            Random random = new Random();
            int diceNo = random.nextInt(6) + 1 ;
            compDice = diceNo;

            //change image
            String diceName = "dice" + Integer.toString(diceNo);
            Log.d("diceNo",diceName);
            ImageView imageView =(ImageView) findViewById(R.id.imageView);
            int drawableID = getResources().getIdentifier(diceName, "drawable", getPackageName());
            imageView.setImageResource(drawableID);

            timerHandler.postDelayed(this, 500);

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

    }




    public int diceRoll(){

        Random random = new Random();
        int diceNo = random.nextInt(6) + 1 ;


        //change image
        String diceName = "dice" + Integer.toString(diceNo);
        Log.d("diceNo",diceName);
        ImageView imageView =(ImageView) findViewById(R.id.imageView);
        int drawableID = getResources().getIdentifier(diceName, "drawable", getPackageName());
        imageView.setImageResource(drawableID);
        return diceNo;

    }

    public void usrRoll(View view){
        //roll btn clicked!
        int diceNo = diceRoll();

        //score dealings
        if(diceNo!=1){
            scUserTurn += diceNo;
            textView.setText("Your score: * computer score: 0 your turn score:" + Integer.toString(scUserTurn));

        }
        else{
            scUserOverall += scUserTurn;
            scUserTurn = 0;
            ComputerTurn();
        }






    }

    public void resetScores(View view){
        scUserOverall = 0 ;
        scUserTurn = 0;
        scCompOverall = 0;
        scCompTurn = 0;

        textView.setText("Your score: 0 computer score: 0 your turn score:0");
    }

    public void setOverall(View view){
        scUserOverall += scUserTurn;
        scUserTurn = 0;
        textView.setText(String.format("Your score:%d Computer Score: %d Your turn Score:%d",scUserOverall,scCompOverall,scUserTurn));
        ComputerTurn();
    }

    public void ComputerTurn(){


        Log.d("diceNo","Computer started");

        //disable user btns
        Button btnRoll = findViewById(R.id.button3);
        Button btnHold = findViewById(R.id.button);
        btnRoll.setEnabled(false);
        btnHold.setEnabled(false);
        int diceNo;

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {

                int diceNo = 0;
                Log.d("diceNo","ivdethi2 after a sec");
                compDice = diceRoll(); //changes image and returns dice no
                diceNo = compDice;
                Log.d("diceNo",Integer.toString(compDice));
                if(diceNo==1) {
                    computerState = "computer rolled a one";
                    scCompOverall +=scCompTurn;
                    textView.setText(String.format("Your score:%d Computer Score: %d Your turn Score:%d , %s",scUserOverall,scCompOverall,scUserTurn,computerState));
                    scCompTurn=0;
                    Log.d("diceNo","rollled a one");
                    return;
                }

                scCompTurn += diceNo;
                textView.setText(String.format("Your score:%d Computer Score: %d Your turn Score:%d , %s",scUserOverall,scCompOverall,scUserTurn,computerState));

                if(scCompTurn>20){

                    computerState = "computer holds";
                    scCompOverall +=scCompTurn;
                    textView.setText(String.format("Your score:%d Computer Score: %d Your turn Score:%d , %s",scUserOverall,scCompOverall,scUserTurn,computerState));
                    scCompTurn=0;
                    //decide to hold
                    return;
                }

                handler.postDelayed(this,1000);
            }

        };

        handler.postDelayed(r, 1000);
        //roll


//        textView.setText(String.format("Your score:%d Computer Score: %d Your turn Score:%d , %s",scUserOverall,scCompOverall,scUserTurn,computerState));
        btnRoll.setEnabled(true);
        btnHold.setEnabled(true);
        Log.d("diceNo",computerState);
        Log.d("diceNo","Computer stopped");

    }

}
