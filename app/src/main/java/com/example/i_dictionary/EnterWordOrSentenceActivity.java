package com.example.i_dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;

public class EnterWordOrSentenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        StatusBarUtil.setTransparent(EnterWordOrSentenceActivity.this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_enter_word_or_sentence);
        ImageView back_img = findViewById(R.id.back_button_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back_button_img_intent = new Intent(EnterWordOrSentenceActivity.this,MainActivity.class);
                back_button_img_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(back_button_img_intent);
            }
        });
    }
}