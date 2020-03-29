package co.chiraggada.trackcovid19;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.chiraggada.trackcovid19.Adapters.AnimationHelper;
import co.chiraggada.trackcovid19.Modal.CovidTimeLine;
import co.chiraggada.trackcovid19.NetworkSync.ApiClient;
import co.chiraggada.trackcovid19.NetworkSync.ApiInterface;
import co.chiraggada.trackcovid19.NetworkSync.CheckInternetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final int[] mainColor = {
            rgb("#f1c40f"), rgb("#2ecc71"), rgb("#e74c3c")
    };
    ApiInterface apiInterface,apiInterface2;
    int confirmed, recovered, death;
    SharedPreferences.Editor editor;
    TextView txt_confirmed, txt_recovered, txt_death, pConfirmed, pRecovered, pDeath, txt_update, txt_location, txt_infog;
    ImageView info;
    SwipeRefreshLayout swipe;
    FloatingActionButton fab_recovered, fab_death, fab_confirmed, mainFAB;
    View includedIdpinned;
    SharedPreferences pref;
    DecimalFormat df = new DecimalFormat("##,##,###");
    com.github.mikephil.charting.charts.PieChart mPieChart;
    private boolean rotate = false;
    private View lyt_recovered, lyt_confirmed, lyt_death;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface2 = ApiClient.getClient2().create(ApiInterface.class);
        mPieChart = findViewById(R.id.piechart);
        txt_confirmed = findViewById(R.id.txt_confirmed);
        txt_recovered = findViewById(R.id.txt_recovered);
        txt_death = findViewById(R.id.txt_deaths);
        pConfirmed = findViewById(R.id.txt_data);
        pRecovered = findViewById(R.id.txt_rcv);
        pDeath = findViewById(R.id.txt_death);
        txt_update = findViewById(R.id.txt_update);
        txt_location = findViewById(R.id.txt_location);
        fab_confirmed = findViewById(R.id.fab_confirmed);
        fab_death = findViewById(R.id.fab_death);
        fab_recovered = findViewById(R.id.fab_recovered);
        lyt_confirmed = findViewById(R.id.lyt_confirmedFAB);
        lyt_death = findViewById(R.id.lyt_deathFAB);
        lyt_recovered = findViewById(R.id.lyt_recoveredFAB);
        includedIdpinned = findViewById(R.id.includedIdPinned);
        info = findViewById(R.id.info);
        txt_infog = findViewById(R.id.infog);
        swipe = findViewById(R.id.swipeRefreshWorld);
        swipe.setOnRefreshListener(this);


        new CheckInternetConnection(this).checkConnection();
        handleFAB();

        pref = getApplicationContext().getSharedPreferences("pref", 0);
        editor = pref.edit();

        getDetails();
//        getAllContries();
        getACountry("india");

        confirmed = pref.getInt("confirmed", 576856);
        recovered = pref.getInt("recovered", 128377);
        death = pref.getInt("death", 26819);

        txt_location.setText("India");
        loadPieChart();
        includedIdpinned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimelineActivity.class);
                startActivity(intent);
            }
        });

        txt_infog.setText("Tap to view TimeLine");

        animateTextView(00000, recovered, txt_recovered);
        animateTextView(00000, confirmed, txt_confirmed);
        animateTextView(00000, death, txt_death);

//        txt_recovered.setText(String.valueOf(df.format(recovered)));
//        txt_confirmed.setText(String.valueOf(df.format(confirmed)));
//        txt_death.setText(String.valueOf(df.format(death)));
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadPieChart() {

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(rgb("#1B232F"));
        mPieChart.setHoleRadius(75f);
        mPieChart.setDescription(null);
        mPieChart.setDrawEntryLabels(false);

        mPieChart.setCenterText(generateCenterSpannableText());

        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(confirmed, 0));
        values.add(new PieEntry(recovered, 1));
        values.add(new PieEntry(death, 2));


        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setColors(mainColor);
        PieData data = new PieData(dataSet);
        mPieChart.setData(data);

        mPieChart.animateXY(1400, 1400);

    }

    private void handleFAB() {

        AnimationHelper.initShowOut(lyt_confirmed);
        AnimationHelper.initShowOut(lyt_death);
        AnimationHelper.initShowOut(lyt_recovered);

        mainFAB = findViewById(R.id.fab);
        mainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, MapingActivity.class);
//                startActivity(intent);
                toggleFab(view);
            }
        });

        fab_recovered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapingActivity.class);
                intent.putExtra("mode", "recovered");
                startActivity(intent);
            }
        });
        fab_death.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapingActivity.class);
                intent.putExtra("mode", "death");
                startActivity(intent);
            }
        });
        fab_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapingActivity.class);
                intent.putExtra("mode", "confirmed");
                startActivity(intent);
            }
        });
    }

    private void toggleFab(View v) {
        rotate = AnimationHelper.rotateFab(v, !rotate);
        if (rotate) {
            AnimationHelper.showIn(lyt_recovered);
            AnimationHelper.showIn(lyt_death);
            AnimationHelper.showIn(lyt_confirmed);
//            back_drop.setVisibility(View.VISIBLE);
        } else {
            AnimationHelper.showOut(lyt_recovered);
            AnimationHelper.showOut(lyt_death);
            AnimationHelper.showOut(lyt_confirmed);
//            back_drop.setVisibility(View.GONE);
        }
    }

    private void getDetails() {

        Call<JsonObject> call = apiInterface.getBreifDetail();
        refreshData(true);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonElement jconfirmed = response.body().getAsJsonObject("confirmed");
                JsonObject jsonObjectc = jconfirmed.getAsJsonObject();

                JsonElement jrecovered = response.body().getAsJsonObject("recovered");
                JsonObject jsonObjectr = jrecovered.getAsJsonObject();

                JsonElement jdeath = response.body().getAsJsonObject("deaths");
                JsonObject jsonObjectd = jdeath.getAsJsonObject();
                refreshData(false);
                editor.putInt("confirmed", Integer.parseInt(String.valueOf(jsonObjectc.get("value"))));
                editor.putInt("recovered", Integer.parseInt(String.valueOf(jsonObjectr.get("value"))));
                editor.putInt("death", Integer.parseInt(String.valueOf(jsonObjectd.get("value"))));
                editor.commit();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong " + t, Toast.LENGTH_LONG).show();
                refreshData(false);
            }
        });
    }

    private void refreshData(boolean isRefresh) {
        if (isRefresh) {
            swipe.setRefreshing(true);
        } else {
            swipe.setRefreshing(false);
        }
    }

    private void getACountry(String countryName) {

        Call<JsonObject> call = apiInterface2.getTimeLine();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonArray statewise = response.body().getAsJsonArray("statewise");

                    JsonObject IndianData = statewise.get(0).getAsJsonObject();
                    String lastupdatedtime = IndianData.get("lastupdatedtime").getAsString();
                    int Confirmed = IndianData.get("confirmed").getAsInt();
                    int Deaths = IndianData.get("deaths").getAsInt();
                    int Recovered = IndianData.get("recovered").getAsInt();

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                Date date1 = null;
                try {
                    date1 = new SimpleDateFormat("dd/mm/yyyy").parse(lastupdatedtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String TimelineDate = formatter.format(date1);

                pDeath.setText(String.valueOf(Deaths));
                pRecovered.setText(String.valueOf(Recovered));
                pConfirmed.setText(String.valueOf(Confirmed));
                txt_update.setText("Updated on " + TimelineDate);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong " + t, Toast.LENGTH_LONG).show();
                refreshData(false);
            }
        });
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString(String.valueOf(df.format(confirmed + recovered + death)) + "\n Cases reported");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length() - 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 8, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length() - 14, 0);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length() - 14, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(.8f), 8, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length(), s.length(), 0);
        return s;
    }

    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textview.setText(String.valueOf(df.format(valueAnimator.getAnimatedValue())));
            }
        });
        valueAnimator.start();

    }

    private void showCustomDialog() {   // here is (People p)
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.feedbackdialog);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_follow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] strTo = {"chiraggada67@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, strTo);
                intent.putExtra(Intent.EXTRA_SUBJECT, "CoVID19 Tracker");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.setType("message/rfc822");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onRefresh() {
        getDetails();
        getACountry("india");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //Ke activity Info
            case R.id.infoMenu:
//
                break;
            //Ke activity Setting
            case R.id.settingMenu:
//
                break;
            //Ke activity About
            case R.id.aboutMenu:
//
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
//14
