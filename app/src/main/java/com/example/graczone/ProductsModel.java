package com.example.graczone;

public class ProductsModel {
    private String time;
    private String teamup;
    private String entry_fee;
    private String rs_per_kill;

    private ProductsModel(){}

    private ProductsModel(String time,String entry_fee,String rs_per_kill, String teamup){
        this.time=time;
        this.entry_fee=entry_fee;
        this.rs_per_kill=rs_per_kill;
        this.teamup=teamup;
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
