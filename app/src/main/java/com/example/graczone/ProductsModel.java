package com.example.graczone;

public class ProductsModel {
    private String time;
    private String teamup;
    private String entry_fee;
    private String rs_per_kill;
    private String count;
    private String match;

    private String map;

    private ProductsModel(String time, String entry_fee, String rs_per_kill, String map, String date, String teamup, String rank1, String rank2, String rank3, String count, String match) {
        this.time = time;
        this.entry_fee = entry_fee;
        this.rs_per_kill = rs_per_kill;
        this.teamup = teamup;
        this.rank1 = rank1;
        this.rank2 = rank2;
        this.rank3 = rank3;
        this.date = date;
        this.map = map;
        this.count = count;
        this.match = match;

    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getMap() {
        return map;
    }

    private String date;
    private String rank1;
    private String rank2;
    private String rank3;

    public void setMap(String map) {
        this.map = map;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRank1() {
        return rank1;
    }

    public void setRank1(String rank1) {
        this.rank1 = rank1;
    }

    public String getRank2() {
        return rank2;
    }

    public void setRank2(String rank2) {
        this.rank2 = rank2;
    }

    public String getRank3() {
        return rank3;
    }


    private ProductsModel() {
    }

    public void setRank3(String rank3) {
        this.rank3 = rank3;
    }

    public String getTeamup() {
        return teamup;
    }

    public void setTeamup(String teamup) {
        this.teamup = teamup;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEntry_fee() {
        return entry_fee;
    }

    public void setEntry_fee(String entry_fee) {
        this.entry_fee = entry_fee;
    }

    public String getRs_per_kill() {
        return rs_per_kill;
    }

    public void setRs_per_kill(String rs_per_kill) {
        this.rs_per_kill = rs_per_kill;
    }


}
