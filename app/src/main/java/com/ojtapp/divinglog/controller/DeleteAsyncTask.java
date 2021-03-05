package com.ojtapp.divinglog.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ojtapp.divinglog.LogConstant;
import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.model.OpenHelper;

import java.lang.ref.WeakReference;

/**
 * Log情報をDBから削除するクラス
 */
public class DeleteAsyncTask extends AsyncTask<DivingLog, Integer, Boolean> {
    /**
     * クラス名
     */
    private static final String TAG = DeleteAsyncTask.class.getSimpleName();
    /**
     * Context受け取り用
     */
    @NonNull
    private final WeakReference<Context> weakReference;
    /**
     * コールバック設定用
     */
    @Nullable
    private DeleteCallback deleteCallback;

    public DeleteAsyncTask(@NonNull Context context) {
        super();
        this.weakReference = new WeakReference<>(context);
    }

    @Override
    protected Boolean doInBackground(DivingLog... divingLogs) {
        Log.d(TAG, "doInBackground()");

        OpenHelper openHelper = new OpenHelper(weakReference.get());
        SQLiteDatabase db = openHelper.getWritableDatabase();

        String selection = LogConstant.LOG_ID + " = ?";
        String[] selectionArgs = {String.valueOf(divingLogs[0].getLogId())};
        db.delete(LogConstant.TABLE_NAME, selection, selectionArgs);
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... integers) {
    }

    /**
     * 非同期処理終了時にコールバックを呼び出す
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (null != deleteCallback) {
            if (result) {
                deleteCallback.onSuccess();
            } else {
                deleteCallback.onFailure();
            }
        }
    }

    /**
     * コールバック処理を設定
     *
     * @param deleteCallback 　コールバックする内容
     */
    public void setDeleteCallback(@Nullable DeleteCallback deleteCallback) {
        this.deleteCallback = deleteCallback;
    }

    /**
     * コールバック用インターフェイス
     */
    public interface DeleteCallback {
        void onSuccess();

        void onFailure();
    }

}
