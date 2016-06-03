package com.enomasoftware.ringside;

/**
 * Created by kirk on 20/05/16.
 */
public interface IEvents {
    void onStart();
    void onEnd();

    void onStartRound(int currentRound);
    void onRoundTick(int secondsUntilFinished);
    void onNSecondsBeforeEndRound(int currentSecond);
    void onEndRound(int currentRound);

    void onStartBreak();
    void onBreakTick(int secondsUntilFinished);
    void onNSecondsBeforeEndBreak(int currentSecond);
    void onEndBreak();

}
