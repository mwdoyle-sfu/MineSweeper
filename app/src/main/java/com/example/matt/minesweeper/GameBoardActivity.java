/**
 * GameBoardActivity
 *
 * @author Matt Doyle (mwdoyle), Emma Hughes (eha38)
 */

package com.example.matt.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.matt.minesweeper.model.Game;
import com.example.matt.minesweeper.model.GameInfo;
import com.google.gson.Gson;

import java.util.Locale;

public class GameBoardActivity extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    ConstraintLayout myMenuLayout;
    Button buttons[][];
    private GameInfo gameInfo;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        setTitle("Game Board");
        setupActionBar();
        setupBackground();
        setupUIGameBoard();
        setText();
    }

    private void setupActionBar() {
        // Action bar back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupBackground() {
        // Declare animation and constraint layout
        myMenuLayout = findViewById(R.id.myLayout);
        animationDrawable = (AnimationDrawable) myMenuLayout.getBackground();

        // Add time changes
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();
    }

    private void setupUIGameBoard() {
        gameInfo = GameInfo.getInstance();
        loadData();
        game = new Game(gameInfo);
        buttons = new Button[game.getNumRow()][game.getNumCol()];
        populateButtons();
    }

    private void loadData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(GameBoardActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("MyObject", null);
        GameInfo info = gson.fromJson(json, GameInfo.class);

        if (info != null) {
            gameInfo = info;
        }
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForButtons);

        for (int row = 0; row < game.getNumRow(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);

            for (int col = 0; col < game.getNumCol(); col++) {
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                Button button = new Button(this);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));

                // Make text not clip on small buttons
                button.setPadding(0, 0, 0, 0);
                button.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridButtonClicked(FINAL_COL, FINAL_ROW);
                    }
                });
                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }
    }

    private void gridButtonClicked(int col, int row) {
        Button button = buttons[row][col];
        // Lock Button Sizes:
        lockButtonSizes();

        // Scan animation
        scanAnimation(row, col);

        // If mine but the mine location has already been scanned
        if (game.checkIfMine(row, col) == -1 && game.getButtonsSelected(row, col) > 1) {
            // Do nothing
        }

        // If mine and the button has already been pressed
        if (game.checkIfMine(row, col) == -1 && game.getButtonsSelected(row, col) == 1) {

            game.incrementClicks();
            game.incrementButtonsSelected(row, col);
            int minesAtPosition = game.checkForMinesAtMine(row, col);
            game.setBoardGame(row, col, minesAtPosition);

            // Text is set but invisible in front of black background
            button.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black_24dp);
            button.setTextColor(Color.parseColor("#ffffff"));
            button.setText("" + minesAtPosition);
            setText();
        }

        // If  mine and the button has not been pressed
        if (game.checkIfMine(row, col) == -1 && game.getButtonsSelected(row, col) == 0) {

            playSound();

            // Set to background picture (ie. mine)
            button.setBackgroundResource(R.drawable.ic_new_releases_black_24dp);

            // Increment mines found
            game.incrementMinesFound();

            // Reduce the count of mines in that specific row/column
            game.reduceMineCount(row, col);

            // Update button in that specific row/column has been pressed
            game.incrementButtonsSelected(row, col);
            updateUI(col, row);
            setText();
        }

        // If no mine, set button text
        if (game.checkIfMine(row, col) != -1 && game.getButtonsSelected(row, col) == 0) {
            game.incrementClicks();
            game.incrementButtonsSelected(row, col);
            button.setText("" + game.checkIfMine(row, col));
            setText();
        }

        // If all mines found
        if (game.getMinesFound() == game.getMines()) {
            disableGameBoard();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            MessageFragmentWin dialog = new MessageFragmentWin();
            dialog.show(manager, "MessageDialog");

            gameInfo.incrementTimePlayed();
            saveData();
        }
    }

    // Source: https://www.youtube.com/watch?v=9oj4f8721LM
    // Sound file source: https://www.freesoundeffects.com/free-sounds/explosion-10070/
    private void playSound() {
        final MediaPlayer explosion = MediaPlayer.create(this, R.raw.explosion);
        explosion.start();
    }

    // Source: https://www.youtube.com/watch?v=g3O2ZUQOirs
    private void scanAnimation(int row, int col) {
        final Animation animAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_alpha);

        // Scan button clicked
        Button button = buttons[row][col];
        button.startAnimation(animAlpha);

        // Scan rows
        for (int i = 0; i < game.getNumCol(); i++) {
            Button btn = buttons[row][i];
            btn.startAnimation(animAlpha);
        }

        // Scan columns
        for (int j = 0; j < game.getNumRow(); j++) {
            Button btn = buttons[j][col];
            btn.startAnimation(animAlpha);
        }
    }

    private void lockButtonSizes() {
        for (int row = 0; row < game.getNumRow(); row++) {
            for (int col = 0; col < game.getNumCol(); col++) {
                Button button = buttons[row][col];
                // Locking width and height
                int width = button.getWidth();
                button.setMaxWidth(width);
                button.setMinWidth(width);

                int height = button.getHeight();
                button.setMaxHeight(height);
                button.setMinHeight(height);
            }
        }
    }

    private void setText() {
        TextView tvFound = findViewById(R.id.txtFound);
        String found = String.format(Locale.getDefault(),
                getString( R.string.found_n_of_n_mines),
                game.getMinesFound(),
                gameInfo.getNumMines());
        tvFound.setText(found);

        TextView tvScans = findViewById(R.id.txtNumScans);
        String scans = String.format(Locale.getDefault(),
                getString( R.string.num_scans_used),
                game.getClicks());
        tvScans.setText(scans);

        TextView tvPlayed = findViewById(R.id.txtTimesPlayed);
        String played = String.format(Locale.getDefault(),
                getString( R.string.times_played),
                gameInfo.getNumTimesPlayed());
        tvPlayed.setText(played);
    }

    private void updateUI(int col, int row) {
        // Change the buttons in the row
        for (int i = 0; i < game.getNumCol(); i++) {
            Button btn = buttons[row][i];

            if (game.getBoardGamePosition(row, i) != -1 && game.getButtonsSelected(row, i) > 0) {
                btn.setText("" + game.getBoardGamePosition(row, i));
            }
        }

        // Change the buttons in the column
        for (int j = 0; j < game.getNumRow(); j++) {
            Button btn = buttons[j][col];
            if (game.getBoardGamePosition(j, col) != -1 && game.getButtonsSelected(j, col) > 0) {

                btn.setText("" + game.getBoardGamePosition(j, col));
            }
        }
    }

    private void saveData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(GameBoardActivity.this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(gameInfo);
        editor.putString("MyObject", json);
        editor.apply();
    }

    private void disableGameBoard() {
        for (int i = 0; i < game.getNumRow(); i++) {
            for (int j = 0; j < game.getNumCol(); j++) {
                Button btn = buttons[i][j];
                btn.setEnabled(false);
            }
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, GameBoardActivity.class);
    }
}