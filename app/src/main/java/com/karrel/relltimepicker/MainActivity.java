package com.karrel.relltimepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.karrel.timepicker.RellTimePicker;

import static com.karrel.relltimepicker.R.id.setTime;

public class MainActivity extends AppCompatActivity {

    private Button mSetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSetTime = (Button) findViewById(setTime);
        mSetTime.setOnClickListener(onSetTimeListener);
    }

    private View.OnClickListener onSetTimeListener = new View.OnClickListener() {
        public RellTimePicker.OnTimePickListener onTimeSelectListener = new RellTimePicker.OnTimePickListener() {
            @Override
            public void onTimePick(int hourOfDay, int minute) {
                mSetTime.setText(String.format("hourOfDay : %s, minute : %s", hourOfDay, minute));
            }
        };

        @Override
        public void onClick(View v) {
            RellTimePicker picker = new RellTimePicker.Builder(MainActivity.this)
                    .setTime(10, 30)
                    .create();

            picker.show(getSupportFragmentManager(), onTimeSelectListener);
        }
    };
}
