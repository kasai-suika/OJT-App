package com.ojtapp.divinglog.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;

public class TaskDetailFragment extends Fragment{
    private static final String TAG = TaskDetailFragment.class.getSimpleName();

    /**
     * フラグメントのインスタンスを作成
     * {@inheritDoc}
     */
    public static Fragment newInstance(DivingLog log) {
        android.util.Log.d(TAG, "newInstance()");

        TaskDetailFragment fragment = new TaskDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(TaskActivity.TABLE_KEY, log);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStat){
        return inflater.inflate(R.layout.fragment_detail_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStat){
        super.onViewCreated(view, savedInstanceStat);

        Bundle args = getArguments();
        if(null == args){
            android.util.Log.e(TAG, "args = null");
            return;
        }

        // シリアライズしたDivingLogクラスを格納
        final DivingLog log = (DivingLog)args.getSerializable(TaskActivity.TABLE_KEY);
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
        if (log != null){
            diveNumber.setText(log.getDivingNumber());
            place.setText(log.getPlace());
            point.setText(log.getPoint());
            date.setText(log.getDate());
            time.setText(log.getTimeDive());
            depthMax.setText(log.getDepthMax());
            depthAve.setText(log.getDepthAve());
            air.setText(log.getAirDive());
            weather.setText(log.getWeather());
            temp.setText(log.getTemp());
            tempWater.setText(log.getTempWater());
            visibility.setText(log.getVisibility());
            member.setText(log.getMember());
            navi.setText(log.getMemberNavigate());
            memo.setText(log.getMemo());
        }

        // 変数とボタンの紐づけ
        Button editButton = view.findViewById(R.id.button_edit_task);
        // 編集ボタン押下時の設定
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TaskActivity.class);
                intent.putExtra(TaskActivity.MODE_KEY, TaskActivity.Mode.EDIT_MOOD.value);
                intent.putExtra(TaskActivity.TABLE_KEY, log);
                startActivity(intent);
            }
        });
    }
}
