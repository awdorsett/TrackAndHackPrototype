package trackandhack.trackandhackprototype_2.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import trackandhack.trackandhackprototype_2.Classes.HistoryItem;
import trackandhack.trackandhackprototype_2.R;

/**
 * Created by andrewdorsett on 9/19/15.
 */
public class HistoryFragment extends DialogFragment {
    HistoryItem historyItem;
    Communicator communicator;
    Button closedButton, editButton;

    public static HistoryFragment newInstance(int id, HistoryItem historyItem) {
        HistoryFragment f = new HistoryFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("historyItem", historyItem);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setCancelable(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_view_history, null);

        historyItem = (HistoryItem) getArguments().getSerializable("historyItem");

        TextView date = (TextView) view.findViewById(R.id.historyDateText);
        date.setText(historyItem.getDate());

        TextView amount = (TextView) view.findViewById(R.id.historyAmountText);
        amount.setText(historyItem.getAmount().toString());

        TextView notes = (TextView) view.findViewById(R.id.historyNotesText);
        TextView notesLabel = (TextView) view.findViewById(R.id.historyNotesLabel);

        if (historyItem.getNotes() == null || historyItem.getNotes().isEmpty()) {
            notes.setVisibility(View.GONE);
            notesLabel.setVisibility(View.GONE);
        } else {
            notes.setText(historyItem.getNotes());
            notes.setVisibility(View.VISIBLE);
            notesLabel.setVisibility(View.VISIBLE);
        }


        closedButton = (Button) view.findViewById(R.id.closeButton);
        closedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        editButton = (Button) view.findViewById(R.id.editButton);


        return view;
    }

    public interface Communicator {
        void onDialogMessage(HistoryItem historyItem);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() == null) {
            return;
        }

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getDialog() == null) {
            return;
        }

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
