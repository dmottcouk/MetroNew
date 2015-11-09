package uk.co.dmott.metronew;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by david on 09/11/15.
 */
public class SoundOptionDialogFragment extends DialogFragment
{

    public interface SoundOptionDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    SoundOptionDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the SoundOptionDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SoundOptionDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SoundOptionDialogListener");
        }
    }






    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.buttonfoc);
        builder.setTitle(R.string.soundoption);
        builder.setMessage(R.string.soundprompt)
                .setPositiveButton(R.string.soundoptionOn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick( SoundOptionDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.soundoptionOff, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(SoundOptionDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }






}
