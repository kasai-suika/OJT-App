package com.ojtapp.divinglog.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ojtapp.divinglog.LogConstant;

public class OpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "DivingLog.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LogConstant.TABLE_NAME + "(" +
                    LogConstant.LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LogConstant.DIVE_NUMBER + " INT, " +
                    LogConstant.PLACE + " TEXT , " +
                    LogConstant.POINT + " TEXT , " +
                    LogConstant.DATE + " TEXT, " +
                    LogConstant.TIME_START + " TEXT, " +
                    LogConstant.TIME_END + " TEXT, " +
                    LogConstant.TIME_DIVE + " TEXT, " +
                    LogConstant.DEPTH_MAX + " INT, " +
                    LogConstant.DEPTH_AVE + " INT, " +
                    LogConstant.AIR_START + " INT, " +
                    LogConstant.AIR_END + " INT, " +
                    LogConstant.AIR_DIVE + " INT, " +
                    LogConstant.WEATHER + " TEXT, " +
                    LogConstant.TEMP + " INT, " +
                    LogConstant.TEMP_WATER + " INT, " +
                    LogConstant.VISIBILITY + " INT, " +
                    LogConstant.MEMBER_NAVIGATE + " TEXT, " +
                    LogConstant.MEMBER + " TEXT, " +
                    LogConstant.MEMO + " TEXT, " +
                    LogConstant.PICTURE + " TEXT" + ")";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LogConstant.TABLE_NAME;

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // データベースの作成
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // データベースの更新
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}

