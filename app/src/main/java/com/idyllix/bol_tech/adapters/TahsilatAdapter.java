package com.idyllix.bol_tech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.models.Collecting2;
import com.idyllix.bol_tech.utils.GetCurrentDate;

import java.util.List;

public class TahsilatAdapter extends RecyclerView.Adapter<TahsilatAdapter.TahsilatViewHolder> {

    private Context c;
    private List<Collecting2> list;

    public TahsilatAdapter(Context c, List<Collecting2> list) {
        this.c = c;
        this.list = list;
    }

    @NonNull
    @Override
    public TahsilatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tahsilat_report, parent, false);
        return new TahsilatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final TahsilatViewHolder holder, int position) {
        Collecting2 object = list.get(position);

        holder.tvUserNameTahsilat.setText(object.getUserName());
        holder.tvCustomerNameTahsilat.setText(object.getCustomerName());
        holder.tvTahsilatTarihi.setText(GetCurrentDate.dateConvertToString(object.getTarih()));
        holder.tvTahsilatMiktari.setText(object.getOdemeMiktari());
        if(!object.getAciklama().equals("")){
            holder.tvTahsilatAciklama.setText(object.getAciklama());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TahsilatViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserNameTahsilat;
        private TextView tvTahsilatTarihi;
        private TextView tvTahsilatMiktari;
        private TextView tvTahsilatAciklama;
        private TextView tvCustomerNameTahsilat;

        TahsilatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserNameTahsilat=itemView.findViewById(R.id.tvUserNameTahsilat);
            tvTahsilatTarihi=itemView.findViewById(R.id.tvTahsilatTarihi);
            tvTahsilatMiktari=itemView.findViewById(R.id.tvTahsilatMiktari);
            tvTahsilatAciklama=itemView.findViewById(R.id.tvTahsilatAciklama);
            tvCustomerNameTahsilat=itemView.findViewById(R.id.tvCustomerNameTahsilat);
        }
    }
}
