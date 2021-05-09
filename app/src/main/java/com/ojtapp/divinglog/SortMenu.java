package com.ojtapp.divinglog;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ojtapp.divinglog.appif.DivingLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortMenu {
    private static final String TAG = SortMenu.class.getSimpleName();
    private static final int INDEX_SORTMODE_MAX_VALUE = 1;
    private static final int INDEX_SORTMODE_MIN_VALUE = 1;

    public static void sortDivingLog(@NonNull List<DivingLog> divingLogList, int sortModeValue) {
        Log.d(TAG, "sortDivingLog");
        if ((INDEX_SORTMODE_MAX_VALUE < sortModeValue) || (INDEX_SORTMODE_MIN_VALUE > sortModeValue)) {
            throw new IllegalArgumentException("引数'sortModeValue'の値が不適切です");
        }

        SortModes sortMode = SortModes.values()[sortModeValue];

        if (SortModes.UP_MODE == sortMode) {
            Collections.sort(divingLogList, new Comparator<DivingLog>() {
                @Override
                public int compare(DivingLog o1, DivingLog o2) {
                    Log.d(TAG, SortModes.UP_MODE.value);
                    return o1.getDivingNumber() - o2.getDivingNumber();
                }
            });
        } else if (SortModes.DOWN_MODE == sortMode) {
            Collections.sort(divingLogList, new Comparator<DivingLog>() {
                @Override
                public int compare(DivingLog o1, DivingLog o2) {
                    Log.d(TAG, SortModes.DOWN_MODE.value);
                    return o2.getDivingNumber() - o1.getDivingNumber();
                }
            });
        } else {
            throw new IndexOutOfBoundsException("対象のソートモードがありません");
        }
    }

    public enum SortModes {
        UP_MODE("No（昇順）"),
        DOWN_MODE("No（降順）");

        public final String value;

        SortModes(@NonNull String value) {
            this.value = value;
        }

        public static String[] getSortModesStr() {
            List<String> list = new ArrayList<>();
            for (SortModes mode : values()) {          // 全ての列挙子を順に比較
                list.add(mode.value);
            }
            return list.toArray(new String[0]);
        }
    }
}
