package com.infinitybyte.mcid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.infinitybyte.mcid.api.ChromeCustomTabAPI;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(R.string.about_app);

        MaterialButton changelog, source_code, privacy_policy, email, infinitybyte, rate_review;

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ChromeCustomTabAPI chromeCustomTabAPI = new ChromeCustomTabAPI(AboutActivity.this);

        TextView app_version = findViewById(R.id.app_version);
        app_version.setText(getString(R.string.app_version) + " " + BuildConfig.VERSION_NAME + " " + "(" + BuildConfig.VERSION_CODE + ")");

        changelog = findViewById(R.id.changelog);
        source_code = findViewById(R.id.source_code);
        privacy_policy = findViewById(R.id.privacy_policy);
        email = findViewById(R.id.email);
        infinitybyte = findViewById(R.id.vk_infinity_byte);
        rate_review = findViewById(R.id.rate_review);

        changelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, getString(R.string.coming_soon) + "...", Toast.LENGTH_SHORT).show();

            }
        });

        source_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeCustomTabAPI.OpenCustomTab(AboutActivity.this, getString(R.string.source_code_url), R.color.white);
            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeCustomTabAPI.OpenCustomTab(AboutActivity.this, getString(R.string.privacy_policy_url), R.color.white);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:ibremminer837.dev@gail.com"));
                startActivity(Intent.createChooser(emailIntent, "Send feedback"));
            }
        });

        infinitybyte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chromeCustomTabAPI.OpenCustomTab(AboutActivity.this, getString(R.string.infinity_byte_vk_group_url), R.color.white);
            }
        });

        rate_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, getString(R.string.coming_soon) + "...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}