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
import androidx.fragment.app.FragmentManager;

import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.controller.UpdateAsyncTask;
import com.ojtapp.divinglog.view.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LogEditFragment extends Fragment {
    /**
     * クラス名
     */
    private static final String TAG = LogEditFragment.class.getSimpleName();
    /**
     * 日付を区切る記号 "/"
     */
    private static final String SEPARATE_DAY = "/";
    /**
     * 時間を区切る記号 ":"
     */
    private static final String SEPARATE_TIME = ":";


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

    /**
     * フラグメントのインスタンスを作成
     * {@inheritDoc}
     */
    public static Fragment newInstance(DivingLog divingLog) {
        android.util.Log.d(TAG, "newInstance()");

        LogEditFragment fragment = new LogEditFragment();

        Bundle args = new Bundle();
        args.putSerializable(LogActivity.TABLE_KEY, divingLog);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStat) {
        return inflater.inflate(R.layout.fragment_edit_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStat) {
        super.onViewCreated(view, savedInstanceStat);

        Bundle args = getArguments();
        if (null == args) {
            android.util.Log.e(TAG, "args = null");
            return;
        }

        // シリアライズしたDivingLogクラスを取得
        final DivingLog divingLog = (DivingLog) args.getSerializable(LogActivity.TABLE_KEY);

        // 変数とレイアウトのViewを紐づける
        diveNumber = view.findViewById(R.id.edit_dive_number);
        place = view.findViewById(R.id.edit_place);
        point = view.findViewById(R.id.edit_point);
        depthMax = view.findViewById(R.id.edit_depth_max);
        depthAve = view.findViewById(R.id.edit_depth_ave);
        airStart = view.findViewById(R.id.edit_air_start);
        airEnd = view.findViewById(R.id.edit_air_end);
        weather = view.findViewById(R.id.edit_weather);
        temp = view.findViewById(R.id.edit_temp);
        tempWater = view.findViewById(R.id.edit_temp_water);
        visibility = view.findViewById(R.id.edit_visibility);
        member = view.findViewById(R.id.edit_member);
        memberNavigate = view.findViewById(R.id.edit_navi);
        memo = view.findViewById(R.id.edit_memo);
        date = view.findViewById(R.id.edit_date);
        timeStart = view.findViewById(R.id.edit_time_start);
        timeEnd = view.findViewById(R.id.edit_time_end);

        // 初期データのセット
        if (null != divingLog) {
            Log.d(TAG, "divN =" + String.valueOf(divingLog.getDivingNumber()));
            diveNumber.setText(String.valueOf(divingLog.getDivingNumber()));
            place.setText(divingLog.getPlace());
            point.setText(divingLog.getPoint());
            depthMax.setText(LogDetailFragment.createStringData(divingLog.getDepthMax()));
            depthAve.setText(LogDetailFragment.createStringData(divingLog.getDepthAve()));
            airStart.setText(LogDetailFragment.createStringData(divingLog.getAirStart()));
            airEnd.setText(LogDetailFragment.createStringData(divingLog.getAirEnd()));
            weather.setText(divingLog.getWeather());
            temp.setText(LogDetailFragment.createStringData(divingLog.getTemp()));
            tempWater.setText(LogDetailFragment.createStringData(divingLog.getTempWater()));
            visibility.setText(LogDetailFragment.createStringData(divingLog.getVisibility()));
            member.setText(divingLog.getMember());
            memberNavigate.setText(divingLog.getMemberNavigate());
            memo.setText(divingLog.getMemo());

            String[] diveDay = divingLog.getDate().split(SEPARATE_DAY, 0);
            int year = Integer.parseInt(diveDay[0]);
            int month = (Integer.parseInt(diveDay[1]) - 1);
            int day = Integer.parseInt(diveDay[2]);
            date.updateDate(year, month, day);

            String[] startTime = divingLog.getTimeStart().split(SEPARATE_TIME, 0);
            timeStart.setHour(Integer.parseInt(startTime[0]));
            timeStart.setMinute(Integer.parseInt(startTime[1]));

            String[] endTime = divingLog.getTimeStart().split(SEPARATE_TIME, 0);
            timeEnd.setHour(Integer.parseInt(endTime[0]));
            timeEnd.setMinute(Integer.parseInt(endTime[1]));
        } else {
            Log.e(TAG, "divingLog = null");
        }

        // 削除ボタン押下時の設定
        Button deleteButton = view.findViewById(R.id.edit_button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "削除ボタン押下");
                FragmentManager fragmentManager = getFragmentManager();
                DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(divingLog);
                deleteDialogFragment.show(fragmentManager, null);
            }
        });

        Button saveButton = view.findViewById(R.id.edit_button_update);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 本数 TODO:LogAddFragmentと同じ処理。簡略化出来るのでは。
                divingLog.setDiveNumber(Integer.parseInt(diveNumber.getText().toString()));

                // 場所
                divingLog.setPlace(place.getText().toString());

                // ポイント
                divingLog.setPoint(point.getText().toString());

                // 深度（最大）
                divingLog.setDepthMax(LogAddFragment.getIntData(depthMax.getText().toString()));

                // 深度（平均）
                divingLog.setDepthAve(LogAddFragment.getIntData(depthAve.getText().toString()));

                // 残圧（開始時）
                int airStartInt = LogAddFragment.getIntData(airStart.getText().toString());
                divingLog.setAirStart(airStartInt);

                // 残圧（終了時）
                int airEndInt = LogAddFragment.getIntData(airEnd.getText().toString());
                divingLog.setAirEnd(airEndInt);

                // 使用した空気
                if ((LogAddFragment.NO_DATA == airStartInt) || (LogAddFragment.NO_DATA == airEndInt)) {
                    divingLog.setAirDive(LogAddFragment.NO_DATA);
                } else {
                    int airDive = airStartInt - airEndInt;
                    divingLog.setAirDive(airDive);
                }

                // 天気
                divingLog.setWeather(weather.getText().toString());

                // 気温
                divingLog.setTemp(LogAddFragment.getIntData(temp.getText().toString()));

                // 水温
                divingLog.setTempWater(LogAddFragment.getIntData(tempWater.getText().toString()));

                // 透明度
                divingLog.setVisibility(LogAddFragment.getIntData(visibility.getText().toString()));

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
                calendar.set(hourStart, minuteStart);
                SimpleDateFormat timeFormat = new SimpleDateFormat(LogActivity.FORMAT_TIME, Locale.JAPAN);
                divingLog.setTimeStart(timeFormat.format(calendar.getTime()));

                // 終了時間
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

                // -----【DB】更新処理--------------
                UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(requireContext());
                updateAsyncTask.setUpdateCallback(new UpdateAsyncTask.UpdateCallback() {
                    @Override
                    public void onUpdate(boolean result) {
                        // 情報をintentに詰める
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        // 指定したアクティビティより上のViewを削除
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                updateAsyncTask.execute(divingLog);
            }
        });
    }
}
