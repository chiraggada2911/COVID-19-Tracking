package co.chiraggada.trackcovid19.Modal;

import com.google.gson.annotations.SerializedName;

public class CovidTimeLine {

    @SerializedName("date")private String date;
    @SerializedName("confirmed")private int confirmed;
    @SerializedName("deaths")private int deaths;
    @SerializedName("recovered")private int recovered;

    public CovidTimeLine(String date, int confirmed, int deaths, int recovered) {
        this.date = date;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }
}
