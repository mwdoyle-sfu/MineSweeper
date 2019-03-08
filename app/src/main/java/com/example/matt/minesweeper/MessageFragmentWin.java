/**
 * MessageFragmentWin
 * @author Matt Doyle (mwdoyle), Emma Hughes (eha38)
 *
 */

package com.example.matt.minesweeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class MessageFragmentWin extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create the view to show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.win_message_layout,null);

        // Create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        };

        // Build the dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle( R.string.game_over_txt)
                .setView(v)
                .setPositiveButton("Ok", listener)
                .create();
    }
}
