package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent.hasExtra("toast"))
        {
            getIntent().getStringExtra("toast");
            Toast.makeText(this, R.string.card_saved_toast, Toast.LENGTH_SHORT);
        }
    }

    public void onClickAddCardButton(View view)
    {
        Intent intent = new Intent(this, ViewEditCard.class);
        startActivity(intent);
    }
}