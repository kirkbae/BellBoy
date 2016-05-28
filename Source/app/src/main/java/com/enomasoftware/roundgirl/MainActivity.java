package com.enomasoftware.roundgirl;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enomasoftware.roundgirl.States.StateContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Configuration mConfiguration = null;
    private BellBoy mBellBoy = null;

    private StateContext mStateContext = new StateContext(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnStart_onClick(View view) {
        mConfiguration = buildConfiguration();
        mBellBoy = buildBellBoy(mConfiguration, mStateContext);

        mBellBoy.start();
    }

    public void UpdateText(String text) {
        TextView textView = (TextView) this.findViewById(R.id.txtMain);
        textView.setText(text.toString());
    }

    public void UpdateTime(int secondsUntilFinished) {
        Date date = new Date(secondsUntilFinished * 1000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        String formattedDate = dateFormat.format(date);

        UpdateText(formattedDate);
    }

    public void setStartButtonEnabled(boolean enabled) {
        Button btn = (Button) this.findViewById(R.id.btnStart);
        btn.setEnabled(enabled);
    }

    private Configuration buildConfiguration() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String numRoundsStr = sharedPref.getString("num_rounds", "12");
        int numRounds = Integer.parseInt(numRoundsStr);
        String roundDurationStr = sharedPref.getString("round_duration", "180");
        int roundDuration = Integer.parseInt(roundDurationStr);
        String breakDurtaionStr = sharedPref.getString("break_duration", "60");
        int breakDurtaion = Integer.parseInt(breakDurtaionStr);

        return new Configuration(numRounds, roundDuration, 10, breakDurtaion);
    }

    private BellBoy buildBellBoy(Configuration configuration, IEvents events) {
        return new BellBoy(configuration, events);
    }
}
