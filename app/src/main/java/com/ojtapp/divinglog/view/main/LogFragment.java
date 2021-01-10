package com.ojtapp.divinglog.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.controller.DisplayAsyncTask;
import com.ojtapp.divinglog.view.detail.TaskActivity;
import com.ojtapp.divinglog.view.detail.TaskDetailFragment;

import java.util.List;

/**
 * リスト表示フラグメント
 */
public class LogFragment extends Fragment {
    private static final String TAG = LogFragment.class.getSimpleName();
    private ListView listView;
    private LogAdapter logAdapter;
    /**
     * フラグメントのインスタンスを作成
     * {@inheritDoc}
     */
    public static LogFragment newInstance() {
        android.util.Log.d(TAG, "newInstance()");

        LogFragment fragment = new LogFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Viewを作成
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStat){
        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStat){
        super.onViewCreated(view, savedInstanceStat);
        // リスト表示のViewを結び付ける
        listView = view.findViewById(R.id.list_view_log);
        // データを取得し、画面を更新処理
        refreshView();

        // リスト押下時
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DivingLog log = (DivingLog)parent.getItemAtPosition(position);

                Intent intent = new Intent(getContext(), TaskActivity.class);
                intent.putExtra(TaskActivity.MODE_KEY, TaskActivity.Mode.DETAIL_MOOD.value);
                intent.putExtra(TaskActivity.TABLE_KEY, log);

                startActivity(intent);
            }
        });
    }

    private void refreshView(){
        final Context context = requireContext();
        //-------【DB】データ取得処理-------------
        DisplayAsyncTask displayAsyncTask = new DisplayAsyncTask(context);

        // コールバック処理
        displayAsyncTask.setOnCallBack(new DisplayAsyncTask.DisplayCallback() {
            @Override
            public void onDisplay(List<DivingLog> logList) {
                // Adapterの設定
                LogAdapter logAdapter = new LogAdapter(context, R.layout.list_log_fragment, logList);
                listView.setAdapter(logAdapter);
            }
        });

        // 非同期処理のメソッドに移動
        displayAsyncTask.execute(0);
    }
}
