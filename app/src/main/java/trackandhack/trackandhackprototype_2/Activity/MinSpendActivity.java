package trackandhack.trackandhackprototype_2.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import trackandhack.trackandhackprototype_2.Classes.DBHelper;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.Classes.MinSpend;
import trackandhack.trackandhackprototype_2.Classes.MinSpendStatus;
import trackandhack.trackandhackprototype_2.MainActivity;
import trackandhack.trackandhackprototype_2.R;

// TODO fix update card values to save to DB
public class MinSpendActivity extends Activity {
    MinSpend minSpend;
    Button closeButton;
    DBHelper dbHelper = DBHelper.getInstance(null);
    boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min_spend);
        Intent intent = getIntent();

        getActionBar().setDisplayShowTitleEnabled(false);

        Long id = intent.getLongExtra("id", -1);
        minSpend = dbHelper.getMinSpend(id);

        setupView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_min_spend, menu);
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

    private void setupView() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView title = (TextView) findViewById(R.id.titleText);
        TextView currentAmount = (TextView) findViewById(R.id.currentAmountText);
        TextView initialAmount = (TextView) findViewById(R.id.initialAmountText);
        TextView notes = (TextView) findViewById(R.id.notesText);

        title.setText(minSpend.getTitle());
        currentAmount.setText(Double.toString(minSpend.getCurrentAmount()));
        initialAmount.setText(Double.toString(minSpend.getInitialAmount()));
        notes.setText(minSpend.getNotes());

        progressBar.setMax((int) Math.ceil(minSpend.getInitialAmount()));
        progressBar.setProgress((int) Math.ceil(minSpend.getCurrentAmount()));

        setupButtons();
    }

    private void setupButtons() {
        Button doneButton = (Button) findViewById(R.id.doneButton);
        final Button adjustmentButton = (Button) findViewById(R.id.adjustmentButton);
        closeButton = (Button) findViewById(R.id.closeButton);
        final Switch adjustmentSwitch = (Switch) findViewById(R.id.adjustmentSwitch);
        final EditText adjustmentInput = (EditText) findViewById(R.id.adjustmentInput);
        final TextView currentAmount = (TextView) findViewById(R.id.currentAmountText);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnActivity();
            }
        });

        adjustmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adjustmentDirection = 1;
                String adjustmentString = adjustmentInput.getText().toString();
                if (!adjustmentString.equals("")) {
                    Double adjustment = Double.parseDouble(adjustmentString);
                    if (adjustmentSwitch.isChecked()) {
                        adjustmentDirection = -1;
                    }
                    Double amountAfterAdjustment = minSpend.adjustCurrentAmount(adjustmentDirection * adjustment);
                    if (amountAfterAdjustment == minSpend.getInitialAmount()) {
                        setStatus(MinSpendStatus.CLOSED);
                    } else if (minSpend.getStatus().equals(MinSpendStatus.CLOSED)) {
                        setStatus(MinSpendStatus.OPEN);
                    }
                    currentAmount.setText(minSpend.getCurrentAmount().toString());
                    progressBar.setProgress(minSpend.getCurrentAmount().intValue());
                    adjustmentInput.setText(null);
                    edited = true;
                }
            }
        });

        if (minSpend.getStatus().equals(MinSpendStatus.CLOSED)) {
            closeButton.setEnabled(false);
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adjustmentDirection = -1;
                minSpend.adjustCurrentAmount(adjustmentDirection * minSpend.getInitialAmount());
                setStatus(MinSpendStatus.CLOSED);
                edited = true;
                returnActivity();
            }
        });

    }

    private void returnActivity() {
        Intent intent = new Intent(MinSpendActivity.this, MainActivity.class);
        if (edited) {
            intent.putExtra("updated", GoalType.MIN_SPEND);
            dbHelper.updateGoal(minSpend);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setStatus(MinSpendStatus status) {
        if (status.equals(MinSpendStatus.OPEN)) {
            closeButton.setEnabled(true);
            minSpend.setStatus(status);
        } else if (status.equals(MinSpendStatus.CLOSED)) {
            closeButton.setEnabled(false);
            minSpend.setStatus(status);
        }
    }
}