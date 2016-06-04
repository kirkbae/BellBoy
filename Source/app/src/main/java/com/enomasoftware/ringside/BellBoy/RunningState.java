package com.enomasoftware.ringside.BellBoy;

/**
 * Created by kirk on 28/05/16.
 */
public class RunningState implements IBellBoyState {
    private BellBoy mBellBoy = null;

    public RunningState(BellBoy bellBoy) {
        mBellBoy = bellBoy;
    }

    @Override
    public void start() {
        mBellBoy.onStart();
    }

    @Override
    public void pause() {
        mBellBoy.onPause();
        mBellBoy.setState(new PausedState(mBellBoy));
    }

    @Override
    public boolean canBeResumed() {
        return false;
    }

    @Override
    public void resume() {
        throw new IllegalStateException("pause() must be called first in order to resume.");
    }

    @Override
    public void reset() {
        mBellBoy.setState(new PausedState(mBellBoy));
        // Todo: a bit of hack. onReset must be called after setState because we set prevState to null.
        mBellBoy.onReset();
    }
}
