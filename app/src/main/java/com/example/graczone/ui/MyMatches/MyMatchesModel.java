package com.example.graczone.ui.MyMatches;

public class MyMatchesModel {
    private String mapTextView;
    private String timeTextView;
    private String dateTextView;
    private String entryFeeTextView;
    private String killTextView;
    private String rank1TextView;
    private String rank2TextView;
    private String rank3TextView;
    private String rank2lTextView;
    private String rank3lTextView;
    private String teamUp;


    public MyMatchesModel(String mapTextView, String timeTextView, String dateTextView,
                          String entryFeeTextView, String killTextView, String rank1TextView,
                          String rank2TextView, String rank3TextView, String rank2lTextView, String rank3lTextView, String teamUp) {
        this.mapTextView = mapTextView;
        this.timeTextView = timeTextView;
        this.dateTextView = dateTextView;
        this.entryFeeTextView = entryFeeTextView;
        this.killTextView = killTextView;
        this.rank1TextView = rank1TextView;
        this.rank2TextView = rank2TextView;
        this.rank3TextView = rank3TextView;
        this.rank2lTextView = rank2lTextView;
        this.rank3lTextView = rank3lTextView;
        this.teamUp = teamUp;
    }

    public String getTeamUp() {
        return teamUp;
    }

    public void setTeamUp(String teamUp) {
        this.teamUp = teamUp;
    }

    public String getRank2lTextView() {
        return rank2lTextView;
    }

    public void setRank2lTextView(String rank2lTextView) {
        this.rank2lTextView = rank2lTextView;
    }

    public String getRank3lTextView() {
        return rank3lTextView;
    }

    public void setRank3lTextView(String rank3lTextView) {
        this.rank3lTextView = rank3lTextView;
    }

    public String getRank1TextView() {
        return rank1TextView;
    }

    public void setRank1TextView(String rank1TextView) {
        this.rank1TextView = rank1TextView;
    }

    public String getRank2TextView() {
        return rank2TextView;
    }

    public void setRank2TextView(String rank2TextView) {
        this.rank2TextView = rank2TextView;
    }

    public String getRank3TextView() {
        return rank3TextView;
    }

    public void setRank3TextView(String rank3TextView) {
        this.rank3TextView = rank3TextView;
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
