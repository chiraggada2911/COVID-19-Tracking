package co.chiraggada.trackcovid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    Button gitHubBtn1,gitHubBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        gitHubBtn1 = findViewById(R.id.githubBtn);
        gitHubBtn2 = findViewById(R.id.githubBtn2);

        gitHubBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.github.com/chiraggada2911";
                Uri parseUrl = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, parseUrl);
                startActivity(intent);
            }
        });

        gitHubBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/warrior2202";
                Uri parseUrl = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, parseUrl);
                startActivity(intent);
            }
        });

    }
}
