package com.ojtapp.divinglog.view.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojtapp.divinglog.LogConstant;
import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.controller.RegisterAsyncTask;
import com.ojtapp.divinglog.view.main.MainActivity;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LogAddFragment extends Fragment {
    /**
     * クラスの名前
     */
    private static final String TAG = LogAddFragment.class.getSimpleName();

    public static final int NO_DATA = -1;

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
    private Uri uri;

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
        return inflater.inflate(R.layout.fragment_add_log, container, false);
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

                //---------DivingLogクラスにセット------------
                DivingLog divingLog = new DivingLog();
                // データをDivingLogクラスにセット
                setDateToDivingLog(divingLog);

                // -----【DB】保存処理--------------
                RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask(requireContext());

                // コールバック処理を設定
                registerAsyncTask.setOnCallBack(new RegisterAsyncTask.RegisterCallback() {
                    @Override
                    public void onSuccess() {
                        // --------最初の画面へ戻る処理------
                        // 情報をintentに詰める
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        // 指定したアクティビティより上のViewを削除
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "正常にデータを保存できませんでした");
                    }
                });

                // 保存処理開始
                registerAsyncTask.execute(divingLog);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == MainActivity.RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                uri = resultData.getData();

                // URIの権限を保持する
                final int takeFlags = resultData.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
                try {
                    Bitmap bmp = getBitmapFromUri(uri);
                    picture.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Uri型をBitmap型に変換する
     *
     * @param uri 選択した写真のuri
     * @return 引数のuriをBitmapに変換したもの
     * @throws IOException 例外
     */
    @NonNull
    private Bitmap getBitmapFromUri(@NonNull Uri uri) throws IOException {
        Context context = getContext();
        assert context != null;
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
    private void setDateToDivingLog(@NonNull DivingLog divingLog) {
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
        divingLog.setPictureUri(uri.toString());
    }

    /**
     * String型をint型に変換する。
     * データがない場合は-1を返す。
     *
     * @param strData 　String型のデータ
     * @return int型に変換したデータ
     */
    public static int getIntData(@NonNull String strData) {
        if (0 == strData.length()) {
            return NO_DATA;
        } else {
            return Integer.parseInt(strData);
        }
    }
}