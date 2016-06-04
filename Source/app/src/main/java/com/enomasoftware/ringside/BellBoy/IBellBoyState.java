package com.enomasoftware.ringside.BellBoy;

/**
 * Created by kirk on 28/05/16.
 */
public interface IBellBoyState {
    void start();
    void pause();
    boolean canBeResumed();
    void resume();
    void reset();
}
