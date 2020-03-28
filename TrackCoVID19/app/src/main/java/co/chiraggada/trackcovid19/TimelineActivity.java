package co.chiraggada.trackcovid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import co.chiraggada.trackcovid19.Adapters.TimeLineAdapters;
import co.chiraggada.trackcovid19.Modal.CovidTimeLine;
import co.chiraggada.trackcovid19.NetworkSync.ApiClient;
import co.chiraggada.trackcovid19.NetworkSync.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimelineActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    List<CovidTimeLine> CountryList;
    TimeLineAdapters timeLineAdapters;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        recyclerView = findViewById(R.id.timeLineRecyclerView);
        CountryList = new ArrayList<>();
        apiInterface = ApiClient.getClient2().create(ApiInterface.class);
        getTimeLine("India");
    }

    private void getTimeLine(String country){
        final KProgressHUD progressDialog = KProgressHUD.create(TimelineActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        Call<JsonObject> call = apiInterface.getTimeLine();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonArray j = response.body().getAsJsonArray("India");
                for(int i = 0; i< j.size();i++){
                    JsonObject pp = j.get(i).getAsJsonObject();
                    String td = pp.get("date").getAsString();
                    int TimelineConfirmed = pp.get("confirmed").getAsInt();
                    int TimelineDeaths = pp.get("deaths").getAsInt();
                    int TimelineRecovered = pp.get("recovered").getAsInt();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                    Date date1= null;
                    try {
                        date1 = new SimpleDateFormat("yyyy-MM-dd").parse(td);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String TimelineDate = formatter.format(date1);
                    CovidTimeLine ccc = new CovidTimeLine(TimelineDate,TimelineConfirmed,TimelineDeaths,TimelineRecovered) ;
                    CountryList.add(ccc);
                }
                populateList(CountryList);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(TimelineActivity.this, "error" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateList(List<CovidTimeLine> countryList) {

        timeLineAdapters = new TimeLineAdapters(TimelineActivity.this,countryList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(timeLineAdapters);

    }

}
