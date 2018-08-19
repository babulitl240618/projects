package imaginebd.com.badge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();


    @BindView(R.id.text_badge_count)
    EditText badgeCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    public void updateButtonPressed(View v) {
        int badgeCount = Integer.parseInt(badgeCountText.getText().toString());
        updateBadge(badgeCount);

    }

    private void updateBadge(int count) {
        try {
            Badges.setBadge(this, count);

        } catch (BadgesNotSupportedException badgesNotSupportedException) {
            Log.d(TAG, badgesNotSupportedException.getMessage());
        }
    }
}
