package com.enomasoftware.roundgirl.States;

import com.enomasoftware.roundgirl.IEvents;
import com.enomasoftware.roundgirl.MainActivity;

/**
 * Created by kirk on 26/05/16.
 */
public class StartSessionState implements IState {

    @Override
    public void render(MainActivity mainActivity, Object data) {
        System.out.println("StartSessionState.render called.");
    }
}
