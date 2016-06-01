package com.enomasoftware.roundgirl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enomasoftware.roundgirl.BellBoy.BellBoy;
import com.enomasoftware.roundgirl.BellBoy.PausedState;
import com.enomasoftware.roundgirl.BellBoy.RunningState;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public enum StartPauseButtonState {
        START, PAUSE
    }

    private Configuration mConfiguration = null;
    private BellBoy mBellBoy = null;
    private IEvents mEventHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConfiguration = buildConfiguration();
        mEventHandler = buildEventHandler();
        mBellBoy = buildBellBoy(mConfiguration, mEventHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnStart_onClick(View view) {
        if (mBellBoy.isRunning()) {
            // Is running. Pause button was displayed.
            mBellBoy.pause();
            setStartPauseButtonState(StartPauseButtonState.START);
        } else {
            // Is paused. Start button was displayed.
            if (mBellBoy.canBeResumed()) {
                mBellBoy.resume();
            }
            else {
                // First time running.
                mBellBoy.start();
            }
            setStartPauseButtonState(StartPauseButtonState.PAUSE);
        }
    }

    private void updateText(String text) {
        TextView textView = (TextView) this.findViewById(R.id.txtMain);
        textView.setText(text.toString());
    }

    private void updateTime(int secondsUntilFinished) {
        Date date = new Date(secondsUntilFinished * 1000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        String formattedDate = dateFormat.format(date);

        updateText(formattedDate);
    }

    private void setStartPauseButtonState(StartPauseButtonState state) {
        Button button = (Button) this.findViewById(R.id.btnStart);
        if (state == StartPauseButtonState.START) {
            button.setText("Start");
        }
        else if (state == StartPauseButtonState.PAUSE) {
            button.setText("Pause");
        }
        else {
            throw new IllegalStateException("Unknown state was passed.");
        }
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

    private IEvents buildEventHandler() {
        return new IEvents() {
            @Override
            public void onStart() {
            }

            @Override
            public void onEnd() {

            }

            @Override
            public void onStartRound(int currentRound) {
                updateText("Round " + currentRound);
            }

            @Override
            public void onRoundTick(int secondsUntilFinished) {
                updateTime(secondsUntilFinished - 1);
            }

            @Override
            public void onNSecondsBeforeEndRound(int currentSecond) {
                System.out.println("NSecondsBeforeEndRound called. Current second is " + currentSecond + ".");
            }

            @Override
            public void onEndRound(int currentRound) {

            }

            @Override
            public void onStartBreak() {
                updateText("Break");
            }

            @Override
            public void onBreakTick(int secondsUntilFinished) {
                updateTime(secondsUntilFinished - 1);
            }

            @Override
            public void onEndBreak() {

            }
        };
    }

    private BellBoy buildBellBoy(Configuration configuration, IEvents events) {
        return new BellBoy(configuration, events);
    }
}
