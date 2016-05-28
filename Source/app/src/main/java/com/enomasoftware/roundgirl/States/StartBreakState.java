package com.enomasoftware.roundgirl.States;

import com.enomasoftware.roundgirl.MainActivity;

/**
 * Created by kirk on 28/05/16.
 */
public class StartBreakState implements IState {
    @Override
    public void render(MainActivity mainActivity, Object data) {
        String text = "Break";
        mainActivity.UpdateText(text);
    }
}
