package com.enomasoftware.roundgirl;

/**
 * Created by kirk on 21/05/16.
 */
public class Session {
    private int mCurrentRound = 0;

    public void incrementRound() {
        ++mCurrentRound;
    }

    public int getCurrentRound() {
        return mCurrentRound;
    }

}
