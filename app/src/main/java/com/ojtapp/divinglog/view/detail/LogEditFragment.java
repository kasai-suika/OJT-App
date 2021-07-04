package com.ojtapp.divinglog.view.detail;

import android.app.Activity;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojtapp.divinglog.LogConstant;
import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.util.ControlDBUtil;
import com.ojtapp.divinglog.util.ConversionUtil;
import com.ojtapp.divinglog.view.dialog.DialogFragment;
import com.ojtapp.divinglog.view.main.MainActivity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class LogEditFragment extends Fragment {
    /**
     * クラス名
     */
    private static final String TAG = LogEditFragment.class.getSimpleName();
    /**
     * DivingLogオブジェクト受け取り用キー
     */
    private static final String LOG_KEY = "DIVE_LOG";

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
    private ImageView picture;
    private Uri uri;

    /**
     * デフォルトコンストラクタ
     */
    public LogEditFragment() {
    }

    /**
     * フラグメントのインスタンスを作成
     *
     * @return フラグメント
     */
    public static Fragment newInstance(@NonNull DivingLog divingLog) {
        android.util.Log.d(TAG, "newInstance()");

        LogEditFragment fragment = new LogEditFragment();

        Bundle args = new Bundle();
        args.putSerializable(LOG_KEY, divingLog);
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
            Log.e(TAG, "args = null");
            return;
        }

        // シリアライズしたDivingLogクラスを取得
        final DivingLog divingLog = (DivingLog) args.getSerializable(LOG_KEY);

        // 変数とレイアウトのViewを紐づける
        linkVariable(view);

        // 初期データのセット
        if (null != divingLog) {    //TODO ifPresentOrElse
            try {
                setDefaultDate(divingLog);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "divingLog = null");
        }

        FloatingActionButton selectPictureButton = view.findViewById(R.id.button_add_picture);
        // 写真追加ボタン押下
        selectPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, MainActivity.RESULT_PICK_IMAGEFILE);
            }
        });

        Button saveButton = view.findViewById(R.id.edit_button_update);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert divingLog != null; //TODO ifPresentOrElse
                setDateToDivingLog(divingLog);

                ControlDBUtil.updateDataOfDB(divingLog, getContext());
            }
        });

        // 削除ボタン押下時の設定
        Button deleteButton = view.findViewById(R.id.edit_button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "削除ボタン押下");
                final FragmentActivity activity = getActivity();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                DialogFragment deleteDialogFragment = DialogFragment.newInstance(
                        LogConstant.TITLE_DELETE_DIALOG,
                        LogConstant.MESSAGE_DELETE_DIALOG);

                deleteDialogFragment.setOnClickButtonListener(new DialogFragment.OnClickButtonListener() {
                    @Override
                    public void onClickPositiveButton() {
                        ControlDBUtil.deleteDataOfDB(divingLog, getContext(), activity);
                    }

                    @Override
                    public void onClickNegativeButton() {
                    }
                });
                deleteDialogFragment.show(fragmentManager, null);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == MainActivity.RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            Context context = getContext();
            if ((resultData != null) && (context != null)) {
                uri = resultData.getData();

                // URIの権限を保持する
                final int takeFlags = resultData.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                context.getContentResolver().takePersistableUriPermission(uri, takeFlags);
                try {
                    Bitmap bmp = ConversionUtil.getBitmapFromUri(uri, context);
                    picture.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 変数とidを紐づける TODO:Addとおなじ。継承作れる？
     *
     * @param view View
     */
    private void linkVariable(@NonNull View view) {
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
        picture = view.findViewById(R.id.image_view_select_picture);
    }

    /**
     * 初期値を設定
     *
     * @param divingLog Logデータ
     */
    private void setDefaultDate(@NonNull DivingLog divingLog) throws ParseException {
        diveNumber.setText(String.valueOf(divingLog.getDivingNumber()));
        place.setText(divingLog.getPlace());
        point.setText(divingLog.getPoint());
        depthMax.setText(ConversionUtil.getStrFromInt(divingLog.getDepthMax()));
        depthAve.setText(ConversionUtil.getStrFromInt(divingLog.getDepthAve()));
        airStart.setText(ConversionUtil.getStrFromInt(divingLog.getAirStart()));
        airEnd.setText(ConversionUtil.getStrFromInt(divingLog.getAirEnd()));
        weather.setText(divingLog.getWeather());
        temp.setText(ConversionUtil.getStrFromInt(divingLog.getTemp()));
        tempWater.setText(ConversionUtil.getStrFromInt(divingLog.getTempWater()));
        visibility.setText(ConversionUtil.getStrFromInt(divingLog.getVisibility()));
        member.setText(divingLog.getMember());
        memberNavigate.setText(divingLog.getMemberNavigate());
        memo.setText(divingLog.getMemo());

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstant.FORMAT_DATE, Locale.JAPAN);
        Optional<Date> defaultDateOpt = Optional.ofNullable(dateFormat.parse(divingLog.getDate()));
        defaultDateOpt.ifPresent(cal::setTime);  //TODO ifPresentOrElse
        date.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat timeFormat = new SimpleDateFormat(LogConstant.FORMAT_TIME, Locale.JAPAN);
        Optional<Date> defaultStartTimeOpt = Optional.ofNullable(timeFormat.parse(divingLog.getTimeStart()));
        defaultStartTimeOpt.ifPresent(cal::setTime);  //TODO ifPresentOrElse
        timeStart.setHour(cal.get(Calendar.HOUR_OF_DAY));
        timeStart.setMinute(cal.get(Calendar.MINUTE));

        Optional<Date> defaultEndTimeOpt = Optional.ofNullable(timeFormat.parse(divingLog.getTimeEnd()));
        defaultEndTimeOpt.ifPresent(cal::setTime);  //TODO ifPresentOrElse
        timeEnd.setHour(cal.get(Calendar.HOUR_OF_DAY));
        timeEnd.setMinute(cal.get(Calendar.MINUTE));

        try {
            String pictureUri = divingLog.getPictureUri();
            if (null != pictureUri) {
                uri = Uri.parse(pictureUri);
                Bitmap bitmap = ConversionUtil.getBitmapFromUri(uri, requireContext());
                picture.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DivingLogクラスに値をセットする TODO:Addとおなじ。継承作れる？
     *
     * @param divingLog 　セットするDivingLogクラス
     */
    private void setDateToDivingLog(@NonNull DivingLog divingLog) {
        divingLog.setDiveNumber(Integer.parseInt(diveNumber.getText().toString()));

        // 場所
        divingLog.setPlace(place.getText().toString());

        // ポイント
        divingLog.setPoint(point.getText().toString());

        // 深度（最大）
        divingLog.setDepthMax(ConversionUtil.getIntFromStr(depthMax.getText().toString()));

        // 深度（平均）
        divingLog.setDepthAve(ConversionUtil.getIntFromStr(depthAve.getText().toString()));

        // 残圧（開始時）
        int airStartInt = ConversionUtil.getIntFromStr(airStart.getText().toString());
        divingLog.setAirStart(airStartInt);

        // 残圧（終了時）
        int airEndInt = ConversionUtil.getIntFromStr(airEnd.getText().toString());
        divingLog.setAirEnd(airEndInt);

        // 使用した空気
        if ((ConversionUtil.NO_DATA == airStartInt) || (ConversionUtil.NO_DATA == airEndInt)) {
            divingLog.setAirDive(ConversionUtil.NO_DATA);
        } else {
            int airDive = airStartInt - airEndInt;
            divingLog.setAirDive(airDive);
        }

        // 天気
        divingLog.setWeather(weather.getText().toString());

        // 気温
        divingLog.setTemp(ConversionUtil.getIntFromStr(temp.getText().toString()));

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
        calendar.set(Calendar.HOUR_OF_DAY, hourStart);
        calendar.set(Calendar.MINUTE, minuteStart);
        SimpleDateFormat timeFormat = new SimpleDateFormat(LogConstant.FORMAT_TIME, Locale.JAPAN);
        divingLog.setTimeStart(timeFormat.format(calendar.getTime()));

        // 終了時間
        int hourEnd = timeEnd.getHour();
        int minuteEnd = timeEnd.getMinute();
        calendar.set(Calendar.HOUR_OF_DAY, hourEnd);
        calendar.set(Calendar.MINUTE, minuteEnd);
        divingLog.setTimeEnd(timeFormat.format(calendar.getTime()));

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
