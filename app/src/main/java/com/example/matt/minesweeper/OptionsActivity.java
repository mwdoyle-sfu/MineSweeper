/**
 * OptionsActivity
 *
 * @author Matt Doyle (mwdoyle), Emma Hughes (eha38)
 */

package com.example.matt.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.matt.minesweeper.model.GameInfo;
import com.google.gson.Gson;

public class OptionsActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    private GameInfo gameInfo;
    AnimationDrawable animationDrawable;
    ConstraintLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        setTitle("Options");
        setupActionBar();
        setupBackground();
        gameInfo = GameInfo.getInstance();
        loadData();
        createGameBoardRadioButtons();
        createMinesRadioButtons();
        setupResetButton();
    }

    private void setupActionBar() {
        // Action bar back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupBackground() {
        // Declare animation and constraint layout
        myLayout = findViewById(R.id.myLayout);
        animationDrawable = (AnimationDrawable) myLayout.getBackground();

        // Add time changes
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
    }

    private void loadData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(OptionsActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("MyObject", null);
        GameInfo info = gson.fromJson(json, GameInfo.class);
        if (info != null) {
            gameInfo = info;
        }
    }

    private void createGameBoardRadioButtons() {
        RadioGroup gameBoardGroup = findViewById(R.id.radio_group_board_size);
        String[] gameBoard = getResources().getStringArray(R.array.num_rows_cols);

        // Create board size buttons
        for (int i = 0; i < gameBoard.length; i++) {
            final String numGameBoard = gameBoard[i];

            RadioButton button = new RadioButton(this);
            button.setText(numGameBoard);
            button.setTextColor(Color.parseColor("#ffffff"));
            button.setTypeface(null, Typeface.BOLD);

            // Set on-click callback
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Parse data from String
                    // Source https://stackoverflow.com/questions/8467241/how-to-parse-multi-digit-integers-from-string
                    String[] stringValues = numGameBoard.split("[^\\d]+");
                    int[] values = new int[2];
                    for (int i = 0; i < stringValues.length; i++) {
                        values[i] = Integer.parseInt(stringValues[i]);
                    }
                    gameInfo.setNumRows(values[0]);
                    gameInfo.setNumCols(values[1]);
                    saveData();
                }
            });

            // Add to radio group
            gameBoardGroup.addView(button);

            // Select default button
            // Source https://stackoverflow.com/questions/513832/how-do-i-compare-strings-in-java
            String currentGameBoardSetting = gameInfo.getNumRows() + " rows by " + gameInfo.getNumCols() + " columns";
            if (numGameBoard.equals(currentGameBoardSetting)) {
                button.setChecked(true);
            }
        }
    }

    private void createMinesRadioButtons() {
        RadioGroup minesGroup = findViewById(R.id.radio_group_num_mines);
        int[] minesButtons = getResources().getIntArray(R.array.num_mines);

        // Create board size buttons
        for (int i = 0; i < minesButtons.length; i++) {
            final int numMinesButton = minesButtons[i];

            RadioButton button = new RadioButton(this);
            button.setText(getString(R.string.num_mines, numMinesButton));
            button.setTextColor(Color.parseColor("#ffffff"));
            button.setTypeface(null, Typeface.BOLD);


            // Set on-click callback
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameInfo.setNumMines(numMinesButton);
                    saveData();
                }
            });

            // Add to radio group
            minesGroup.addView(button);

            // Select default button
            if (numMinesButton == gameInfo.getNumMines()) {
                button.setChecked(true);
            }
        }
    }

    private void setupResetButton() {
        Button btn = findViewById(R.id.btnResetNumPlayed);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameInfo.setNumTimesPlayed(0);
                saveData();
            }

        });
    }

    private void saveData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(OptionsActivity.this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gameInfo);
        editor.putString("MyObject", json);
        editor.apply();
    }
}