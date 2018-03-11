package tm.fantom.simplemqtt.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import timber.log.Timber;
import tm.fantom.simplemqtt.R;
import tm.fantom.simplemqtt.databinding.FragmentMainBinding;
import tm.fantom.simplemqtt.network.Connectivity;

public final class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    interface Listener {
        void onOrgClicked(String name);
        void onNetClicked();
    }

    static MainFragment newInstance() {
        return new MainFragment();
    }

    private Listener listener;

    @Override
    public void onAttach(Context context) {
        if (!(getActivity() instanceof Listener)) {
            throw new IllegalStateException("Activity must implement fragment Listener.");
        }
        super.onAttach(context);
        listener = (Listener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_main, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnNet.setOnClickListener(v->listener.onNetClicked());
//        binding.rvOrganizations.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.rvOrganizations.setAdapter(adapter);
//        adapter.setItemClickedListener(name -> {
//            if (isConnected()) listener.onOrgClicked(name);
//        });
//        binding.errorHolder.setVisibility(View.GONE);
//        binding.orgItem.getRoot().setVisibility(View.GONE);
//        RxTextView.textChanges(binding.etRepo)
//                .filter(e -> e.length() >= 3)
//                .debounce(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::getOrg);
//        RxTextView.textChanges(binding.etRepo)
//                .filter(e -> e.length() < 3)
//                .debounce(1000, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(txt -> binding.rvLL.setVisibility(View.VISIBLE));
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void getOrg(CharSequence text) {
        if (!isConnected()) {
            return;
        }
//        binding.progressBar.setVisibility(View.VISIBLE);
//        if (disposable != null) {
//            disposable.dispose();
//        }
//        disposable = apiService
//                .getOrg(text.toString())
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::showResult, e -> showError(getString(R.string.error_404)));
    }

    private void showError(String errorText) {
        Timber.e(errorText);
//        binding.progressBar.setVisibility(View.GONE);
//        binding.orgItem.getRoot().setVisibility(View.GONE);
//        binding.rvLL.setVisibility(View.VISIBLE);
//        binding.errorHolder.setVisibility(View.VISIBLE);
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
}
