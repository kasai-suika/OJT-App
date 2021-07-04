package com.ojtapp.divinglog.view.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojtapp.divinglog.LogConstant;
import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.databinding.FragmentAddLogBinding;
import com.ojtapp.divinglog.util.ControlDBUtil;
import com.ojtapp.divinglog.util.ConversionUtil;
import com.ojtapp.divinglog.view.main.MainActivity;
import com.ojtapp.divinglog.viewModel.MainViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LogAddFragment extends Fragment {
    /**
     * クラスの名前
     */
    private static final String TAG = LogAddFragment.class.getSimpleName();

    private EditText diveNumber;    // TODO:変数まみれはみにくい。リストやenumでまとめられないか。（ライフサイクル艇にはonCreatedViewで再設定されるからＯＫ？）
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
    private ImageView picture;
    private Uri uri = null;

    private MainViewModel viewModel = new MainViewModel();

    /**
     * デフォルトコンストラクタ
     */
    public LogAddFragment() {
    }

    /**
     * フラグメントのインスタンスを作成
     *
     * @return フラグメント
     */
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
        FragmentAddLogBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_log, container, false);
        binding.set

        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_add_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStat) {
        super.onViewCreated(view, savedInstanceStat);
        android.util.Log.d(TAG, "onViewCreated");
        Button makeTaskButton = view.findViewById(R.id.button_make_task);
        FloatingActionButton selectPictureButton = view.findViewById(R.id.button_add_picture);

        // 変数とレイアウトを紐づけ
        linkVariable(view);

        // 写真追加ボタン押下
        selectPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, MainActivity.RESULT_PICK_IMAGEFILE);
            }
        });

        // 作成ボタン押下
        makeTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");

                //DivingLogクラスに入力されたデータをセット
                DivingLog divingLog = new DivingLog();
//                setDataToDivingLog(divingLog);

                //DBに入力されたデータをセット
//                ControlDBUtil.setNewDataToDB(divingLog, getContext());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == MainActivity.RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            if (null != resultData) {
                uri = resultData.getData();

                // URIの権限を保持する
                final int takeFlags = resultData.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                requireContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
                try {
                    Bitmap bmp = ConversionUtil.getBitmapFromUri(uri, requireContext());
                    picture.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 変数とidを紐づける
     *
     * @param view View
     */
    private void linkVariable(@NonNull View view) {
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
        picture = view.findViewById(R.id.image_view_select_picture);
    }

    /**
     * DivingLogクラスに値をセットする
     *
     * @param divingLog 　セットするDivingLogクラス
     */
    private void setDataToDivingLog(@NonNull DivingLog divingLog) {
//
//        // 本数
//        divingLog.setDiveNumber(Integer.parseInt(diveNumber.getText().toString()));
//
//        // 場所
//        divingLog.setPlace(place.getText().toString());
//
//        // ポイント
//        divingLog.setPoint(point.getText().toString());
//
//        // 深度（最大）
//        divingLog.setDepthMax(ConversionUtil.getIntFromStr(depthMax.getText().toString()));
//
//        // 深度（平均）
//        divingLog.setDepthAve(ConversionUtil.getIntFromStr(depthAve.getText().toString()));
//
//        // 残圧（開始時）
//        int airStartInt = ConversionUtil.getIntFromStr(airStart.getText().toString());
//        divingLog.setAirStart(airStartInt);
//
//        // 残圧（終了時）
//        int airEndInt = ConversionUtil.getIntFromStr(airEnd.getText().toString());
//        divingLog.setAirEnd(airEndInt);
//
//        // 使用した空気
//        if ((ConversionUtil.NO_DATA == airStartInt) || (ConversionUtil.NO_DATA == airEndInt)) {
//            divingLog.setAirDive(ConversionUtil.NO_DATA);
//        } else {
//            int airDive = airStartInt - airEndInt;
//            divingLog.setAirDive(airDive);
//        }
//
//        // 天気
//        divingLog.setWeather(weather.getText().toString());
//
//        // 気温
//        divingLog.setTemp(ConversionUtil.getIntFromStr(temp.getText().toString()));

        // 水温
        divingLog.setTempWater(ConversionUtil.getIntFromStr(tempWater.getText().toString()));

        // 透明度
        divingLog.setVisibility(ConversionUtil.getIntFromStr(visibility.getText().toString()));

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
        SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstant.FORMAT_DATE, Locale.JAPAN);
        divingLog.setDate(dateFormat.format(calendar.getTime()));   // フォーマットを指定してDivingLogクラスにセット

        // 開始時間
        int hourStart = timeStart.getHour();
        int minuteStart = timeStart.getMinute();
        calendar.set(Calendar.HOUR, hourStart);
        calendar.set(Calendar.MINUTE, minuteStart);
        SimpleDateFormat timeFormat = new SimpleDateFormat(LogConstant.FORMAT_TIME, Locale.JAPAN);
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

        // 写真
        if (null != uri) {
            divingLog.setPictureUri(uri.toString());
        }
    }
}