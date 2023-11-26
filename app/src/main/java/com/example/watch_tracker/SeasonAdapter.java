import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.watch_tracker.Season;
import java.util.List;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder> {

    private List<Season> seasons;
    private Context context;

    public SeasonAdapter(Context context, List<Season> seasons) {
        this.context = context;
        this.seasons = seasons;
    }

    @NonNull
    @Override
    public SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        return new SeasonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonViewHolder holder, int position) {
        holder.textView.setText(seasons.get(position).getSeasonName());
    }

    @Override
    public int getItemCount() {
        return seasons.size();
    }

    public static class SeasonViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SeasonViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
