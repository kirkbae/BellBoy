package com.enomasoftware.roundgirl.BellBoy;

import android.os.CountDownTimer;

import com.enomasoftware.roundgirl.Configuration;
import com.enomasoftware.roundgirl.IEvents;

/**
 * Created by kirk on 20/05/16.
 */
public class BellBoy {
    private static final long COUNTDOWNTIMER_BUFFER = 500;

    public Configuration getConfiguration() {
        return mConfiguration;
    }
    private Configuration mConfiguration;

    private IEvents mEvents;
    private int mCurrentRound = 0;
    // This may need to be thread safe.
    private int mCurrentRoundSecond = 0;
    private int mCurrentBreakSecond = 0;

    private CountDownTimer mRoundCountDownTimer = null;
    private CountDownTimer mBreakCountDownTimer = null;

    public IBellBoyState getPrevBellBoyState() {
        return mPrevBellBoyState;
    }
    private IBellBoyState mPrevBellBoyState = null;
    private IBellBoyState mBellBoyState = null;

    public BellBoy(Configuration configuration, IEvents events) {
        mConfiguration = configuration;
        mEvents = events;
        setState(new PausedState(this));
    }

    public void start() {
        mBellBoyState.start();
    }

    public void onStart() {
        mEvents.onStart();

        ++mCurrentRound;
        mEvents.onStartRound(mCurrentRound);
        mRoundCountDownTimer = buildRoundCountDownTimer(mConfiguration.getRoundDurationInSeconds());
        mRoundCountDownTimer.start();
    }

    public void pause() {
        mBellBoyState.pause();
    }

    public void onPause() {
        if (mRoundCountDownTimer != null) {
            mRoundCountDownTimer.cancel();
            mRoundCountDownTimer = null;
        }
        if (mBreakCountDownTimer != null) {
            mBreakCountDownTimer.cancel();
            mBreakCountDownTimer = null;
        }
    }

    public boolean canBeResumed() {
        return mBellBoyState.canBeResumed();
    }

    public void resume() {
        mBellBoyState.resume();
    }

    public void onResume() {
        if (mCurrentRoundSecond != 0) {
            mRoundCountDownTimer = buildRoundCountDownTimer(mConfiguration.getRoundDurationInSeconds() - mCurrentRoundSecond);
            mRoundCountDownTimer.start();
        } else if (mCurrentBreakSecond != 0) {
            mBreakCountDownTimer = buildBreakCountDownTimer(mConfiguration.getBreakDurationInSeconds() - mCurrentBreakSecond);
            mBreakCountDownTimer.start();
        } else {
            System.out.println("resume() is called but both the mCurrentRoundSecond and mCurrentBreakSecond are zero.");
        }
    }

    public boolean isRunning() {
        return mBellBoyState instanceof RunningState;
    }

    public void setState(IBellBoyState bellBoyState) {
        mPrevBellBoyState = mBellBoyState;
        mBellBoyState = bellBoyState;
    }

    private CountDownTimer buildRoundCountDownTimer(int durationInSeconds) {
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
                    mEvents.onStartBreak();
                    mBreakCountDownTimer = buildBreakCountDownTimer(mConfiguration.getBreakDurationInSeconds());
                    mBreakCountDownTimer.start();
                }
            }
        };
    }

    private CountDownTimer buildBreakCountDownTimer(int durationInSeconds) {
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

                ++mCurrentRound;
                mEvents.onStartRound(mCurrentRound);

                mRoundCountDownTimer = buildRoundCountDownTimer(mConfiguration.getRoundDurationInSeconds());
                mRoundCountDownTimer.start();
            }
        };
    }
}
