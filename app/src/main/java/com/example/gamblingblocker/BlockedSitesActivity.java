package com.example.gamblingblocker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;

public class BlockedSitesActivity extends AppCompatActivity {

    private TextView blockedUrlsTextView;
    private EditText newUrlEditText;
    private Button addUrlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_sites);

        blockedUrlsTextView = findViewById(R.id.blockedUrlsTextView);
        newUrlEditText = findViewById(R.id.newUrlEditText);
        addUrlButton = findViewById(R.id.addUrlButton);

        displayBlockedUrls();

        addUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUrl = newUrlEditText.getText().toString().trim();
                if (!newUrl.isEmpty()) {
                    addBlockedUrl(newUrl);
                    newUrlEditText.setText(""); // Clear input field
                    displayBlockedUrls();
                }
            }
        });
    }

    private void displayBlockedUrls() {
        Set<String> blockedUrls = getBlockedUrls();
        blockedUrlsTextView.setText(blockedUrls.isEmpty() ? "No blocked sites." : String.join("\n", blockedUrls));
    }

    private void addBlockedUrl(String url) {
        SharedPreferences prefs = getSharedPreferences("BlockedSites", MODE_PRIVATE);
        Set<String> blockedUrls = prefs.getStringSet("blockedUrls", new HashSet<>());

        blockedUrls.add(url); // Add new URL

        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("blockedUrls", blockedUrls);
        editor.apply();
    }

    private Set<String> getBlockedUrls() {
        SharedPreferences prefs = getSharedPreferences("BlockedSites", MODE_PRIVATE);
        return prefs.getStringSet("blockedUrls", new HashSet<>());
    }
}
