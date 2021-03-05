package com.ojtapp.divinglog.view.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;

public class LogActivity extends AppCompatActivity {
    private static final String TAG = LogActivity.class.getSimpleName();
    /**
     * DivingLogオブジェクト受け取り用キー
     */
    public static final String TABLE_KEY = "DIVE_LOG";
    /**
     * 各機能の文字受け取り用キー（「追加」「「詳細」「編集」）
     */
    public static final String MODE_KEY = "MODE";
    /**
     * 日付フォーマット
     */
    public static final String FORMAT_DATE = "yyyy/MM/dd";
    /**
     * 時間フォーマット
     */
    public static final String FORMAT_TIME = "HH:mm";

    /**
     * 画面遷移
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Fragment targetFragment;
        setContentView(R.layout.activity_task);

        // DivingLogオブジェクトの取得
        DivingLog divingLog = (DivingLog) intent.getSerializableExtra(TABLE_KEY);
        // 画面遷移のモードの取得
        String modeValue = intent.getStringExtra(MODE_KEY);
        Log.d(TAG, "modeValue=" + modeValue);

        // フラグメントの選択
        switch (Mode.getEnumName(modeValue)) {
            case ADD_MODE:
                targetFragment = LogAddFragment.newInstance();
                Log.d(TAG, "追加画面に遷移");
                break;
            case DETAIL_MOOD:
                targetFragment = LogDetailFragment.newInstance(divingLog);
                Log.d(TAG, "詳細画面に遷移");
                break;
            case EDIT_MOOD:
                targetFragment = LogEditFragment.newInstance(divingLog);
                Log.d(TAG, "編集画面に遷移");
                break;
            default:
                targetFragment = LogAddFragment.newInstance();
                Log.d(TAG, "追加画面に遷移（default）");
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_task, targetFragment).commit();
    }

    public enum Mode {
        ADD_MODE("ADD"),        // 追加モードのキー
        DETAIL_MOOD("DETAIL"),  // 詳細モードのキー
        EDIT_MOOD("EDIT");      // 編集モードのキー

        public final String value;

        Mode(@NonNull String value) {
            this.value = value;
        }

        /**
         * 引数`str`と同じ文字列を持つ列挙子を返す
         * {@inheritDoc}
         */
        public static Mode getEnumName(String str) {
            for (Mode mode : values()) {          // 全ての列挙子を順に比較
                if (mode.value.equals(str)) {     // 引数’str’と文字列が一致した場合
                    return mode;                // 列挙子を返す
                }
            }
            // 一致するものがない場合例外をthrow
            throw new IllegalArgumentException("undefined : " + str);
        }
    }
}