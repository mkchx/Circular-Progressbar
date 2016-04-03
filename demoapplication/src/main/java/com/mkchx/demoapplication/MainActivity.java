package com.mkchx.demoapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mkchx.widget.circleprogressbar.CircleProgressBar;

public class MainActivity extends AppCompatActivity {

    private CircleProgressBar circleProgressBar;
    private int mTotal = 0;
    private boolean animate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        circleProgressBar = (CircleProgressBar) findViewById(R.id.circle_bar);

        // With animation
        if (animate) {

            circleProgressBar.setAnimate(animate);
            circleProgressBar.updateProgress(100);

        } else {

            // Or without animation
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    circleProgressBar.updateProgress(mTotal);

                    if (mTotal < 100) {
                        handler.postDelayed(this, 25);
                    }

                    mTotal++;
                }
            };

            handler.post(runnable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
