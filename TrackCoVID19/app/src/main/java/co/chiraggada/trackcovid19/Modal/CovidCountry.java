package co.chiraggada.trackcovid19.Modal;

import com.google.gson.annotations.SerializedName;

public class CovidCountry {

    @SerializedName("confirmed")private int confirmed;
    @SerializedName("countryRegion")private String countryRegion;
    @SerializedName("deaths")private int deaths;
    @SerializedName("lastUpdate")private Long lastUpdate;
    @SerializedName("lat")private Double lat;
    @SerializedName("long")private Double longi;
    @SerializedName("provinceState")private String provinceState;
    @SerializedName("recovered")private int recovered;


    public CovidCountry(int confirmed, String countryRegion, int deaths, Long lastUpdate, Double lat, Double longi, String provinceState, int recovered) {

        this.confirmed = confirmed;
        this.countryRegion = countryRegion;
        this.deaths = deaths;
        this.lastUpdate = lastUpdate;
        this.lat = lat;
        this.longi = longi;
        this.provinceState = provinceState;
        this.recovered = recovered;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public String getCountryRegion() {
        return countryRegion;
    }

    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public String getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }
}
