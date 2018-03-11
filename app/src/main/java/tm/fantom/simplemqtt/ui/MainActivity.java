package tm.fantom.simplemqtt.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tm.fantom.simplemqtt.R;

public class MainActivity extends AppCompatActivity implements MainFragment.Listener, WifiFragment.Listener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, MainFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onOrgClicked(String name) {

    }

    @Override
    public void onNetClicked() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .replace(android.R.id.content, WifiFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onNetSelected() {
        onBackPressed();
    }
}
