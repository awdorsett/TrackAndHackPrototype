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
import android.widget.ImageButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Fragments.DatePickerFragment;
import trackandhack.trackandhackprototype_2.MainActivity;
import trackandhack.trackandhackprototype_2.R;

public class NewMinSpendActivity extends Activity implements DatePickerFragment.Communicator{
    ImageButton startDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_min_spend);
        startDateButton = (ImageButton) findViewById(R.id.startDateButton);

        getActionBar().setTitle("Add New Min Spend");
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

    public void showDatePicker(View v) {
        FragmentManager fragMgr = getFragmentManager();
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(v.getId(), null);
        datePickerFragment.show(fragMgr, "datePicker");
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
}
