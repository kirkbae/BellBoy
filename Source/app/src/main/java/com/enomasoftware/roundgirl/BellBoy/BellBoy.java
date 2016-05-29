package com.enomasoftware.roundgirl.BellBoy;

import android.os.CountDownTimer;

import com.enomasoftware.roundgirl.Configuration;
import com.enomasoftware.roundgirl.IEvents;

/**
 * Created by kirk on 20/05/16.
 */
public class BellBoy {
    private static final long COUNTDOWNTIMER_BUFFER = 500;
    private Configuration mConfiguration;
    private IEvents mEvents;
    private boolean mHasStarted = false;
    private int mCurrentRound = 0;
    // This may need to be thread safe.
    private int mCurrentRoundSecond = 0;
    private int mCurrentBreakSecond = 0;

    private CountDownTimer mRoundCountDownTimer = null;
    private CountDownTimer mBreakCountDownTimer = null;

    public BellBoy(Configuration configuration, IEvents events) {
        mConfiguration = configuration;
        mEvents = events;
    }

    public void start() {
        mHasStarted = true;
        mRoundCountDownTimer = buildRoundCountDownTimer(mConfiguration.getRoundDurationInSeconds());
        mRoundCountDownTimer.start();
    }

    public void pause() {
        mHasStarted = false;

        if (mRoundCountDownTimer != null) {
            mRoundCountDownTimer.cancel();
            mRoundCountDownTimer = null;
        }
        if (mBreakCountDownTimer != null) {
            mBreakCountDownTimer.cancel();
            mBreakCountDownTimer = null;
        }
    }

    public void resume() {
        if (mHasStarted == false) {
            throw new IllegalStateException("resume() can be called only after it has been paused.");
        }
        mHasStarted = true;

        if (mCurrentRoundSecond != 0) {
            mRoundCountDownTimer = buildRoundCountDownTimer(mCurrentRoundSecond);
            mRoundCountDownTimer.start();
        } else if (mCurrentBreakSecond != 0) {
            mBreakCountDownTimer = buildBreakCountDownTimer(mCurrentBreakSecond);
            mBreakCountDownTimer.start();
        } else {
            System.out.println("resume() is called but both the mCurrentRoundSecond and mCurrentBreakSecond are zero.");
        }
    }

    public boolean getHasStarted() {
        return mHasStarted;
    }

    private CountDownTimer buildRoundCountDownTimer(int durationInSeconds) {
        // Todo: We may need to lock before updating this variable as this gets called from BreakCountDownTimer's onFinish() method.
        // We increment the round when it starts.
        ++mCurrentRound;
        mEvents.onStartRound(mCurrentRound);

        return new CountDownTimer(durationInSeconds * 1000 + COUNTDOWNTIMER_BUFFER, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsUntilFinished = (int)Math.ceil(millisUntilFinished / 1000);

                System.out.println("onTick " + millisUntilFinished);
                System.out.println("onTick seconds " + secondsUntilFinished);
                mEvents.onRoundTick(secondsUntilFinished);
                mCurrentRoundSecond = mConfiguration.getRoundDurationInSeconds() - secondsUntilFinished;

                if (secondsUntilFinished == mConfiguration.getNSecondsBeforeRoundEnd()) {
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
                    mBreakCountDownTimer = buildBreakCountDownTimer(mConfiguration.getBreakDurationInSeconds());
                    mBreakCountDownTimer.start();
                }
            }
        };
    }

    private CountDownTimer buildBreakCountDownTimer(int durationInSeconds) {
        mEvents.onStartBreak();

        return new CountDownTimer(durationInSeconds * 1000 + COUNTDOWNTIMER_BUFFER, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsUntilFinished = (int)Math.ceil(millisUntilFinished / 1000);
                mCurrentBreakSecond = mConfiguration.getBreakDurationInSeconds() - secondsUntilFinished;

                mEvents.onBreakTick(secondsUntilFinished);
            }
            @Override
            public void onFinish() {
                mEvents.onEndBreak();
                mRoundCountDownTimer = buildRoundCountDownTimer(mConfiguration.getRoundDurationInSeconds());
                mRoundCountDownTimer.start();
            }
        };
    }
}
