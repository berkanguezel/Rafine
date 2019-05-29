package bgapps.rafine.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bgapps.rafine.R;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        getSupportActionBar().hide();
    }
}
