package com.enomasoftware.roundgirl;

import android.os.CountDownTimer;

/**
 * Created by kirk on 21/05/16.
 */
public class BreakCountDownTimer extends CountDownTimer {
    BellBoy mRoundCountDownTimer;

    public BreakCountDownTimer(BellBoy roundCountDownTimer, int breakDurationInSeconds) {
        super(breakDurationInSeconds * 1000, 1000);
        mRoundCountDownTimer = roundCountDownTimer;
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        mRoundCountDownTimer.start();
    }
}
