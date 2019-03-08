/**
 * MainMenuActivity
 *
 * @author Matt Doyle (mwdoyle), Emma Hughes (eha38)
 */

package com.example.matt.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    ConstraintLayout myMenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        setupBackground();
        setupPlayGameButton();
        setupOptionsButton();
        setupHelpGameButton();
        setupText();
    }

    private void setupBackground() {
        // Declare animation and constraint layout
        myMenuLayout = findViewById(R.id.myLayout);
        animationDrawable = (AnimationDrawable) myMenuLayout.getBackground();

        // Add time changes
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
    }

    private void setupPlayGameButton() {
        Button btn = findViewById(R.id.btnPlayGame);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GameBoardActivity.makeIntent(MainMenuActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupOptionsButton() {
        Button btn = findViewById(R.id.btnOptions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionsActivity.makeIntent(MainMenuActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupHelpGameButton() {
        Button btn = findViewById(R.id.btnHelp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HelpActivity.makeIntent(MainMenuActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupText() {
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "sign_painter_text.ttf");

        TextView textView = findViewById(R.id.txtMainMenu);
        textView.setTypeface(myTypeface);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, MainMenuActivity.class);
    }
}
