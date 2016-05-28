package com.enomasoftware.roundgirl.States;

import com.enomasoftware.roundgirl.IEvents;
import com.enomasoftware.roundgirl.MainActivity;

/**
 * Created by kirk on 26/05/16.
 */
public interface IState {
    void render(MainActivity mainActivity, Object data);
}
