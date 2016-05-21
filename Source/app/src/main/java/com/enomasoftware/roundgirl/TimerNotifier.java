package com.enomasoftware.roundgirl;

import android.os.CountDownTimer;

/**
 * Created by kirk on 20/05/16.
 */

public class TimerNotifier {
    private Configuration mConfiguration;
    private IEvents mEvents;

    private BellBoy mRoundCountDownTimer;
    private CountDownTimer mBreakCountDownTimer;

    // Since this is an int, and since only one thread will update it at any given time,
    // Double check if writing int variable is an atomic operation.
    // If not, we need to synchronize access to this variable.
    private int mCurrentRound = 0;

    public TimerNotifier(Configuration configuration, IEvents events) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannot be null");

        }
        mConfiguration = configuration;

        if (events == null) {
            throw new IllegalArgumentException("Events cannot be null");
        }
        mEvents = events;
    }

    public void start()
    {
        mEvents.onStart();
        //mRoundCountDownTimer = new BellBoy(mConfiguration.getRoundDurationInSeconds(), mConfiguration.getNSecondsBeforeRoundEnd(), mEvents);
        mRoundCountDownTimer.start();
    }

    /*
    private CountDownTimer startRoundCountDownTimer()
    {
        // Todo: We may need to lock before upadting this variable as this gets called from BreakCountDownTimer's onFinish() method.
        // We increment the round when it starts.
        ++mCurrentRound;
        mEvents.onStartRound();

        return new CountDownTimer(mConfiguration.getRoundDurationInSeconds() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                long secondsRemaininig = millisUntilFinished / 1000;
                if (secondsRemaininig == mConfiguration.getNSecondsBeforeRoundEnd()) {
                    mEvents.onNSecondsBeforeEndRound();
                }
            }
            @Override
            public void onFinish() {
                mEvents.onEndRound();

                if (mCurrentRound == mConfiguration.getNumRounds())
                {
                    mEvents.onEnd();
                }
                else
                {
                    mBreakCountDownTimer = startBreakCountDownTimer();
                    mBreakCountDownTimer.start();
                }
            }
        };
    }

    private CountDownTimer startBreakCountDownTimer()
    {
        mEvents.onStartBreak();

        return new CountDownTimer(mConfiguration.getBreakDurationInSeconds() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }
            @Override
            public void onFinish() {
                mEvents.onEndBreak();
                // see if this works
                // mRoundCountDownTimer.start
                mRoundCountDownTimer = startRoundCountDownTimer();
            }
        };
    }
    */
}

