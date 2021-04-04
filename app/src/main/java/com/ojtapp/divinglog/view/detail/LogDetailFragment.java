package com.ojtapp.divinglog.view.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.ojtapp.divinglog.R;
import com.ojtapp.divinglog.appif.DivingLog;
import com.ojtapp.divinglog.controller.DeleteAsyncTask;
import com.ojtapp.divinglog.view.dialog.DeleteDialogFragment;
import com.ojtapp.divinglog.view.main.MainActivity;

import java.io.FileDescriptor;
import java.io.IOException;

public class LogDetailFragment extends Fragment {
    /**
     * クラス名
     */
    private static final String TAG = LogDetailFragment.class.getSimpleName();
    /**
     * DivingLogオブジェクト受け取り用キー
     */
    private static final String LOG_KEY = "DIVE_LOG";

    /**
     * デフォルトコンストラクタ
     */
    LogDetailFragment() {
    }

    /**
     * フラグメントのインスタンスを作成
     * @return フラグメント
     */
    public static Fragment newInstance(DivingLog divingLog) {
        android.util.Log.d(TAG, "newInstance()");

        LogDetailFragment fragment = new LogDetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(LOG_KEY, divingLog);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStat) {
        return inflater.inflate(R.layout.fragment_detail_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceStat) {
        super.onViewCreated(view, savedInstanceStat);

        Bundle args = getArguments();
        if (null == args) {
            android.util.Log.e(TAG, "args = null");
            return;
        }

        // シリアライズしたDivingLogクラスを取得
        final DivingLog divingLog = (DivingLog) args.getSerializable(LOG_KEY);

        // 編集ボタン押下時の設定
        Button editButton = view.findViewById(R.id.detail_button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "編集ボタン押下");
                Intent intent = new Intent(getContext(), LogActivity.class);
                intent.putExtra(LogActivity.MODE_KEY, LogActivity.Mode.EDIT_MOOD.value);
                intent.putExtra(LogActivity.TABLE_KEY, divingLog);
                startActivity(intent);
            }
        });

        // 削除ボタン押下時の設定
        Button deleteButton = view.findViewById(R.id.detail_button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "削除ボタン押下");
                FragmentActivity fragmentActivity = getActivity();
                assert fragmentActivity != null;
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                assert divingLog != null;
                DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(divingLog);
                deleteDialogFragment.setOnClickButtonListener(new DeleteDialogFragment.OnClickButtonListener() {
                    @Override
                    public void onClickPositiveButton() {
                        DeleteAsyncTask deleteAsyncTask = new DeleteAsyncTask(requireContext(), getActivity());

                        deleteAsyncTask.setDeleteCallback(new DeleteAsyncTask.DeleteCallback() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(requireContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure() {
                                Log.e(TAG, "正常に削除されませんでした");
                            }
                        });

                        Bundle args = getArguments();
                        if (null == args) {
                            Log.e(TAG, "args = null");
                            return;
                        }

                        // シリアライズしたDivingLogクラスを格納
                        final DivingLog divingLog = (DivingLog) args.getSerializable(LOG_KEY);

                        // 非同期処理を実行
                        deleteAsyncTask.execute(divingLog);
                    }
                });
                deleteDialogFragment.show(fragmentManager, null);
            }
        });

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
        ImageView picture = view.findViewById(R.id.image_view_select_picture);

        // 値をセット
        if (divingLog != null) {
            diveNumber.setText(String.valueOf(divingLog.getDivingNumber()));
            place.setText(divingLog.getPlace());
            point.setText(divingLog.getPoint());
            date.setText(divingLog.getDate());
            time.setText(divingLog.getTimeDive());
            depthMax.setText(createStringData(divingLog.getDepthMax()));
            depthAve.setText(createStringData(divingLog.getDepthAve()));
            air.setText(createStringData(divingLog.getAirDive()));
            weather.setText(divingLog.getWeather());
            temp.setText(createStringData(divingLog.getTemp()));
            tempWater.setText(createStringData(divingLog.getTempWater()));
            visibility.setText(createStringData(divingLog.getVisibility()));
            member.setText(divingLog.getMember());
            navi.setText(divingLog.getMemberNavigate());
            memo.setText(divingLog.getMemo());

            try {
                Bitmap bitmap = getBitmapFromUri(Uri.parse(divingLog.getPictureUri()));
                picture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Uri型をBitmap型に変換する
     *
     * @param uri 選択した写真のuri
     * @return 引数のuriをBitmapに変換したもの
     * @throws IOException 例外
     */
    @NonNull
    private Bitmap getBitmapFromUri(@NonNull Uri uri) throws IOException {
        Context context = getContext();
        assert context != null;
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * int型をString型に変換する。
     * データがない場合は空文字を返す。
     *
     * @param intData 　int型のデータ
     * @return String型に変換したデータ
     */
    public static String createStringData(int intData) {
        if (LogAddFragment.NO_DATA == intData) {
            return "";
        } else {
            return String.valueOf(intData);
        }
    }
}
