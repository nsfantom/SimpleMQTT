package tm.fantom.simplemqtt.ui;

import android.databinding.DataBindingUtil;
import android.net.wifi.ScanResult;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tm.fantom.simplemqtt.databinding.ItemWifiBinding;
import tm.fantom.simplemqtt.databinding.ItemWifiShortBinding;

public class WiFiAdapter extends RecyclerView.Adapter<WiFiAdapter.ScanHolder> {

    private ItemClickedListener itemClickedListener;
    private List<ScanResult> scanResults = new ArrayList<>();
    private boolean isShortView = true;

    @Override
    public ScanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (isShortView) {
            ItemWifiShortBinding bindingShort = ItemWifiShortBinding.inflate(inflater, parent, false);
            return new ScanHolder(bindingShort.getRoot());
        } else {
            ItemWifiBinding binding = ItemWifiBinding.inflate(inflater, parent, false);
            return new ScanHolder(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(ScanHolder holder, int position) {
        if (position != holder.getAdapterPosition()) return;

        ScanResult item = scanResults.get(position);
        if (isShortView)
            holder.bindingShort.setScanItem(item);
        else
            holder.binding.setScanItem(item);
    }

    @Override
    public int getItemCount() {
        return scanResults.size();
    }

    public void setScanResults(List<ScanResult> scanResults) {

            final WifiDiffCallback diffCallback = new WifiDiffCallback(this.scanResults, scanResults);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
            this.scanResults.clear();
            this.scanResults.addAll(scanResults);

            diffResult.dispatchUpdatesTo(this);
    }

    public void appendItems(List<ScanResult> scanResults) {
        this.scanResults.addAll(scanResults);
        notifyDataSetChanged();
    }

    public void setItemClickedListener(ItemClickedListener itemClickedListener) {
        this.itemClickedListener = itemClickedListener;
    }

    public WiFiAdapter setShortView(boolean shortView) {
        isShortView = shortView;
        return this;
    }

    interface ItemClickedListener {
        void onItemClicked(ScanResult scanResult);
    }

    class ScanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemWifiBinding binding;
        ItemWifiShortBinding bindingShort;

        ScanHolder(View itemView) {
            super(itemView);
            if (isShortView)
                bindingShort = DataBindingUtil.bind(itemView);
            else
                binding = DataBindingUtil.bind(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickedListener != null)
                itemClickedListener.onItemClicked(scanResults.get(getAdapterPosition()));
        }
    }

    class WifiDiffCallback extends DiffUtil.Callback {

        private final List<ScanResult> mOldList;
        private final List<ScanResult> mNewList;

        WifiDiffCallback(List<ScanResult> oldList, List<ScanResult> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return mNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).BSSID.equals(mNewList.get(newItemPosition).BSSID);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            final ScanResult oldScanResult = mOldList.get(oldItemPosition);
            final ScanResult newScanResult = mNewList.get(newItemPosition);
            return oldScanResult.level == newScanResult.level;
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }
}
