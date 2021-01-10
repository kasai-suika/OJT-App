package com.ojtapp.divinglog.view.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;

import java.util.List;

public class LogAdapter extends ArrayAdapter<DivingLog> {
    private static final String TAG = LogAdapter.class.getSimpleName();
    private final int resource;
    private final LayoutInflater inflater;
    private final Context context;

    /**
     * コンストラクタ
     */
    LogAdapter(@NonNull Context context, int resource, List<DivingLog> items) {
        super(context, resource, items);
        this.resource = resource;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        View view;
        // Viewの再利用
        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(resource, null);
        }

        // DivingLogを取得
        final DivingLog log = getItem(position);
        if (log != null) {
            // 本数
            TextView divingNumber = view.findViewById(R.id.list_diving_number);
            divingNumber.setText(log.getDivingNumber());

            // 場所
            TextView place = view.findViewById(R.id.list_place);
            place.setText(log.getPlace());

            // ポイント
            TextView point = view.findViewById(R.id.list_point);
            point.setText(log.getPoint());
        } else {
            Log.e(TAG, "log = null");
        }

        // TODO:リスト内表示の「編集ボタン」を押下

        // TODO:リスト内表示の「削除ボタン」を押下

        return view;
    }
}
