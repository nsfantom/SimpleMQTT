package tm.fantom.simplemqtt.ui;

import android.databinding.DataBindingUtil;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tm.fantom.simplemqtt.databinding.ItemWifiBinding;

public class WiFiAdapter extends RecyclerView.Adapter<WiFiAdapter.ScanHolder> {

    private ItemClickedListener itemClickedListener;
    private List<ScanResult> scanResults = new ArrayList<>();

    @Override
    public ScanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemWifiBinding binding = ItemWifiBinding.inflate(inflater, parent, false);
        return new ScanHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ScanHolder holder, int position) {
        if(position != holder.getAdapterPosition()) return;

        ScanResult item = scanResults.get(position);
        holder.binding.setScanItem(item);
    }

    @Override
    public int getItemCount() {
        return scanResults.size();
    }

    public void setScanResults(List<ScanResult> scanResults){
        this.scanResults = scanResults;
        notifyDataSetChanged();
    }

    public void appendItems(List<ScanResult> scanResults){
        this.scanResults.addAll(scanResults);
        notifyDataSetChanged();
    }

    public void setItemClickedListener(ItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }

    interface ItemClickedListener{
        void onItemClicked(ScanResult scanResult);
    }

    class ScanHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

         ItemWifiBinding binding;

        public ScanHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override public void onClick(View view) {
            if(itemClickedListener!=null) itemClickedListener.onItemClicked(scanResults.get(getAdapterPosition()));
        }
    }
}
