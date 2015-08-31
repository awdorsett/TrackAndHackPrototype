package trackandhack.trackandhackprototype_2.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import trackandhack.trackandhackprototype_2.DBHelper;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.Classes.MinSpend;
import trackandhack.trackandhackprototype_2.Classes.MinSpendStatus;
import trackandhack.trackandhackprototype_2.Fragments.DatePickerFragment;
import trackandhack.trackandhackprototype_2.MainActivity;
import trackandhack.trackandhackprototype_2.R;

public class NewMinSpendActivity extends Activity implements DatePickerFragment.Communicator{
    final static String EDIT_MODE = "edit";
    EditText title, startDate, endDate, initialAmount, currentAmount, notes;
    DateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.ENGLISH);
    Button cancelButton, doneButton;
    DBHelper dbHelper;
    Boolean editMode = false;
    MinSpend minSpend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_min_spend);
        getActionBar().setTitle("Add New Min Spend");
        dbHelper = DBHelper.getInstance(null);

        title = (EditText) findViewById(R.id.titleInput);
        startDate = (EditText) findViewById(R.id.startDateInput);
        endDate = (EditText) findViewById(R.id.endDateInput);
        initialAmount = (EditText) findViewById(R.id.amountInput);
        currentAmount = (EditText) findViewById(R.id.currentInput);
        notes = (EditText) findViewById(R.id.notesInput);
        doneButton = (Button) findViewById(R.id.doneButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        Intent intent = getIntent();
        if (intent.hasExtra("mode") && intent.getStringExtra("mode").equals(EDIT_MODE)) {
            editMode = true;
            Long id = intent.getLongExtra("id", -1);
            minSpend = dbHelper.getMinSpend(id);
            setupEditView();
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_min_spend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // TODO handle date errors
    public void showDatePicker(View v) {
        FragmentManager fragMgr = getFragmentManager();
        Calendar calendar = null;
        if (v.getId() == R.id.endDateButton ) {

            try {
                calendar = Calendar.getInstance();
                if (endDate.getText().toString().equals("")) {
                    calendar.setTime(format.parse(startDate.getText().toString()));
                    calendar.add(Calendar.MONTH, 3);
                } else {
                    calendar.setTime(format.parse(endDate.getText().toString()));
                }
            } catch (Exception e) {}
        } else if (v.getId() == R.id.startDateButton && !startDate.getText().toString().equals("")) {
            DateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.ENGLISH);
            try {
                calendar = Calendar.getInstance();
                calendar.setTime(format.parse(startDate.getText().toString()));
            } catch (Exception e) {}
        }
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(v.getId(), calendar);
        datePickerFragment.show(fragMgr, "datePicker");
    }
    // TODO handle date errors
    public void submitMinSpend(View v) {
        Double curr = Double.parseDouble(currentAmount.getText().toString());
        Double initial = Double.parseDouble(initialAmount.getText().toString());
        Date end = null, start = null;
        try {
            DateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.ENGLISH);
            end = format.parse(endDate.getText().toString());
            start = format.parse(startDate.getText().toString());
        } catch (Exception e) {}

        if (editMode) {
            minSpend.setCurrentAmount(curr);
            minSpend.setInitialAmount(initial);
            minSpend.setTitle(title.getText().toString());
            minSpend.setStartDate(start);
            minSpend.setEndDate(end);
            minSpend.setNotes(notes.getText().toString());
            dbHelper.updateMinSpend(minSpend);
        } else {
            minSpend = new MinSpend(curr, end, initial, start, MinSpendStatus.OPEN, title.getText().toString(), notes.getText().toString());
            dbHelper.insertMinSpend(minSpend);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("updated", GoalType.MIN_SPEND);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onDialogMessage(Integer day, Integer month, Integer year, Integer id) {
        EditText date;
        if (id == R.id.startDateButton) {
            date = (EditText) findViewById(R.id.startDateInput);
        } else {
            date = (EditText) findViewById(R.id.endDateInput);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy");
        dateFormat.setTimeZone(calendar.getTimeZone());
        date.setText(dateFormat.format(calendar.getTime()));
    }

    private void setupEditView() {
        title.setText(minSpend.getTitle());
        startDate.setText(format.format(minSpend.getStartDate()));
        endDate.setText(format.format(minSpend.getEndDate()));
        initialAmount.setText(minSpend.getInitialAmount().toString());
        currentAmount.setText(minSpend.getCurrentAmount().toString());
        notes.setText(minSpend.getNotes());
    }
}
