package co.chiraggada.trackcovid19.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import co.chiraggada.trackcovid19.MapingActivity;
import co.chiraggada.trackcovid19.Modal.CovidCountry;
import co.chiraggada.trackcovid19.NetworkSync.ApiClient;
import co.chiraggada.trackcovid19.NetworkSync.ApiInterface;
import co.chiraggada.trackcovid19.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "FooMaping";
    private static final LatLng PERTH = new LatLng(41.87194, 12.56738);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);


    private Marker mPerth;
    private Marker mSydney;
    private Marker mBrisbane;
    private Marker[] markerList;

    private GoogleMap mMap;
    String mode;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View view = inflater.inflate(R.layout.map, container, false);

        Bundle bundle = this.getArguments();
        mode = bundle.getString("mode");
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map_fr);
        mapFragment.getMapAsync(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        getAllcountries();
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void getAllcountries(){

    }


    @Override
    public boolean onMarkerClick(Marker marker) {// Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        Call<List<CovidCountry>> call = apiInterface.getForMap();
        call.enqueue(new Callback<List<CovidCountry>>() {
            @Override
            public void onResponse(Call<List<CovidCountry>> call, Response<List<CovidCountry>> response) {
                List<CovidCountry> allCountries = response.body();
                for(int i = 0;i< allCountries.size();i++){
                    createMarker(allCountries.get(i).getLat(),
                            allCountries.get(i).getLongi(),
                            allCountries.get(i).getProvinceState(),
                            allCountries.get(i).getConfirmed(),
                            allCountries.get(i).getDeaths(),
                            allCountries.get(i).getRecovered(),
                            allCountries.get(i).getCountryRegion());
                }

            }
            CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(20.593684,78.96288));
            @Override
            public void onFailure(Call<List<CovidCountry>> call, Throwable t) {
                Toast.makeText(getActivity(), "smething went wrond "+ t, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.animateCamera( CameraUpdateFactory.zoomTo( 4.0f ) );
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

    }

    private void createMarker(Double lat, Double longi, String provinceState, int confirmed,int death,int recovered,String Country) {
        int marker = R.drawable.img_confirmed_marker;
        String title = "";
        String snippet = "";
        switch(mode) {
            case "confirmed":
                marker= R.drawable.img_confirmed_marker;
                snippet = "Confirmed " + confirmed;
                break;
            case "death":
                marker= R.drawable.img_deaths_marker;
                snippet = "Dead "+death;
                break;
            case "recovered":
                 marker=R.drawable.img_recovered_marker;
                 snippet = "Recovered "+recovered;
        }
        if(provinceState==null){
            title = Country;
        }else{
            title = provinceState + " , " + Country;
        }
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,longi))
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(marker)));

    }
}
