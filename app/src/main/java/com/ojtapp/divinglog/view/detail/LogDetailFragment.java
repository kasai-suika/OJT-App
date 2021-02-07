package com.ojtapp.divinglog.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;

public class LogDetailFragment extends Fragment {
    private static final String TAG = LogDetailFragment.class.getSimpleName();

    /**
     * フラグメントのインスタンスを作成
     * {@inheritDoc}
     */
    public static Fragment newInstance(DivingLog divingLog) {
        android.util.Log.d(TAG, "newInstance()");

        LogDetailFragment fragment = new LogDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(LogActivity.TABLE_KEY, divingLog);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStat) {
        return inflater.inflate(R.layout.fragment_detail_log, container, false);
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
        TextView diveNumber = view.findViewById(R.id.det_dive_number);
        TextView place = view.findViewById(R.id.det_place);
        TextView point = view.findViewById(R.id.det_point);
        TextView date = view.findViewById(R.id.det_date);
        TextView time = view.findViewById(R.id.det_time);
        TextView depthMax = view.findViewById(R.id.det_depth_max);
        TextView depthAve = view.findViewById(R.id.det_depth_ave);
        TextView air = view.findViewById(R.id.det_air);
        TextView weather = view.findViewById(R.id.det_weather);
        TextView temp = view.findViewById(R.id.det_temp);
        TextView tempWater = view.findViewById(R.id.det_temp_water);
        TextView visibility = view.findViewById(R.id.det_visibility);
        TextView member = view.findViewById(R.id.det_member);
        TextView navi = view.findViewById(R.id.det_navi);
        TextView memo = view.findViewById(R.id.det_memo);

        // 値をセット
        if (divingLog != null) {
            diveNumber.setText(String.valueOf(divingLog.getDivingNumber()));
            place.setText(divingLog.getPlace());
            point.setText(divingLog.getPoint());
            date.setText(divingLog.getDate());
            time.setText(divingLog.getTimeDive());
            depthMax.setText(createStringData(divingLog.getDepthMax()));
            depthAve.setText(createStringData(divingLog.getDepthAve()));
            air.setText(createStringData(divingLog.getAirDive()));
            weather.setText(divingLog.getWeather());
            temp.setText(createStringData(divingLog.getTemp()));
            tempWater.setText(createStringData(divingLog.getTempWater()));
            visibility.setText(createStringData(divingLog.getVisibility()));
            member.setText(divingLog.getMember());
            navi.setText(divingLog.getMemberNavigate());
            memo.setText(divingLog.getMemo());
        }

        // 編集ボタン押下時の設定
        Button editButton = view.findViewById(R.id.detail_button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "編集ボタン押下");
                Intent intent = new Intent(getContext(), LogActivity.class);
                intent.putExtra(LogActivity.MODE_KEY, LogActivity.Mode.EDIT_MOOD.value);
                intent.putExtra(LogActivity.TABLE_KEY, divingLog);
                startActivity(intent);
            }
        });

        // 削除ボタン押下時の設定
        Button deleteButton = view.findViewById(R.id.detail_button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "削除ボタン押下");
                DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(divingLog);
                deleteDialogFragment.show(getFragmentManager(), null);
            }
        });
    }

    public static String createStringData(int intData) {
        if (LogAddFragment.NO_DATA == intData) {
            return "";
        } else {
            return String.valueOf(intData);
        }
    }
}
