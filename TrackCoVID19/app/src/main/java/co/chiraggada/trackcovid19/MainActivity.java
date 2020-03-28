package co.chiraggada.trackcovid19;

import android.animation.ValueAnimator;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.chiraggada.trackcovid19.Adapters.AnimationHelper;
import co.chiraggada.trackcovid19.NetworkSync.ApiClient;
import co.chiraggada.trackcovid19.NetworkSync.ApiInterface;
import co.chiraggada.trackcovid19.NetworkSync.CheckInternetConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class MainActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    int confirmed, recovered, death;
    SharedPreferences.Editor editor;
    TextView txt_confirmed, txt_recovered, txt_death,pConfirmed,pRecovered,pDeath,txt_update,txt_location,txt_infog;
    ImageView info;
    private boolean rotate = false;
    private View lyt_recovered,lyt_confirmed,lyt_death;
    FloatingActionButton fab_recovered,fab_death,fab_confirmed,mainFAB;
    View includedIdpinned;
    SharedPreferences pref;
    DecimalFormat df = new DecimalFormat("##,##,###");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        com.github.mikephil.charting.charts.PieChart mPieChart =  findViewById(R.id.piechart);
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

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(rgb("#1B232F"));
        mPieChart.setHoleRadius(75f);
        mPieChart.setDescription(null);
        mPieChart.setDrawEntryLabels(false);

        mPieChart.setCenterText(generateCenterSpannableText());

        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(confirmed,0));
        values.add(new PieEntry(recovered,1));
        values.add(new PieEntry(death,2));


        PieDataSet dataSet = new PieDataSet(values,"");
        dataSet.setColors(mainCOlor);
        PieData data = new PieData(dataSet);
        mPieChart.setData(data);

        mPieChart.animateXY(1400, 1400);

        includedIdpinned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TimelineActivity.class);
                startActivity(intent);
            }
        });

        txt_infog.setText("Tap to view TimeLine");

        animateTextView(00000,recovered,txt_recovered);
        animateTextView(00000,confirmed,txt_confirmed);
        animateTextView(00000,death,txt_death);

//        txt_recovered.setText(String.valueOf(df.format(recovered)));
//        txt_confirmed.setText(String.valueOf(df.format(confirmed)));
//        txt_death.setText(String.valueOf(df.format(death)));
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

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
                intent.putExtra("mode","recovered");
                startActivity(intent);
            }
        });
        fab_death.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapingActivity.class);
                intent.putExtra("mode","death");
                startActivity(intent);
            }
        });
        fab_confirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapingActivity.class);
                intent.putExtra("mode","confirmed");
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

//        final KProgressHUD progressDialog = KProgressHUD.create(MainActivity.this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(false)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();

        Call<JsonObject> call = apiInterface.getBreifDetail();
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonElement jconfirmed = response.body().getAsJsonObject("confirmed");
                JsonObject jsonObjectc = jconfirmed.getAsJsonObject();

                JsonElement jrecovered = response.body().getAsJsonObject("recovered");
                JsonObject jsonObjectr = jrecovered.getAsJsonObject();

                JsonElement jdeath = response.body().getAsJsonObject("deaths");
                JsonObject jsonObjectd = jdeath.getAsJsonObject();

                editor.putInt("confirmed", Integer.parseInt(String.valueOf(jsonObjectc.get("value"))));
                editor.putInt("recovered", Integer.parseInt(String.valueOf(jsonObjectr.get("value"))));
                editor.putInt("death", Integer.parseInt(String.valueOf(jsonObjectd.get("value"))));
                editor.commit();

//                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "nahi aya " + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getACountry(String countryName) {

        Call<JsonObject> call = apiInterface.getCountry(countryName);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonElement jconfirmed = response.body().getAsJsonObject("confirmed");
                JsonObject jsonObjectc = jconfirmed.getAsJsonObject();

                JsonElement jrecovered = response.body().getAsJsonObject("recovered");
                JsonObject jsonObjectr = jrecovered.getAsJsonObject();

                JsonElement jdeath = response.body().getAsJsonObject("deaths");
                JsonObject jsonObjectd = jdeath.getAsJsonObject();

                JsonElement lastUpdate = response.body().getAsJsonPrimitive("lastUpdate");

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                Date date1= null;
                try {
                    date1 = new SimpleDateFormat("yyyy-MM-dd").parse(lastUpdate.getAsString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String TimelineDate = formatter.format(date1);

                pDeath.setText(String.valueOf(jsonObjectd.get("value")));
                pRecovered.setText(String.valueOf(jsonObjectr.get("value")));
                pConfirmed.setText(String.valueOf(jsonObjectc.get("value")));
                txt_update.setText("Updated on "+TimelineDate);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong " + t, Toast.LENGTH_LONG).show();
            }
        });
    }
    public static final int[] mainCOlor = {
            rgb("#f1c40f"), rgb("#2ecc71"), rgb("#e74c3c")
    };
    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString(String.valueOf(df.format(confirmed+recovered+death)) + "\n Cases reported");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length()-14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 8, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length()-14, 0);
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length()-14 , 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length() , 0);
        s.setSpan(new RelativeSizeSpan(.8f), 8, s.length() , 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() , s.length(), 0);
        return s;
    }

    public void animateTextView(int initialValue, int finalValue, final TextView  textview) {
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
                String[] strTo = { "chiraggada67@gmail.com" };
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

}
//14
