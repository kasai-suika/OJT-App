package com.ojtapp.divinglog.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.controller.DeleteAsyncTask;
import com.ojtapp.divinglog.controller.DisplayAsyncTask;
import com.ojtapp.divinglog.controller.RegisterAsyncTask;
import com.ojtapp.divinglog.controller.UpdateAsyncTask;
import com.ojtapp.divinglog.view.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ControlDBUtil {
    /**
     * クラス名
     */
    private static final String TAG = ControlDBUtil.class.getSimpleName();


    public static void setNewDataToDB(@NonNull DivingLog divingLog, @NonNull Context context) {
        // -----【DB】保存処理--------------
        RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask(context);

        // コールバック処理を設定
        registerAsyncTask.setOnCallBack(new RegisterAsyncTask.RegisterCallback() {
            @Override
            public void onSuccess() {
                // --------最初の画面へ戻る処理------
                // 情報をintentに詰める
                Intent intent = new Intent(context, MainActivity.class); // Todo:Utilでやるの良くない
                // 指定したアクティビティより上のViewを削除
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "正常にデータを保存できませんでした");
            }
        });

        // 保存処理開始
        registerAsyncTask.execute(divingLog);
    }


    public static void updateDataOfDB(@NonNull DivingLog divingLog, @NonNull Context context) {
        // -----【DB】更新処理--------------
        UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(context);
        updateAsyncTask.setUpdateCallback(new UpdateAsyncTask.UpdateCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "上書きに成功しました");
                // 情報をintentに詰める
                Intent intent = new Intent(context, MainActivity.class);    // Todo:Utilでやるの良くない.
                // 指定したアクティビティより上のViewを削除
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "上書きに失敗しました");
            }
        });
        updateAsyncTask.execute(divingLog);
    }

    public static void deleteDataOfDB(@NonNull DivingLog divingLog, @NonNull Context context, @NonNull FragmentActivity activity) {
        DeleteAsyncTask deleteAsyncTask = new DeleteAsyncTask(context, activity);
        deleteAsyncTask.setDeleteCallback(new DeleteAsyncTask.DeleteCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "削除に成功しました");
                Intent intent = new Intent(context, MainActivity.class);  // Todo:Utilでやるの良くない.
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "削除に失敗しました");
            }
        });
        deleteAsyncTask.execute(divingLog);
    }

    @Nullable
    public static List<DivingLog> getDataListFromDB(@NonNull Context context) {
        DisplayAsyncTask displayAsyncTask = new DisplayAsyncTask(context);
        final List<List<DivingLog>> logLists = new ArrayList<>();

        // コールバック処理
        displayAsyncTask.setOnCallBack(new DisplayAsyncTask.DisplayCallback() {
            @Override
            public void onSuccess(List<DivingLog> logList) {
                Log.d(TAG, "データ取得に成功しました");
                logLists.set(0, logList);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "データ取得に失敗しました");
                logLists.set(0, null);
            }
        });

        // 非同期処理のメソッドに移動
        displayAsyncTask.execute(0);

        while (0 == logLists.size()) {
            Log.d(TAG, "データ取得処理中です " + logLists.size());
        }

        return logLists.get(0);
    }
}
