package com.enomasoftware.roundgirl.States;

import com.enomasoftware.roundgirl.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kirk on 26/05/16.
 */
public class RoundState implements IState {
    @Override
    public void render(MainActivity mainActivity, Object data) {
        mainActivity.UpdateTime((int)data);
    }
}
