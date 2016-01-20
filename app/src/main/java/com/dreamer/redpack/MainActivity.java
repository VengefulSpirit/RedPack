package com.dreamer.redpack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dreamer.library.Package;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Package giftPackage;
    private Button start, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        giftPackage = (Package) findViewById(R.id.pack);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                giftPackage.start();
                break;
            case R.id.stop:
                giftPackage.stop();
                break;
        }
    }
}
