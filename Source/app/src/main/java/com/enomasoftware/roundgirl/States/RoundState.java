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
        Integer secondsUntilFinished = (Integer)data;
        Date date = new Date(secondsUntilFinished * 1000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        String formattedDate = dateFormat.format(date);

        mainActivity.UpdateText(formattedDate);
    }
}
