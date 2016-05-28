package com.enomasoftware.roundgirl.States;

import com.enomasoftware.roundgirl.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kirk on 28/05/16.
 */
public class BreakState implements IState {
    @Override
    public void render(MainActivity mainActivity, Object data) {
        // Todo: duplicate code with RoundState. Refactor.
        Integer secondsUntilFinished = (Integer)data;
        Date date = new Date(secondsUntilFinished * 1000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        String formattedDate = dateFormat.format(date);

        mainActivity.UpdateText(formattedDate);
    }
}
