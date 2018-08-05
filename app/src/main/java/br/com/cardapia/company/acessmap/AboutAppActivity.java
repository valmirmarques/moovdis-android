package br.com.cardapia.company.acessmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.crashlytics.android.Crashlytics;

import br.com.cardapia.company.acessmap.util.MoovDisContants;
import io.fabric.sdk.android.Fabric;

public class AboutAppActivity extends AppCompatActivity implements MoovDisContants {

    private static final String TAG = AboutAppActivity.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (BuildConfig.enableCrashlytics) {
            Fabric.with(this, new Crashlytics());
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));

        ImageButton btnSendMail = (ImageButton) findViewById(R.id.btn_mail_contact);
        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/html");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"valmirluizmarques@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_title));
                startActivity(Intent.createChooser(intent, getString(R.string.email_action)));
            }
        });

    }

}
