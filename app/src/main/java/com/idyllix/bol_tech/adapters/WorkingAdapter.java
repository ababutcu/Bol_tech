package com.idyllix.bol_tech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.models.Work;
import com.idyllix.bol_tech.utils.GetCurrentDate;

import java.util.List;

public class WorkingAdapter extends RecyclerView.Adapter<WorkingAdapter.WorkingViewHolder> {

    private List<Work> list;
    private Context context;

    public WorkingAdapter( List<Work> list,Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public WorkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_working_report, parent, false);
        return new WorkingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkingViewHolder holder, int position) {
        Work work = list.get(position);
        String start=GetCurrentDate.dateConvertToString(work.getStartingDateTime());
        String finish="";
        String[] finishArray=null;
        if(work.getFinishingDateTime()!=null){
            finish=GetCurrentDate.dateConvertToString(work.getFinishingDateTime());
            finishArray=finish.split(" ");

        }

        if(work.getFinishingDateTime()!=null){
            holder.tvWorkingFinishes.setText(String.format(context.getResources()
                            .getString(R.string.date_placeholder_1),
                    finishArray[0],finishArray[1]));
        }else {
            holder.tvWorkingFinishes.setText(R.string.cikis_yapilmadi);
        }

        String[] startArray=start.split(" ");
        holder.tvUserNameWorking.setText(work.getUserName());
        holder.tvWorkingStarts.setText(String.format(context.getResources()
                        .getString(R.string.date_placeholder_1),
                        startArray[0],startArray[1]));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //inner class
    static class WorkingViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserNameWorking;
        private TextView tvWorkingStarts;
        private TextView tvWorkingFinishes;

        WorkingViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserNameWorking=itemView.findViewById(R.id.tvUserNameWorking);
            tvWorkingStarts=itemView.findViewById(R.id.tvWorkingStarts);
            tvWorkingFinishes=itemView.findViewById(R.id.tvWorkingFinishes);
        }
    }
}
