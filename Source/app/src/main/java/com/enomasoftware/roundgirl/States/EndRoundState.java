package com.enomasoftware.roundgirl.States;

import com.enomasoftware.roundgirl.MainActivity;

/**
 * Created by kirk on 26/05/16.
 */
public class EndRoundState implements IState {
    @Override
    public void render(MainActivity mainActivity, Object data) {
        System.out.println("EndRoundState.render() called.");
    }
}
