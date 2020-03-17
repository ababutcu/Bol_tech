package com.idyllix.bol_tech.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.idyllix.bol_tech.R;
import com.idyllix.bol_tech.interfaces.CustomerClickListener;
import com.idyllix.bol_tech.models.Customer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewModel> implements Filterable {

    private Context c;
    private List<Customer> list;
    private WeakReference<CustomerClickListener> listenerRef;
    private List<Customer> fullList;

    public CustomerAdapter(Context c, List<Customer> list,CustomerClickListener listener) {
        this.c = c;
        this.list = list;
        listenerRef=new WeakReference<>(listener);
        fullList=new ArrayList<>(list);
    }

    @NonNull
    @Override
    public CustomerViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customers, parent, false);
        return new CustomerViewModel(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomerViewModel holder, int position) {
        Customer object = list.get(position);
        Resources resources=c.getResources();
        holder.tvCustomerName.setText(object.getCustomerName());
        holder.tvCustomerBakiye.setText(object.getBakiye());

        if(Double.parseDouble(object.getBakiye())>0){
            holder.tvCustomerBakiye.setTextColor(resources.getColor(R.color.claret_red));
        }else if(Double.parseDouble(object.getBakiye())==0){
            holder.tvCustomerBakiye.setTextColor(resources.getColor(R.color.black));
        }else {
            holder.tvCustomerBakiye.setTextColor(resources.getColor(R.color.green_4));
        }

        holder.tvCustomerTel.setText(object.getCariTelefon());

        holder.tvCustomerTel.setOnLongClickListener(v -> {
            listenerRef.get().onPositionLongClicked(R.id.tvCustomerTel,object,object.getCariTelefon());
            return true;
        });

        holder.imageArrow.setOnClickListener(v -> listenerRef.get().onPositionClicked(object.getId()));

        holder.tvCustomerBakiye.setOnLongClickListener(v -> {
            listenerRef.get().onPositionLongClicked(R.id.tvCustomerBakiye,object,object.getCariTelefon());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return musteriFilter;
    }

    private Filter musteriFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Customer> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Customer item : fullList) {
                    if (item.getCustomerName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class CustomerViewModel extends RecyclerView.ViewHolder {

        private TextView tvCustomerName;
        private TextView tvCustomerTel;
        private TextView tvCustomerBakiye;
        private ImageButton imageArrow;

        CustomerViewModel(@NonNull View itemView) {
            super(itemView);

            tvCustomerName=itemView.findViewById(R.id.tvCustomerName);
            tvCustomerTel=itemView.findViewById(R.id.tvCustomerCariTel);
            tvCustomerBakiye=itemView.findViewById(R.id.tvCustomerBakiye);
            imageArrow=itemView.findViewById(R.id.imageArrow);
        }
    }
}
