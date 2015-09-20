package trackandhack.trackandhackprototype_2.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import trackandhack.trackandhackprototype_2.Classes.HistoryItem;
import trackandhack.trackandhackprototype_2.DBHelper;
import trackandhack.trackandhackprototype_2.Module.DBHelperModule;
import trackandhack.trackandhackprototype_2.R;

/**
 * Created by andrewdorsett on 9/19/15.
 */
public class HistoryFragment extends DialogFragment {
    HistoryItem historyItem;
    Communicator communicator;
    Button closedButton, editButton, cancelButton, saveButton;
    ViewSwitcher viewSwitcher;
    DBHelper dbHelper;
    DateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.ENGLISH);
    View fragmentView;

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
        fragmentView = inflater.inflate(R.layout.fragment_view_history, null);
        dbHelper = DBHelper.getInstance(null);

        historyItem = (HistoryItem) getArguments().getSerializable("historyItem");

        viewSwitcher = (ViewSwitcher) fragmentView.findViewById(R.id.historySwitcher);

        TextView date = (TextView) fragmentView.findViewById(R.id.historyDateText);
        date.setText(format.format(historyItem.getDate()));

        TextView amount = (TextView) fragmentView.findViewById(R.id.historyAmountText);
        amount.setText(String.format("%.2f", historyItem.getAmount()));

        TextView notes = (TextView) fragmentView.findViewById(R.id.historyNotesText);

        if (historyItem.getNotes() == null || historyItem.getNotes().isEmpty()) {
            notes.setVisibility(View.GONE);
        } else {
            notes.setText(historyItem.getNotes());
            notes.setVisibility(View.VISIBLE);
        }
        setupButtons();

        return fragmentView;
    }

    public interface Communicator {
        void onDialogMessage(HistoryItem historyItem);
        void adjustGiftCard(Double adjustment);
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

    private void setupButtons() {

        closedButton = (Button) fragmentView.findViewById(R.id.closeButton);
        closedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        editButton = (Button) fragmentView.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyItem.getUid() == -1L) {
                    new AlertDialog.Builder(HistoryFragment.this.getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getResources().getString(R.string.pending_adjustment_popup_title))
                            .setMessage(getResources().getString(R.string.pending_adjustment_popup_body))
                            .setPositiveButton("Close", null)
                            .show();
                } else {
                    TextView date = (TextView) fragmentView.findViewById(R.id.historyEditDateInput);
                    date.setText(format.format(historyItem.getDate()));
                    date.setEnabled(false);

                    TextView amount = (TextView) fragmentView.findViewById(R.id.historyEditAmountInput);
                    amount.setText(String.format("%.2f", historyItem.getAmount()));

                    TextView notes = (TextView) fragmentView.findViewById(R.id.historyEditNotesLInput);
                    notes.setText(historyItem.getNotes());
                    viewSwitcher.showNext();
                }
            }
        });

        cancelButton = (Button) fragmentView.findViewById(R.id.historyEditCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.showPrevious();
            }
        });

        saveButton = (Button) fragmentView.findViewById(R.id.historyEditSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communicator.adjustGiftCard(historyItem.getAmount() * -1);

                EditText amount = (EditText) fragmentView.findViewById(R.id.historyEditAmountInput);
                EditText notes = (EditText) fragmentView.findViewById(R.id.historyEditNotesLInput);

                historyItem.setAmount(Double.parseDouble(amount.getText().toString()));
                historyItem.setNotes(notes.getText().toString());

                communicator.adjustGiftCard(historyItem.getAmount());
                communicator.onDialogMessage(historyItem);

                dismiss();
            }
        });
    }
}
