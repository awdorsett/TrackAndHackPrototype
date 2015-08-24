package trackandhack.trackandhackprototype_2.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import trackandhack.trackandhackprototype_2.R;

/**
 * Created by andrewdorsett on 8/23/15.
 */
public class DatePickerFragment extends DialogFragment {
    Integer day, month, year, id;
    String existingDate;
    Button cancelButton, okayButton;
    DatePicker datePicker;
    Communicator communicator;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static DatePickerFragment newInstance(int id, String date) {
        DatePickerFragment f = new DatePickerFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("date", date);
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

        View view = inflater.inflate(R.layout.fragment_show_date_picker, null);
        existingDate = getArguments().getString("date");
        id = getArguments().getInt("id");

        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okayButton = (Button) view.findViewById(R.id.okayButton);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth();
                year = datePicker.getYear();
                communicator.onDialogMessage(day, month, year, id);
                dismiss();
            }
        });
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        return view;
    }

    public interface Communicator {
        void onDialogMessage(Integer day, Integer month, Integer year, Integer id);
    }
}
