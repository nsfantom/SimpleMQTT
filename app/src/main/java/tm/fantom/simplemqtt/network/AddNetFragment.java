package tm.fantom.simplemqtt.network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;

import io.reactivex.subjects.PublishSubject;
import tm.fantom.simplemqtt.R;
import tm.fantom.simplemqtt.databinding.FragmentAddNetworkBinding;

public final class AddNetFragment extends DialogFragment {

    private FragmentAddNetworkBinding binding;
    private ScanResult scanResult;

    public static AddNetFragment newInstance(ScanResult scanResult) {
        AddNetFragment fragment = new AddNetFragment();
        fragment.scanResult = scanResult;
        return fragment;
    }

    private final PublishSubject<String> createClicked = PublishSubject.create();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();
//        View view = LayoutInflater.from(context).inflate(R.layout.fragment_add_network, null);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_add_network, null, false);
        binding.networkName.setText(scanResult.SSID);
//        Observable.combineLatest(createClicked, RxTextView.textChanges(binding.etPass),
//                (ignored, text) -> text.toString())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(text -> {
//                    if (!isPassValid(text)) {
//                        Toast.makeText(getActivity(), R.string.error_invalid_pass, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                });

        return new AlertDialog.Builder(context)
                .setTitle(R.string.connect_network) // Set title for dialog
                .setView(binding.getRoot()) // Set custom View
                .setPositiveButton(R.string.add, (dialog, which) -> createClicked.onNext("clicked"))
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                })
                .create();
    }

    //Check name length
    private boolean isPassValid(String text) {
        return !TextUtils.isEmpty(text) && text.length() > 7;
    }
}
