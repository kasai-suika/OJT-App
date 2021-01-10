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
import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.controller.RegisterAsyncTask;
import com.ojtapp.divinglog.view.main.MainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskAddFragment extends Fragment {
    private static final String TAG = TaskActivity.class.getSimpleName();

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

    public TaskAddFragment() {
    }

    public static Fragment newInstance() {
        android.util.Log.d(TAG, "newInstance()");
        return new TaskAddFragment();
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
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStat) {
        super.onViewCreated(view, savedInstanceStat);
        android.util.Log.d(TAG, "onViewCreated");
        // 変数とレイアウトを紐づけ
        Button makeTaskButton = view.findViewById(R.id.button_make_task);

        // クリックされた時の動作を紐づける
        makeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.d(TAG, "onClick");

                //---------DivingLogクラスにセット------------
                DivingLog divingLog = new DivingLog();

                // 本数
                diveNumber = v.findViewById(R.id.add_dive_number);
                String test = diveNumber.getText().toString();
                Log.d(TAG, "test = " +test);
                divingLog.setDiveNumber(Integer.parseInt(diveNumber.getText().toString()));

                // 場所
                place = v.findViewById(R.id.add_place);
                divingLog.setPlace(place.getText().toString());

                // ポイント
                point = v.findViewById(R.id.add_point);
                divingLog.setPoint(point.getText().toString());

                // 深度（最大）
                depthMax = v.findViewById(R.id.add_depth_max);
                divingLog.setDepthMax(Integer.parseInt(depthMax.getText().toString()));

                // 深度（平均）
                depthAve = v.findViewById(R.id.add_depth_ave);
                divingLog.setDepthAve(Integer.parseInt(depthAve.getText().toString()));

                // 残圧（開始時）
                airStart = v.findViewById(R.id.add_air_start);
                int airStartInt = Integer.parseInt(airStart.getText().toString());
                divingLog.setAirStart(airStartInt);

                // 残圧（終了時）
                airEnd = v.findViewById(R.id.add_air_end);
                int airEndInt = Integer.parseInt(airEnd.getText().toString());
                divingLog.setAirEnd(airEndInt);

                // 使用した空気
                int airDive = airStartInt - airEndInt;
                divingLog.setAirDive(airDive);

                // 天気
                weather = v.findViewById(R.id.add_weather);
                divingLog.setWeather(weather.getText().toString());

                // 気温
                temp = v.findViewById(R.id.add_temp);
                divingLog.setTemp(Integer.parseInt(temp.getText().toString()));

                // 水温
                tempWater = v.findViewById(R.id.add_temp_water);
                divingLog.setTempWater(Integer.parseInt(tempWater.getText().toString()));

                // 透明度
                visibility = v.findViewById(R.id.add_visibility);
                divingLog.setVisibility(Integer.parseInt(visibility.getText().toString()));

                // メンバー
                member = v.findViewById(R.id.add_member);
                divingLog.setMember(member.getText().toString());

                // ナビ
                memberNavigate = v.findViewById(R.id.add_navi);
                divingLog.setMemberNavigate(memberNavigate.getText().toString());

                // メモ
                memo = v.findViewById(R.id.add_memo);
                divingLog.setMemo(memo.getText().toString());

                // 日付
                date = v.findViewById(R.id.add_date);
                int day = date.getDayOfMonth();
                int month = date.getMonth();
                int year = date.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day); // 日付をカレンダークラスにセット
                SimpleDateFormat dateFormat = new SimpleDateFormat(TaskActivity.FORMAT_DATE, Locale.JAPAN);
                divingLog.setDate(dateFormat.format(calendar.getTime()));   // フォーマットを指定してDivingLogクラスにセット

                // 開始時間
                timeStart = v.findViewById(R.id.add_time_start);
                int hourStart = timeStart.getHour();
                int minuteStart = timeStart.getMinute();
                calendar.set(hourStart, minuteStart);
                SimpleDateFormat timeFormat = new SimpleDateFormat(TaskActivity.FORMAT_TIME, Locale.JAPAN);
                divingLog.setTimeStart(timeFormat.format(calendar.getTime()));

                // 終了時間
                timeEnd = v.findViewById(R.id.add_time_end);
                int hourEnd = timeEnd.getHour();
                int minuteEnd = timeEnd.getMinute();
                calendar.set(hourEnd, minuteEnd);
                divingLog.setTimeEnd(timeFormat.format(calendar.getTime()));

                // 潜水時間
                int hour = hourEnd - hourStart;
                int minute;
                if (minuteEnd < minuteStart) {
                    minute = minuteEnd + 60 - minuteStart;
                    hour = hour - 1;
                } else {
                    minute = minuteEnd - minuteStart;
                }
                calendar.set(hour, minute);
                divingLog.setTimeDive(timeFormat.format(calendar.getTime()));


                // -----【DB】保存処理--------------
                RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask(requireContext());

                // コールバック処理を設定
                registerAsyncTask.setOnCallBack(new RegisterAsyncTask.RegisterCallback() {

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
}