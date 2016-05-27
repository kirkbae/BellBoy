package com.enomasoftware.roundgirl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Configuration mConfiguration = null;
    private IEvents mEvents = null;
    private BellBoy mBellBoy = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnStart_onClick(View view) {
        mConfiguration = buildConfiguration();
        mEvents = buildEvents();
        mBellBoy = buildBellBoy(mConfiguration, mEvents);

        mBellBoy.start();
    }

    private void UpdateText(String text) {
        TextView textView = (TextView) this.findViewById(R.id.txtMain);
        textView.setText(text.toString());
    }

    private Configuration buildConfiguration() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String numRoundsStr = sharedPref.getString("num_rounds", "12");
        int numRounds = Integer.parseInt(numRoundsStr);
        String roundDurationStr = sharedPref.getString("round_duration", "180");
        int roundDuration = Integer.parseInt(roundDurationStr);
        String breakDurtaionStr = sharedPref.getString("break_duration", "60");
        int breakDurtaion = Integer.parseInt(breakDurtaionStr);

        return new Configuration(numRounds, roundDuration, 10, breakDurtaion);
    }

    private IEvents buildEvents() {
        return new IEvents() {
            @Override
            public void onStart()
            {
                System.out.println("onStart called.");
            }
            @Override
            public void onEnd()
            {
                System.out.println("onEnd called.");
            }
            @Override
            public void onStartRound(int currentRound) {
                System.out.println("onStartRound called. Current round is " + currentRound);
            }
            @Override
            public void onRoundTick(int secondsUntilFinished) {
                UpdateText(String.valueOf(secondsUntilFinished));
            }
            @Override
            public void onNSecondsBeforeEndRound(int currentSecond) {
                System.out.println("onNSecondsBeforeEndRound called. Current second is " + currentSecond);
            }
            @Override
            public void onEndRound(int currentRound) {
                System.out.println("onEndRound called. Current round is " + currentRound);
            }
            @Override
            public void onStartBreak()
            {
                System.out.println("onStartBreak called.");
            }
            @Override
            public void onBreakTick(int secondsUntilFinished) {
                UpdateText(String.valueOf(secondsUntilFinished));

            }
            @Override
            public void onEndBreak()
            {
                System.out.println("onEndBreak called.");
            }
        };
    }

    private BellBoy buildBellBoy(Configuration configuration, IEvents events) {
        return new BellBoy(configuration, events);
    }
}
