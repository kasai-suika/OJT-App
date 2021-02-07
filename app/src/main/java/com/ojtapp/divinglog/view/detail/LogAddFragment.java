package com.ojtapp.divinglog.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.controller.WriteAsyncTask;
import com.ojtapp.divinglog.view.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LogAddFragment extends Fragment {
    private static final String TAG = LogActivity.class.getSimpleName();
    public static final int NO_DATA = -1;

    private Button makeTaskButton;
    private EditText diveNumber;
    private EditText place;
    private EditText point;
    private EditText depthMax;
    private EditText depthAve;
    private EditText airStart;
    private EditText airEnd;
    private EditText weather;
    private EditText temp;
    private EditText tempWater;
    private EditText visibility;
    private EditText memberNavigate;
    private EditText member;
    private EditText memo;
    private DatePicker date;
    private TimePicker timeStart;
    private TimePicker timeEnd;

    public static Fragment newInstance() {
        android.util.Log.d(TAG, "newInstance()");
        return new LogAddFragment();
    }

    /**
     * 作成画面のViewを作成
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStat) {
        android.util.Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_add_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStat) {
        super.onViewCreated(view, savedInstanceStat);
        android.util.Log.d(TAG, "onViewCreated");

        // 変数とレイアウトを紐づけ
        linkVariable(view);

        makeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");

                //---------DivingLogクラスにセット------------
                DivingLog divingLog = new DivingLog();
                setDateToDivingLog(divingLog);

                // -----【DB】保存処理--------------
                WriteAsyncTask registerAsyncTask = new WriteAsyncTask(requireContext());

                // コールバック処理を設定
                registerAsyncTask.setOnCallBack(new WriteAsyncTask.RegisterCallback() {

                    @Override
                    public void onRegister(Boolean result) {
                        // --------最初の画面へ戻る処理------
                        // 情報をintentに詰める
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        // 指定したアクティビティより上のViewを削除
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                // 保存処理開始
                registerAsyncTask.execute(divingLog);
            }
        });
    }

    private void linkVariable(View view) {
        makeTaskButton = view.findViewById(R.id.button_make_task);
        diveNumber = view.findViewById(R.id.add_dive_number);
        place = view.findViewById(R.id.add_place);
        point = view.findViewById(R.id.add_point);
        depthMax = view.findViewById(R.id.add_depth_max);
        depthAve = view.findViewById(R.id.add_depth_ave);
        airStart = view.findViewById(R.id.add_air_start);
        airEnd = view.findViewById(R.id.add_air_end);
        weather = view.findViewById(R.id.add_weather);
        temp = view.findViewById(R.id.add_temp);
        tempWater = view.findViewById(R.id.add_temp_water);
        visibility = view.findViewById(R.id.add_visibility);
        member = view.findViewById(R.id.add_member);
        memberNavigate = view.findViewById(R.id.add_navi);
        memo = view.findViewById(R.id.add_memo);
        date = view.findViewById(R.id.add_date);
        timeStart = view.findViewById(R.id.add_time_start);
        timeEnd = view.findViewById(R.id.add_time_end);
    }

    public void setDateToDivingLog(DivingLog divingLog) {
        // 本数
        divingLog.setDiveNumber(Integer.parseInt(diveNumber.getText().toString()));

        // 場所
        divingLog.setPlace(place.getText().toString());

        // ポイント
        divingLog.setPoint(point.getText().toString());

        // 深度（最大）
        divingLog.setDepthMax(getIntData(depthMax.getText().toString()));

        // 深度（平均）
        divingLog.setDepthAve(getIntData(depthAve.getText().toString()));

        // 残圧（開始時）
        int airStartInt = getIntData(airStart.getText().toString());
        divingLog.setAirStart(airStartInt);

        // 残圧（終了時）
        int airEndInt = getIntData(airEnd.getText().toString());
        divingLog.setAirEnd(airEndInt);

        // 使用した空気
        if ((NO_DATA == airStartInt) || (NO_DATA == airEndInt)) {
            divingLog.setAirDive(NO_DATA);
        } else {
            int airDive = airStartInt - airEndInt;
            divingLog.setAirDive(airDive);
        }

        // 天気
        divingLog.setWeather(weather.getText().toString());

        // 気温
        divingLog.setTemp(getIntData(temp.getText().toString()));

        // 水温
        divingLog.setTempWater(getIntData(tempWater.getText().toString()));

        // 透明度
        divingLog.setVisibility(getIntData(visibility.getText().toString()));

        // メンバー
        divingLog.setMember(member.getText().toString());

        // ナビ
        divingLog.setMemberNavigate(memberNavigate.getText().toString());

        // メモ
        divingLog.setMemo(memo.getText().toString());

        // 日付
        int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year = date.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day); // 日付をカレンダークラスにセット
        SimpleDateFormat dateFormat = new SimpleDateFormat(LogActivity.FORMAT_DATE, Locale.JAPAN);
        divingLog.setDate(dateFormat.format(calendar.getTime()));   // フォーマットを指定してDivingLogクラスにセット

        // 開始時間
        int hourStart = timeStart.getHour();
        int minuteStart = timeStart.getMinute();
        calendar.set(Calendar.HOUR, hourStart);
        calendar.set(Calendar.MINUTE, minuteStart);
        SimpleDateFormat timeFormat = new SimpleDateFormat(LogActivity.FORMAT_TIME, Locale.JAPAN);
        divingLog.setTimeStart(timeFormat.format(calendar.getTime()));
        Log.d(TAG, "start =" + timeFormat.format(calendar.getTime()));

        // 終了時間
        int hourEnd = timeEnd.getHour();
        int minuteEnd = timeEnd.getMinute();
        calendar.set(Calendar.HOUR, hourEnd);
        calendar.set(Calendar.MINUTE, minuteEnd);
        divingLog.setTimeEnd(timeFormat.format(calendar.getTime()));
        Log.d(TAG, "end =" + timeFormat.format(calendar.getTime()));

        // 潜水時間
        int hour = (hourEnd - hourStart);
        int minute;
        if (minuteEnd < minuteStart) {
            minute = minuteEnd + 60 - minuteStart;
            hour = hour - 1;
        } else {
            minute = minuteEnd - minuteStart;
        }
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        divingLog.setTimeDive(timeFormat.format(calendar.getTimeInMillis()));
    }

    public static int getIntData(String strData) {
        if (0 == strData.length()) {
            return NO_DATA;
        } else {
            return Integer.parseInt(strData);
        }
    }
}