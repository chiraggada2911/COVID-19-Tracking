package co.chiraggada.trackcovid19.NetworkSync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "https://covid19.mathdro.id/";
    public static final String BASE_URL2 = "https://pomber.github.io/";
    private static Retrofit retrofit,retrofit2 = null;

    public static Retrofit getClient(){
        String string = "";
        try{
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("");
        }catch (JSONException e){
            e.printStackTrace();
        }
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getClient2(){
        String string = "";
        try{
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONArray("");
        }catch (JSONException e){
            e.printStackTrace();
        }
        if(retrofit2 == null){
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }
}
