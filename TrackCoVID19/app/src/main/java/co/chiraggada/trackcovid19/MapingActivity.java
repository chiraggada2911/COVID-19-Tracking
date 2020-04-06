package co.chiraggada.trackcovid19;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.nio.MappedByteBuffer;
import java.util.List;

import co.chiraggada.trackcovid19.Adapters.AllCountriesAdapter;
import co.chiraggada.trackcovid19.Modal.CovidCountry;
import co.chiraggada.trackcovid19.NetworkSync.ApiClient;
import co.chiraggada.trackcovid19.NetworkSync.ApiInterface;
import co.chiraggada.trackcovid19.fragments.MapFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapingActivity extends AppCompatActivity implements AllCountriesAdapter.OnCountryListener  {

   androidx.appcompat.widget.SearchView txt_search;
    RecyclerView recyclerView;
    AllCountriesAdapter allCountriesAdapter;
    ApiInterface apiInterface;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton fab_back;
    ArrayAdapter<String> filterArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        txt_search = findViewById(R.id.txt_search);
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        fab_back = findViewById(R.id.fab_back);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initComponent();
            }
        });

        initComponent();
        String mode = getIntent().getStringExtra("mode");

        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mode",mode);
        mapFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.layout_visual,mapFragment,mapFragment.getTag());
        ft.commit();

        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                allCountriesAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                allCountriesAdapter.getFilter().filter(newText);
                return false;
            }
        });

//        txt_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                allCountriesAdapter.getFilter().filter(charSequence);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

    }

    private void initComponent(){
        getAllcountries();

    }

    private void getAllcountries(){
        final KProgressHUD progressDialog = KProgressHUD.create(MapingActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        Call<List<CovidCountry>> call = apiInterface.getForMap();
        call.enqueue(new Callback<List<CovidCountry>>() {
            @Override
            public void onResponse(Call<List<CovidCountry>> call, Response<List<CovidCountry>> response) {
                List<CovidCountry> allCountries = response.body();
                populateList(allCountries);
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
//                for(int i = 1;i<allCountries.size();i++){
//                    filterArray.add(allCountries.get(i).getCountryRegion());
//                }
            }

            @Override
            public void onFailure(Call<List<CovidCountry>> call, Throwable t) {
                Toast.makeText(MapingActivity.this, "smething went wrond "+ t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populateList(List<CovidCountry> allcountry){
        allCountriesAdapter = new AllCountriesAdapter(MapingActivity.this,allcountry,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MapingActivity.this));
        recyclerView.setAdapter(allCountriesAdapter);

    }

    @Override
    public void countryListener(int position) {

    }
}
