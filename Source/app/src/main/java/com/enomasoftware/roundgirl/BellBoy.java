package com.enomasoftware.roundgirl;

import android.os.CountDownTimer;

/**
 * Created by kirk on 20/05/16.
 */
public class BellBoy {
    private Configuration mConfiguration;
    private IEvents mEvents;
    private int mCurrentRound = 0;
    // This may need to be thread safe.
    private int mCurrentRoundSecond = 0;
    private int mCurrentBreakSecond = 0;

    public BellBoy(Configuration configuration, IEvents events)
    {
        mConfiguration = configuration;
        mEvents = events;
    }

    public void start()
    {
        startRoundCountDownTimer();
    }

    private CountDownTimer startRoundCountDownTimer()
    {
        // Todo: We may need to lock before updating this variable as this gets called from BreakCountDownTimer's onFinish() method.
        // We increment the round when it starts.
        ++mCurrentRound;
        mEvents.onStartRound(mCurrentRound);

        return new CountDownTimer(mConfiguration.getRoundDurationInSeconds() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                int secondsRemaininig = (int) (millisUntilFinished / 1000);
                mCurrentRoundSecond = mConfiguration.getRoundDurationInSeconds() - secondsRemaininig;

                if (secondsRemaininig == mConfiguration.getNSecondsBeforeRoundEnd()) {
                    mEvents.onNSecondsBeforeEndRound(mCurrentRoundSecond);
                }
            }
            @Override
            public void onFinish() {
                mEvents.onEndRound(mCurrentRound);

                if (mCurrentRound >= mConfiguration.getNumRounds())
                {
                    mEvents.onEnd();
                }
                else
                {
                    startBreakCountDownTimer();
                }
            }
        }.start();
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
                startRoundCountDownTimer();
            }
        }.start();
    }


    /*
    @Override
    public void onTick(long millisUntilFinished) {
        //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
        int secondsRemaininig = (int) (millisUntilFinished / 1000);
        if (secondsRemaininig == mConfiguration.getNSecondsBeforeRoundEnd()) {
            mEvents.onNSecondsBeforeEndRound();
        }
        mCurrentSecond = mConfiguration.getRoundDurationInSeconds() - secondsRemaininig;
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
    */
}
