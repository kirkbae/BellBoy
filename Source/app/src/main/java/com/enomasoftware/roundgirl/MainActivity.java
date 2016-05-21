package com.enomasoftware.roundgirl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Configuration mConfiguration = null;
    private IEvents mEvents = null;
    private BellBoy mBellBoy = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConfiguration = buildConfiguration();
        mEvents = buildEvents();
        mBellBoy = buildBellBoy(mConfiguration, mEvents);

        mBellBoy.start();
    }

    private Configuration buildConfiguration() {
        return new Configuration(3, 10, 3, 5);
    }

    private IEvents buildEvents() {
        return new IEvents() {
            @Override
            public void onStart()
            {
                System.out.println("onStart called.");
            }
            @Override
            public void onEnd()
            {
                System.out.println("onEnd called.");
            }
            @Override
            public void onStartRound(int currentRound) {
                System.out.println("onStartRound called. Current round is " + currentRound);
            }
            @Override
            public void onNSecondsBeforeEndRound(int currentSecond) {
                System.out.println("onNSecondsBeforeEndRound called. Current second is " + currentSecond);
            }
            @Override
            public void onEndRound(int currentRound) {
                System.out.println("onEndRound called. Current round is " + currentRound);
            }
            @Override
            public void onStartBreak()
            {
                System.out.println("onStartBreak called.");
            }
            @Override
            public void onEndBreak()
            {
                System.out.println("onEndBreak called.");
            }
        };
    }

    private BellBoy buildBellBoy(Configuration configuration, IEvents events) {
        return new BellBoy(configuration, events);
    }
}
