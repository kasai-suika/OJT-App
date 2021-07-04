package com.ojtapp.divinglog.databinding;

import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

public class LogDateBinding {
    public int diveNumber;
    public String place;
    public String point;
    public int depthMax;
    public int depthAve;
    public int airStart;
    public int airEnd;
    public String weather;
    public int temp;
    public int tempWater;
    public int visibility;
    public String memberNavigate;
    public String member;
    public String memo;
    public DatePicker date;
    public TimePicker timeStart;
    public TimePicker timeEnd;
    public ImageView picture;

    public interface EventHandlers {
        void onMakeClick(View view);
        void onDeleteClick(View view);
        void onEditClick(View view);
    }
}
