package com.enomasoftware.ringside;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enomasoftware.ringside.BellBoy.BellBoy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public enum StartPauseButtonState {
        START, PAUSE
    }

    private MediaPlayer mStartBellPlayer = null;
    private MediaPlayer mEndBellPlayer = null;
    private MediaPlayer mRoundEndWarningSoundPlayer = null;
    private MediaPlayer mBreakEndWarningSoundPlayer = null;


    private Configuration mConfiguration = null;
    private BellBoy mBellBoy = null;
    private IEvents mEventHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartBellPlayer = MediaPlayer.create(this, R.raw.one_bell);
        mEndBellPlayer = MediaPlayer.create(this, R.raw.three_bells);
        mRoundEndWarningSoundPlayer = MediaPlayer.create(this, R.raw.cracking);
        mBreakEndWarningSoundPlayer = MediaPlayer.create(this, R.raw.whistle);

        mConfiguration = buildConfiguration();
        mEventHandler = buildEventHandler();
        mBellBoy = buildBellBoy(mConfiguration, mEventHandler);

        init(mConfiguration, mBellBoy);
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
            case R.id.reset_timer:
                resetTimer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        mStartBellPlayer.stop();
        mEndBellPlayer.stop();
        mRoundEndWarningSoundPlayer.stop();
        mBreakEndWarningSoundPlayer.stop();

        // Todo: Destroy timers.
        super.onDestroy();
    }

    public void btnStartPause_onClick(View view) {
        if (mBellBoy.isRunning()) {
            // Is running. Pause button was displayed.
            mBellBoy.pause();
        } else {
            // Is paused. Start button was displayed.
            if (mBellBoy.canBeResumed()) {
                mBellBoy.resume();
            }
            else {
                // First time running.
                mBellBoy.start();
            }
        }

        setStartPauseButtonState(mBellBoy.isRunning() ?  StartPauseButtonState.PAUSE : StartPauseButtonState.START);
    }

    private void updateRound(String text) {
        TextView textView = (TextView) this.findViewById(R.id.txtRound);
        textView.setText(text.toString());
    }

    private void updateTime(int secondsUntilFinished) {
        Date date = new Date(secondsUntilFinished * 1000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        String formattedDate = dateFormat.format(date);

        TextView textView = (TextView) this.findViewById(R.id.txtTime);
        textView.setText(formattedDate.toString());
    }

    private void setStartPauseButtonState(StartPauseButtonState state) {
        Button button = (Button) this.findViewById(R.id.btnStartPause);
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

    private void resetTimer() {
        mBellBoy.reset();
        init(mConfiguration, mBellBoy);
    }

    private void init(Configuration configuration, BellBoy bellBoy) {
        updateRound("Round 1 of " + configuration.getNumRounds());
        updateTime(configuration.getRoundDurationInSeconds());

        setStartPauseButtonState(bellBoy.isRunning() ?  StartPauseButtonState.PAUSE : StartPauseButtonState.START);
    }

    private Configuration buildConfiguration() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String numRoundsStr = sharedPref.getString("num_rounds", "12");
        int numRounds = Integer.parseInt(numRoundsStr);

        String roundDurationStr = sharedPref.getString("round_duration", "180");
        int roundDuration = Integer.parseInt(roundDurationStr);

        String nSecondsBeforeRoundEndStr = sharedPref.getString("round_end_warning", "10");
        int nSecondsBeforeRoundEnd = Integer.parseInt(nSecondsBeforeRoundEndStr);

        String breakDurationStr = sharedPref.getString("break_duration", "60");
        int breakDuration = Integer.parseInt(breakDurationStr);

        String nSecondsBeforeBreakEndStr = sharedPref.getString("break_end_warning", "10");
        int nSecondsBeforeBreakEnd = Integer.parseInt(nSecondsBeforeBreakEndStr);

        return new Configuration(numRounds, roundDuration, nSecondsBeforeRoundEnd, breakDuration, nSecondsBeforeBreakEnd);
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
                updateRound("Round " + currentRound + " of " + mConfiguration.getNumRounds());
                mStartBellPlayer.start();
            }

            @Override
            public void onRoundTick(int secondsUntilFinished) {
                updateTime(secondsUntilFinished - 1);
            }

            @Override
            public void onNSecondsBeforeEndRound(int currentSecond) {
                System.out.println("NSecondsBeforeEndRound called. Current second is " + currentSecond + ".");
                mRoundEndWarningSoundPlayer.start();
            }

            @Override
            public void onEndRound(int currentRound) {
                mEndBellPlayer.start();
            }

            @Override
            public void onStartBreak() {
                updateRound("Break");
            }

            @Override
            public void onBreakTick(int secondsUntilFinished) {
                updateTime(secondsUntilFinished - 1);
            }

            @Override
            public void onNSecondsBeforeEndBreak(int currentSecond) {
                mBreakEndWarningSoundPlayer.start();
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
