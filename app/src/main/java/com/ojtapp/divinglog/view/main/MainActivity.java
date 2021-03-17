package com.ojtapp.divinglog.view.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.util.SharedPreferencesUtil;
import com.ojtapp.divinglog.view.detail.LogActivity;
import com.ojtapp.divinglog.view.detail.SortDialogFragment;

public class MainActivity extends AppCompatActivity {
    /**
     * クラス名
     */
    private static final String TAG = MainActivity.class.getSimpleName();
    private LogFragment targetFragment;
    public static SharedPreferencesUtil sharedPreferencesUtil;
    public static final int RESULT_PICK_IMAGEFILE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferencesUtil = new SharedPreferencesUtil(getApplicationContext(),SharedPreferencesUtil.FILE_NAME_SORT);

        // アクションボタンとViewの紐づけ
        FloatingActionButton addButton = findViewById(R.id.button_add);
        // 追加ボタンがクリックされた時の動作
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "追加ボタン押下");

                // TaskActivityに「追加」モードの情報を渡す
                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                intent.putExtra(LogActivity.MODE_KEY, LogActivity.Mode.ADD_MODE.value);
                startActivity(intent);
            }
        });

        targetFragment = LogFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, targetFragment).commit();
    }

    /**
     * メニューの作成
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * 「並び替え」をクリックされた時の動作
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){
        int memorySortMode;
        memorySortMode = sharedPreferencesUtil.getInt(SharedPreferencesUtil.KEY_SORT_MODE);

        SortDialogFragment sortDialogFragment = SortDialogFragment.newInstance(memorySortMode);

        sortDialogFragment.setOnCallback(new SortDialogFragment.SortDialogCallback() {
            @Override
            public void onSortDialog(int position) {
                Log.d(TAG, "SortDialogCallback");
                // 選択されたソートモードを記憶
                sharedPreferencesUtil.setInt(SharedPreferencesUtil.KEY_SORT_MODE, position);
                targetFragment.refreshView();
            }
        });
        sortDialogFragment.show(getSupportFragmentManager(), null);
        return super.onOptionsItemSelected(menuItem);
    }
}