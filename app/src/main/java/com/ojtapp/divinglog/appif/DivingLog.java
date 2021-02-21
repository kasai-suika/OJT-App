package com.ojtapp.divinglog.appif;

import android.widget.ImageView;

import java.io.Serializable;

public class DivingLog implements Serializable {
    private static final long serialVersionID = 1;

    private int logId;
    private DivingLog log;
    private int diveNumber;
    private String place;
    private String point;
    private int depthMax;
    private int depthAve;
    private int airStart;
    private int airEnd;
    private int airDive;
    private String weather;
    private int temp;
    private int tempWater;
    private int visibility;
    private String memberNavigate;
    private String member;
    private String memo;
    private String date;
    private String timeStart;
    private String timeEnd;
    private String timeDive;
    private byte[] pictureBytes;

    /**
     * コンストラクタ
     */
    public DivingLog(){
    }

    public void setDivingLog(DivingLog log){
        this.log = log;
    }

    public int getLogId(){
        return logId;
    }

    public void setLogId(int logId){
        this.logId = logId;
    }

    public int getDivingNumber() {
        return diveNumber;
    }

    public void setDiveNumber(int diveNumber){
        this.diveNumber = diveNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getDepthMax() {
        return depthMax;
    }

    public void setDepthMax(int depthMax) {
        this.depthMax = depthMax;
    }

    public int getDepthAve() {
        return depthAve;
    }

    public void setDepthAve(int depthAve) {
        this.depthAve = depthAve;
    }

    public int getAirStart() {
        return airStart;
    }

    public void setAirStart(int airStart) {
        this.airStart = airStart;
    }

    public int getAirEnd() {
        return airEnd;
    }

    public void setAirEnd(int airEnd) {
        this.airEnd = airEnd;
    }

    public int getAirDive() {
        return airDive;
    }

    public void setAirDive(int airDive) {
        this.airDive = airDive;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String we) {
        this.weather = weather;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getTempWater() {
        return tempWater;
    }

    public void setTempWater(int tempWater) {
        this.tempWater = tempWater;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getMemberNavigate() {
        return memberNavigate;
    }

    public void setMemberNavigate(String memberNavigate) {
        this.memberNavigate = memberNavigate;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeDive() {
        return timeDive;
    }

    public void setTimeDive(String timeDive) {
        this.timeDive = timeDive;
    }

    public byte[] getPictureBytes() {
        return pictureBytes;
    }

    public void setPictureBytes(byte[] picture) {
        this.pictureBytes = picture;
    }
}
