package com.example.graczone.ui.MyMatches;

public class MyMatchesModel {
    private String mapTextView, timeTextView, dateTextView, entryFeeTextView, killTextView;

    public MyMatchesModel(String mapTextView, String timeTextView, String dateTextView, String entryFeeTextView, String killTextView) {
        this.mapTextView = mapTextView;
        this.timeTextView = timeTextView;
        this.dateTextView = dateTextView;
        this.entryFeeTextView = entryFeeTextView;
        this.killTextView = killTextView;
    }

    public String getMapTextView() {
        return mapTextView;
    }

    public void setMapTextView(String mapTextView) {
        this.mapTextView = mapTextView;
    }

    public String getTimeTextView() {
        return timeTextView;
    }

    public void setTimeTextView(String timeTextView) {
        this.timeTextView = timeTextView;
    }

    public String getDateTextView() {
        return dateTextView;
    }

    public void setDateTextView(String dateTextView) {
        this.dateTextView = dateTextView;
    }

    public String getEntryFeeTextView() {
        return entryFeeTextView;
    }

    public void setEntryFeeTextView(String entryFeeTextView) {
        this.entryFeeTextView = entryFeeTextView;
    }

    public String getKillTextView() {
        return killTextView;
    }

    public void setKillTextView(String killTextView) {
        this.killTextView = killTextView;
    }
}
