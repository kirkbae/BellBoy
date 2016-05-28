package com.enomasoftware.roundgirl.States;

import com.enomasoftware.roundgirl.MainActivity;

/**
 * Created by kirk on 26/05/16.
 */
public class StartRoundState implements IState {

    @Override
    public void render(MainActivity mainActivity, Object data) {
        String text = "Round " + data.toString();
        mainActivity.UpdateText(text);

        mainActivity.setStartButtonEnabled(false);
    }
}
