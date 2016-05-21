package com.enomasoftware.roundgirl;

/**
 * Created by kirk on 20/05/16.
 */
public interface IEvents {
    void onStart();
    void onEnd();

    void onStartRound(int currentRound);
    void onNSecondsBeforeEndRound(int currentSecond);
    void onEndRound(int currentRound);

    void onStartBreak();
    void onEndBreak();
}
