package co.chiraggada.trackcovid19.NetworkSync;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import co.chiraggada.trackcovid19.Modal.CovidCountry;
import co.chiraggada.trackcovid19.Modal.CovidDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api")
    Call<JsonObject> getBreifDetail();

//    @GET("api/daily")

    @GET("api/confirmed")
    Call<List<CovidCountry>> getForMap();

    @GET("api/countries/{country}")
    Call<JsonObject> getCountry(@Path("country") String Country);

    @GET("data.json")
    Call<JsonObject> getTimeLine();


}
