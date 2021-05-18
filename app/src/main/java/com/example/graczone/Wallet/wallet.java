package com.example.graczone.Wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.graczone.R;

public class wallet extends AppCompatActivity {

    Button btn1;
    Button btn2;


    EditText enter_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn1=findViewById(R.id.transaction_btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wallet.this, transactions.class);
                startActivity(intent);
            }
        });


//prefix code---------------

        enter_amount = findViewById(R.id.enter_amount);
        final String rs = "₹";
        enter_amount.setText(rs);


        enter_amount.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(final CharSequence source, final int start, final int end, final
                    Spanned dest, final int dstart, final int dend) {
                        final int newStart = Math.max(rs.length(), dstart);
                        final int newEnd = Math.max(rs.length(), dend);
                        if (newStart != dstart || newEnd != dend) {
                            final SpannableStringBuilder builder = new SpannableStringBuilder(dest);
                            builder.replace(newStart, newEnd, source);
                            if (source instanceof Spanned) {
                                TextUtils.copySpansFrom((Spanned) source, 0, source.length(), null, builder, newStart);
                            }
                            Selection.setSelection(builder, newStart + source.length());
                            return builder;
                        } else {
                            return null;
                        }
                    }
                }
        });

//-----------------------


        btn2 = findViewById(R.id.withdraw_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wallet.this, withdraw.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
}
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
}

}