package co.chiraggada.trackcovid19.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.chiraggada.trackcovid19.Modal.CovidTimeLine;
import co.chiraggada.trackcovid19.R;

public class TimeLineAdapters extends RecyclerView.Adapter<TimeLineAdapters.timelineViewHolder> {

    private List<CovidTimeLine> timeLine;
    private Context context;
    private LayoutInflater inflater;

    public TimeLineAdapters(Context context,List<CovidTimeLine> covidTimeLine){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.timeLine = covidTimeLine;

    }

    @NonNull
    @Override
    public TimeLineAdapters.timelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_daily,parent,false);
        timelineViewHolder holder = new timelineViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineAdapters.timelineViewHolder holder, int position) {
        final CovidTimeLine covidTimeLine = timeLine.get(position);

        holder.timelinetxt_recovered.setText("Recovered "+String.valueOf(covidTimeLine.getRecovered()));
        holder.timelinetxt_death.setText("Death "+String.valueOf(covidTimeLine.getDeaths()));
        holder.timelinetxt_confirmed.setText("Confirmed "+String.valueOf(covidTimeLine.getConfirmed()));
        holder.timelinetxt_date.setText(String.valueOf(covidTimeLine.getDate()));
    }

    @Override
    public int getItemCount() {
        return timeLine.size();
    }

    public class timelineViewHolder extends RecyclerView.ViewHolder {

        TextView timelinetxt_date,timelinetxt_confirmed,timelinetxt_recovered,timelinetxt_death;

        public timelineViewHolder(@NonNull View itemView) {
            super(itemView);

            timelinetxt_confirmed = itemView.findViewById(R.id.timelinetxt_confirmed);
            timelinetxt_date = itemView.findViewById(R.id.timelinetxt_date);
            timelinetxt_recovered = itemView.findViewById(R.id.timelinetxt_recovered);
            timelinetxt_death = itemView.findViewById(R.id.timelinetxt_death);

        }

    }

}
