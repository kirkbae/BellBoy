package com.enomasoftware.ringside;

/**
 * Created by kirk on 20/05/16.
 */
public class Configuration
{
    private int mNumRounds = 12;
    private int mRoundDurationInSeconds = 180;
    private int mNSecondsBeforeRoundEnd = 10;
    private int mBreakDurationInSeconds = 60;
    private int mNSecondsBeforeBreakEnd = 10;

    public int getNumRounds(){
        return mNumRounds;
    }

    public int getRoundDurationInSeconds(){
        return mRoundDurationInSeconds;
    }

    public int getNSecondsBeforeRoundEnd(){
        return mNSecondsBeforeRoundEnd;
    }

    public int getBreakDurationInSeconds(){
        return mBreakDurationInSeconds;
    }

    public int getNSecondsBeforeBreakEnd(){
        return mNSecondsBeforeBreakEnd;
    }

    public Configuration(int numRounds,
                         int roundDurationInSeconds,
                         int nSecondsBeforeRoundEnd,
                         int breakDurationInSeconds,
                         int nSecondsBeforeBreakEnd) {
        mNumRounds = numRounds;
        mRoundDurationInSeconds = roundDurationInSeconds;
        mNSecondsBeforeRoundEnd = nSecondsBeforeRoundEnd;
        mBreakDurationInSeconds = breakDurationInSeconds;
        mNSecondsBeforeBreakEnd = nSecondsBeforeBreakEnd;
    }
}