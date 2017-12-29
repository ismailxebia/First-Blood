package xebia.ismail.firstblood.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xebia.ismail.firstblood.R;
import xebia.ismail.firstblood.controller.SharedPrefManager;

/**
 * Created by Admin on 12/29/2017.
 */

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.tvResultNama)
    TextView tvResultNama;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        sharedPrefManager = new SharedPrefManager(this);

        tvResultNama.setText(sharedPrefManager.getSPNama());
    }
}
