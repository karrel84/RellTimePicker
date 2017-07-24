package com.karrel.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 이주영 on 2017-06-01.
 */

public class RellTimePicker extends AppCompatDialogFragment {

    private final String TAG = "RellTimePicker";

    public interface OnTimePickListener {
        void onTimePick(int hourOfDay, int minute);
    }

    private Builder mBuilder;
    private OnTimePickListener mOnDatePickListener;
    private android.widget.TimePicker mTimePicker;
    private View mRoot;

    public int hourOfDay = -1;
    public int minute = -1;

    public RellTimePicker(Builder builder) {
        mBuilder = builder;
    }

    public void show(FragmentManager fragmentManager, OnTimePickListener onDatePickListener) {
        if (onDatePickListener == null)
            throw new NullPointerException("onDatePickListener must be not null");

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, getTag());
        ft.commitAllowingStateLoss();

        mOnDatePickListener = onDatePickListener;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        setupDialogSetting(dialog);
        setupLayout(dialog);
        setupButtonEvents();
    }

    /**
     * 레이아웃 세팅
     */
    private void setupLayout(Dialog dialog) {
        mRoot = LayoutInflater.from(mBuilder.mContext).inflate(R.layout.time_picker, null, false);
        mTimePicker = (android.widget.TimePicker) mRoot.findViewById(R.id.timePicker);
        dialog.setContentView(mRoot);

        setupTimePicker();
    }

    /**
     * 타임피커 세팅
     */
    private void setupTimePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setMinute(mBuilder.minute);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(mBuilder.hour);
        }
        mTimePicker.setOnTimeChangedListener(new android.widget.TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(android.widget.TimePicker view, int hourOfDay, int minute) {
                Log.d(TAG, String.format("hourOfDay : %s , minute : %s", hourOfDay, minute));
                RellTimePicker.this.hourOfDay = hourOfDay;
                RellTimePicker.this.minute = minute;
            }
        });
    }

    /**
     * 다이얼로그 셋팅
     */
    private void setupDialogSetting(Dialog dialog) {
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 확인, 취소 버튼 세팅
     */
    private void setupButtonEvents() {
        mRoot.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDatePickListener.onTimePick(getHour(), getMinute());
                dismiss();
            }
        });
        mRoot.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private int getHour() {
        if (hourOfDay == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hourOfDay = mTimePicker.getHour();
            } else {
                hourOfDay = mTimePicker.getCurrentHour();
            }
        }
        return hourOfDay;
    }

    private int getMinute() {
        if (minute == -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                minute = mTimePicker.getMinute();
            } else {
                minute = mTimePicker.getCurrentMinute();
            }
        }
        return minute;
    }

    /**
     * 빌더 클래스
     */
    public static class Builder {
        public Context mContext;
        public int hour = -1;
        public int minute = -1;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTime(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
            return this;
        }

        public RellTimePicker create() {
            RellTimePicker picker = new RellTimePicker(this);
            return picker;
        }
    }
}
