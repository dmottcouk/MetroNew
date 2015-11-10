package uk.co.dmott.metronew;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by david on 09/11/15.
 */
public class PresetTempoDialogFragment extends DialogFragment{

    // Use this instance of the interface to deliver action events
    TempoOptionDialogListener mListener;
    int presetTempo;
    HashMap<String, Integer> presetTempoLookup = new HashMap<String, Integer>();


    public interface TempoOptionDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, int bpm);

        public void onDialogNegativeClick(DialogFragment dialog);
    }


    private void setPresetHashmap()
    {
        presetTempoLookup.put("Grave", 40);
        presetTempoLookup.put("Largo", 44);
        presetTempoLookup.put("Lento", 50);
        presetTempoLookup.put("Adagio", 56);
        presetTempoLookup.put("Larghetto", 60);
        presetTempoLookup.put("Andante", 66);
        presetTempoLookup.put("Andantino", 69);
        presetTempoLookup.put("Sostenuto", 76);
        presetTempoLookup.put("Comodo", 80);
        presetTempoLookup.put("Maestoso", 84);
        presetTempoLookup.put("Moderato", 88);
        presetTempoLookup.put("Allegretto", 108);
        presetTempoLookup.put("Animato", 120);
        presetTempoLookup.put("Allegro", 132);
        presetTempoLookup.put("Allegro assai", 144);
        presetTempoLookup.put("Allegro vivace", 152);
        presetTempoLookup.put("Vivace", 160);
        presetTempoLookup.put("Presto", 184);
        presetTempoLookup.put("Prestissimo", 202);


    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (TempoOptionDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement TempoOptionDialogListener");
        }
        setPresetHashmap();
    }



        @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
            //ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.Theme_Holo_Dialog_Alert);
            //AlertDialog.Builder builder = new AlertDialog.Builder(ctw);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View myDialogView = inflater.inflate(R.layout.presettempolayout, null);
        builder.setView(myDialogView);
        Spinner spinner = (Spinner) myDialogView.findViewById(R.id.presetspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.preset_tempo_options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getActivity().getBaseContext(), "Position is " + position, Toast.LENGTH_SHORT).show();
                    String[] tempo_values = getResources().getStringArray(R.array.preset_tempo_options);
                    String presetMark = tempo_values[position];
                    presetTempo = presetTempoLookup.get(presetMark);
                    
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }


            });

        builder.setIcon(R.drawable.buttonfoc);
            builder.setTitle(R.string.tempopresetlist);
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

            }
            });
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mListener.onDialogPositiveClick(PresetTempoDialogFragment.this,presetTempo);
                }
            });



     /**

        builder.setTitle(R.string.tempopresetlist)
                .setItems(R.array.preset_tempo_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Toast.makeText(getActivity().getBaseContext(), "Which is " + which, Toast.LENGTH_SHORT).show();


                    }
                });
        builder.setIcon(R.drawable.buttonfoc);

      */



        return builder.create();
    }

}
