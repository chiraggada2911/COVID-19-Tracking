package co.chiraggada.trackcovid19.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.chiraggada.trackcovid19.Modal.CovidCountry;
import co.chiraggada.trackcovid19.R;

public class AllCountriesAdapter extends RecyclerView.Adapter<AllCountriesAdapter.countriesViewHolder> implements Filterable {

   private List<CovidCountry> countries;
    private List<CovidCountry> countriesFiltered;
   private Context context;
   private LayoutInflater inflater;
   private OnCountryListener onCountryListener;

   public AllCountriesAdapter(Context context,List<CovidCountry> covidCountries, OnCountryListener onCountryListener){

       inflater = LayoutInflater.from(context);
       this.context = context;
       this.countries = covidCountries;
       this.onCountryListener = onCountryListener;

   }

    @NonNull
    @Override
    public AllCountriesAdapter.countriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = inflater.inflate(R.layout.item_location,parent,false);
       countriesViewHolder holder = new countriesViewHolder(view,onCountryListener);
       return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllCountriesAdapter.countriesViewHolder holder, int position) {

       final CovidCountry covidCountry = countries.get(position);
       if(covidCountry.getProvinceState()==null){
           holder.txt_location.setText(covidCountry.getCountryRegion());
       }else{
           holder.txt_location.setText(covidCountry.getProvinceState()+" , "+covidCountry.getCountryRegion());
       }
       holder.Alltxt_data.setText("Confirmed "+String.valueOf(covidCountry.getConfirmed()));
       holder.Alltxt_death.setText("Death "+String.valueOf(covidCountry.getDeaths()));
       holder.Alltxt_recovered.setText("Recovered "+String.valueOf(covidCountry.getRecovered()));

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    countriesFiltered = countries;
                } else {
                    List<CovidCountry> filteredList = new ArrayList<>();
                    for (CovidCountry row : countries) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCountryRegion().toLowerCase().contains(charString.toLowerCase()) || row.getProvinceState().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    countriesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = countriesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                countriesFiltered = (ArrayList<CovidCountry>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class countriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_location,Alltxt_data,Alltxt_death,Alltxt_recovered,Alllast_update;
        ConstraintLayout parentLayout;
        OnCountryListener onCountryListener;

        public countriesViewHolder(@NonNull View itemView,OnCountryListener onCountryListener) {
            super(itemView);

            txt_location = itemView.findViewById(R.id.Alltxt_location);
            Alltxt_data = itemView.findViewById(R.id.Alltxt_data);
            Alltxt_death = itemView.findViewById(R.id.Alltxt_death);
            Alltxt_recovered = itemView.findViewById(R.id.Alltxt_rcv);

            parentLayout = itemView.findViewById(R.id.root);

            this.onCountryListener = onCountryListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onCountryListener.countryListener(getAdapterPosition());
        }
    }

    public interface OnCountryListener{
        void countryListener(int position);
    }


}
