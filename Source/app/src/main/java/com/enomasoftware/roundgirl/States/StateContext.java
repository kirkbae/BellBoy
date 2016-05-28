package com.enomasoftware.roundgirl.States;

import com.enomasoftware.roundgirl.IEvents;
import com.enomasoftware.roundgirl.MainActivity;

/**
 * Created by kirk on 26/05/16.
 */
public class StateContext implements IEvents {
    private IState mState;
    private MainActivity mMainActivity;

    private StartSessionState mStartSessionState = new StartSessionState();
    private StartRoundState mStartRoundState = new StartRoundState();
    private RoundState mRoundState = new RoundState();
    private NSecondsBeforeEndRound mNSecondsBeforeEndRound = new NSecondsBeforeEndRound();
    private EndRoundState mEndRoundState = new EndRoundState();
    private StartBreakState mStartBreakState = new StartBreakState();
    private BreakState mBreakState = new BreakState();
    private EndBreakState mEndBreakState = new EndBreakState();

    public StateContext(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    private void setState(IState state, Object data) {
        mState = state;

        state.render(mMainActivity, data);
    }

    @Override
    public void onStart() {
        setState(mStartSessionState, null);
    }

    @Override
    public void onEnd() {
        System.out.println("onEnd called.");
    }

    @Override
    public void onStartRound(int currentRound) {
        setState(mStartRoundState, currentRound);
    }

    @Override
    public void onRoundTick(int secondsUntilFinished) {
        setState(mRoundState, secondsUntilFinished);
    }

    @Override
    public void onNSecondsBeforeEndRound(int currentSecond) {
        setState(mNSecondsBeforeEndRound, currentSecond);
    }

    @Override
    public void onEndRound(int currentRound) {
        setState(mEndRoundState, currentRound);
    }

    @Override
    public void onStartBreak() {
        setState(mStartBreakState, null);
    }

    @Override
    public void onBreakTick(int secondsUntilFinished) {
        setState(mBreakState, secondsUntilFinished);
    }

    @Override
    public void onEndBreak() {
        setState(mEndBreakState, null);
    }
}
