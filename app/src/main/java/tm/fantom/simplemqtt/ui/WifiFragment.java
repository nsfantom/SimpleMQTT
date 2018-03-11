package tm.fantom.simplemqtt.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import timber.log.Timber;
import tm.fantom.simplemqtt.R;
import tm.fantom.simplemqtt.databinding.FragmentWifiBinding;
import tm.fantom.simplemqtt.network.Connectivity;

public final class WifiFragment extends Fragment {

    private FragmentWifiBinding binding;
    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;
    private WiFiAdapter adapter;

    String networkSSID = "test";
    String networkPass = "qwerty123";


    private final Handler handler = new Handler();

    interface Listener {
        void onNetSelected();
    }

    static WifiFragment newInstance() {
        return new WifiFragment();
    }

    private Listener listener;

    @Override
    public void onAttach(Context context) {
        if (!(getActivity() instanceof Listener)) {
            throw new IllegalStateException("Activity must implement fragment Listener.");
        }
        super.onAttach(context);
        listener = (Listener) getActivity();
        adapter = new WiFiAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvNetworks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvNetworks.setAdapter(adapter);
        adapter.setItemClickedListener(scanResult -> {
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + scanResult.SSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.preSharedKey = "\"" + networkPass + "\"";
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            mainWifi.addNetwork(conf);

            Timber.e("connect to: %s", scanResult.SSID);
            List<WifiConfiguration> list = mainWifi.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + scanResult.SSID + "\"")) {
                    mainWifi.disconnect();
                    mainWifi.enableNetwork(i.networkId, true);
                    mainWifi.reconnect();
                    break;
                }
            }
            listener.onNetSelected();
        });
        binding.errorHolder.setVisibility(View.GONE);
        binding.btnScan.setOnClickListener(v -> mainWifi.startScan());

        mainWifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        receiverWifi = new WifiReceiver();

        if (!mainWifi.isWifiEnabled()) {
            mainWifi.setWifiEnabled(true);
        }

        doInback();
    }

    public void doInback() {
        handler.postDelayed(() -> {
            // TODO Auto-generated method stub
//            mainWifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            receiverWifi = new WifiReceiver();
//            getActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            mainWifi.startScan();
            doInback();
        }, 1000);

    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiverWifi);
        super.onPause();
    }

    @Override
    public void onResume() {
        getActivity().registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    private void getOrg(CharSequence text) {
        if (!isConnected()) {
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void showError(String errorText) {
        Timber.e(errorText);
        binding.progressBar.setVisibility(View.GONE);
        binding.rvNetworks.setVisibility(View.VISIBLE);
        binding.errorHolder.setVisibility(View.VISIBLE);
    }

    private boolean isConnected() {
        if (!Connectivity.isConnected(getContext())) {
            hideKeyboard();
            Snackbar.make(binding.llTop, R.string.no_connection, Snackbar.LENGTH_LONG)
                    .setAction(R.string.btn_go_online, view -> {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                        startActivity(intent);
                    })
                    .show();
            return false;
        }
        return true;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.llTop.getWindowToken(), 0);
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiList = mainWifi.getScanResults();
            if (wifiList != null && wifiList.size() > 0)
                adapter.setScanResults(wifiList);
        }
    }
}
