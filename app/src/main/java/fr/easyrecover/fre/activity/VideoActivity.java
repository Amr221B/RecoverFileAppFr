package fr.easyrecover.fre.activity;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.easyrecover.fre.Constant;
import fr.easyrecover.fre.R;
import fr.easyrecover.fre.common.VideoScanner;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import net.bohush.geometricprogressview.GeometricProgressView;

public class VideoActivity extends AppCompatActivity {

    public static VideoActivity videoActivity;

    public static String PKG;
    public static String RESTORE_DIR;

    private ImageView scan_icon;
    private ImageView ic_back;

    LinearLayout show_button;
    private TextView number_text;

    GeometricProgressView progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoActivity = VideoActivity.this;

        addBanner();
        findViews();
        initViews();
    }

    private void findViews() {

        show_button = findViewById(R.id.show_button);
        number_text = findViewById(R.id.number_text);
        scan_icon = findViewById(R.id.scan_icon);
        progress = findViewById(R.id.progress);
        ic_back = findViewById(R.id.ic_back);


    }

    private void initViews() {

        PKG = getPackageName();
        RESTORE_DIR = PreferenceManager.getDefaultSharedPreferences(this).getString("restore_path", Environment.getExternalStorageDirectory().toString() + "/Delete Photo Recovery/");

        show_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);
                VideoActivity.this.number_text.setText(VideoActivity.this.getResources().getString(R.string.number_text) + " 0 " + VideoActivity.this.getResources().getString(R.string.image) + " (0 B)");
                new VideoScanner(VideoActivity.this, VideoActivity.this.number_text, VideoActivity.this.scan_icon).execute(new Void[0]);

            }
        });

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }


    public void addBanner() {


        final AdView mAdView = new AdView(VideoActivity.this);
        mAdView.setAdSize(AdSize.BANNER);
        final View adContainer = findViewById(R.id.layoutViewAdd);


        mAdView.setAdUnitId(Constant.bannerId);

        ((LinearLayout) adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);


                adContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                adContainer.setVisibility(View.VISIBLE);

            }
        });
    }

}
