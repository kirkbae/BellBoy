package com.enomasoftware.roundgirl.States;

import com.enomasoftware.roundgirl.MainActivity;

/**
 * Created by kirk on 28/05/16.
 */
public class NSecondsBeforeEndRound implements IState {
    @Override
    public void render(MainActivity mainActivity, Object data) {
        System.out.println("NSecondsBeforeEndRound.render() called. Current second is " + data.toString());
    }
}
