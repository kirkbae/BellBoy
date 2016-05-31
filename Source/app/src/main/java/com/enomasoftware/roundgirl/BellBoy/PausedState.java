package com.enomasoftware.roundgirl.BellBoy;

/**
 * Created by kirk on 28/05/16.
 */
public class PausedState implements IBellBoyState {
    private BellBoy mBellBoy = null;

    public PausedState(BellBoy bellBoy) {
        mBellBoy = bellBoy;
    }

    @Override
    public void start() {
        mBellBoy.onStart();
        mBellBoy.setState(new RunningState(mBellBoy));
    }

    @Override
    public void pause() {
        throw new IllegalStateException("It is already in paused state.");
    }

    @Override
    public boolean canBeResumed() {
        return mBellBoy.getPrevBellBoyState() instanceof RunningState;
    }

    @Override
    public void resume() {
        mBellBoy.onResume();
        mBellBoy.setState(new RunningState(mBellBoy));
    }
}
