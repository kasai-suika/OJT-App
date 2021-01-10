package com.ojtapp.divinglog.view.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.view.detail.TaskActivity;
import com.ojtapp.divinglog.view.detail.TaskAddFragment;

public class MainActivity extends AppCompatActivity {
    public static final int PAGE_MAIN = 0;
    public static final int PAGE_LOG = 1;
    public static final int ALL_TAB_NUMBER = 2;
    public static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // アクションボタンとViewの紐づけ
        FloatingActionButton addButton = findViewById(R.id.button_add);
        // 追加ボタンがクリックされた時の動作
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "追加ボタン押下");

                // TaskActivityに「追加」モードの情報を渡す
                Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                intent.putExtra(TaskActivity.MODE_KEY, TaskActivity.Mode.ADD_MODE.value);
                startActivity(intent);
            }
        });

         LogFragment targetFragment = LogFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, targetFragment).commit();
    }
}