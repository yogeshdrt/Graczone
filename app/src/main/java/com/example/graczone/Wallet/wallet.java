package com.example.graczone.Wallet;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.graczone.LOGIN.NetworkChangeListener;
import com.example.graczone.R;

public class wallet extends AppCompatActivity {

    TextView recent, balance;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.switch_frame, new BalanceFragment()).commit();


        recent = findViewById(R.id.recent_btn);
        recent.setTextSize(getResources().getDimension(R.dimen.text_balanceTransactionSmall));
        recent.setBackgroundResource(R.drawable.corners);
        recent.setOnClickListener(v -> {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.switch_frame, new RecentTransactionFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            recent.setBackgroundResource(R.drawable.corners2);
            recent.setTextColor(this.getResources().getColor(R.color.black));
            recent.setTextSize(getResources().getDimension(R.dimen.text_balanceTransactionBig));
            balance.setBackgroundResource(R.drawable.corners);
            balance.setTextColor(this.getResources().getColor(R.color.purple_500));
            balance.setTextSize(getResources().getDimension(R.dimen.text_balanceTransactionSmall));


        });


        balance = findViewById(R.id.balance_btn);
        balance.setBackgroundResource(R.drawable.corners2);
        balance.setTextSize(getResources().getDimension(R.dimen.text_balanceTransactionBig));
        balance.setTextColor(this.getResources().getColor(R.color.black));
        balance.setOnClickListener(v -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.switch_frame, new BalanceFragment());
            ft.addToBackStack(null);
            ft.commit();
            balance.setBackgroundResource(R.drawable.corners2);
            balance.setTextColor(this.getResources().getColor(R.color.black));
            balance.setTextSize(getResources().getDimension(R.dimen.text_balanceTransactionBig));
            recent.setBackgroundResource(R.drawable.corners);
            recent.setTextColor(this.getResources().getColor(R.color.purple_500));
            recent.setTextSize(getResources().getDimension(R.dimen.text_balanceTransactionSmall));

        });




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        Log.d("myTag", "call on start");
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        Log.d("myTag", "call on stop");
        super.onStop();
    }

}