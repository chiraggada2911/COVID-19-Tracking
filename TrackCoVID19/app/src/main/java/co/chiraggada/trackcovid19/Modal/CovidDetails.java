package co.chiraggada.trackcovid19.Modal;

import com.google.gson.annotations.SerializedName;

public class CovidDetails {

    @SerializedName("confirmed")
    private int confirmed;
    @SerializedName("recovered")
    private int recovered;
    @SerializedName("death")
    private int death;

    public CovidDetails(int confirmed, int recovered, int death) {
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.death = death;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }


}
