package com.idyllix.bol_tech.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.interfaces.ClickListener;
import com.idyllix.bol_tech.models.Service;
import com.idyllix.bol_tech.utils.GetCurrentDate;

import java.lang.ref.WeakReference;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private List<Service> list;
    private ClickListener listener;

    public ServiceAdapter(List<Service> list, ClickListener listener) {
        this.list = list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_report,
                parent, false);
        return new ServiceViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServiceViewHolder holder, int position) {
        Service service = list.get(position);

        holder.tvUserName.setText(String.valueOf(service.getAdiSoyadi()));
        holder.tvCustomerName.setText(service.getCariAdi());
        if(service.getBeginingDate()!=null) {
            holder.tvServiceStart.setText(GetCurrentDate.dateConvertToString(service.getBeginingDate()));
        }
        holder.tvServiceCode.setText(service.getServiceCode());

        if(service.getFinishingDate()!=null){
            holder.tvServiceFinish.setText(GetCurrentDate.dateConvertToString(service.getFinishingDate()));
        }else{
            holder.tvServiceFinish.setText(R.string.servis_kapatilmadi);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserName;
        private TextView tvServiceStart;
        private TextView tvServiceCode;
        private TextView tvServiceFinish;
        private TextView tvCustomerName;
        private WeakReference<ClickListener> listenerRef;

        ServiceViewHolder(@NonNull View itemView,final ClickListener listener) {
            super(itemView);

            tvUserName=itemView.findViewById(R.id.tvUserNameReport);
            tvServiceStart=itemView.findViewById(R.id.tvServiceStarts);
            tvServiceCode=itemView.findViewById(R.id.tvServiceCode2);
            tvServiceFinish=itemView.findViewById(R.id.tvServiceFinishes);
            tvCustomerName=itemView.findViewById(R.id.tvCustomerName);
            listenerRef = new WeakReference<>(listener);

            itemView.setOnClickListener(v -> listenerRef.get().onPositionClicked(ServiceViewHolder.this.getAdapterPosition()));
        }
    }
}
