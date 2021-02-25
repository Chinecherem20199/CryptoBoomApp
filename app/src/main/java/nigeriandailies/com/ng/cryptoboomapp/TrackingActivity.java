package nigeriandailies.com.ng.cryptoboomapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nigeriandailies.com.ng.cryptoboomapp.tracking.Tracker;

/**
 * Created by omrierez on 02.11.17.
 */

public class TrackingActivity extends AppCompatActivity {


    protected Tracker mTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker=new Tracker(this);
    }
}
