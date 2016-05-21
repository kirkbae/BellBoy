package com.enomasoftware.roundgirl;

import android.os.CountDownTimer;

/**
 * Created by kirk on 20/05/16.
 */
public class BellBoy {
    private static final long COUNTDOWNTIMER_BUFFER = 500;
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

        return new CountDownTimer(mConfiguration.getRoundDurationInSeconds() * 1000 + COUNTDOWNTIMER_BUFFER, 1000) {
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
                    startBreakCountDownTimer();
                }
            }
        }.start();
    }

    private CountDownTimer startBreakCountDownTimer()
    {
        mEvents.onStartBreak();

        return new CountDownTimer(mConfiguration.getBreakDurationInSeconds() * 1000 + COUNTDOWNTIMER_BUFFER, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsUntilFinished = (int)Math.ceil(millisUntilFinished / 1000);
                mEvents.onBreakTick(secondsUntilFinished);
            }
            @Override
            public void onFinish() {
                mEvents.onEndBreak();
                startRoundCountDownTimer();
            }
        }.start();
    }
}
