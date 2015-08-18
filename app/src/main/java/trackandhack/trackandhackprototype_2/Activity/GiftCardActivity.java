package trackandhack.trackandhackprototype_2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.GiftCardStatus;
import trackandhack.trackandhackprototype_2.MainActivity;
import trackandhack.trackandhackprototype_2.R;

public class GiftCardActivity extends ActionBarActivity {
    GiftCard giftCard;
    Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card);
        Intent intent = getIntent();
        if (intent.hasExtra("giftCard")) {
            giftCard = (GiftCard) intent.getSerializableExtra("giftCard");
        }

        setupView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gift_card, menu);
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
        TextView digits = (TextView) findViewById(R.id.digitsText);
        TextView currentAmount = (TextView) findViewById(R.id.currentAmountText);
        TextView initialAmount = (TextView) findViewById(R.id.initialAmountText);

        title.setText(giftCard.getTitle());
        digits.setText(Integer.toString(giftCard.getDigits()));
        currentAmount.setText(Double.toString(giftCard.getCurrentAmount()));
        initialAmount.setText(Double.toString(giftCard.getInitialAmount()));

        progressBar.setMax((int) Math.ceil(giftCard.getInitialAmount()));
        progressBar.setProgress((int) Math.ceil(giftCard.getCurrentAmount()));

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
                    Double amountAfterAdjustment = giftCard.adjustCurrentAmount(adjustmentDirection * adjustment);
                    if (amountAfterAdjustment == 0) {
                        setGiftCardStatus(GiftCardStatus.CLOSED);
                    } else if (giftCard.getStatus().equals(GiftCardStatus.CLOSED)) {
                        setGiftCardStatus(GiftCardStatus.OPEN);
                    }
                    currentAmount.setText(giftCard.getCurrentAmount().toString());
                    progressBar.setProgress(giftCard.getCurrentAmount().intValue());
                    adjustmentInput.setText(null);
                }
            }
        });

        if (giftCard.getStatus().equals(GiftCardStatus.CLOSED)) {
            closeButton.setEnabled(false);
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adjustmentDirection = -1;
                giftCard.adjustCurrentAmount(adjustmentDirection * giftCard.getInitialAmount());
                setGiftCardStatus(GiftCardStatus.CLOSED);
                returnActivity();
            }
        });

    }

    private void returnActivity() {
        Intent intent = new Intent(GiftCardActivity.this, MainActivity.class);
        intent.putExtra("giftCard", giftCard);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setGiftCardStatus(GiftCardStatus status) {
        if (status.equals(GiftCardStatus.OPEN)) {
            closeButton.setEnabled(true);
            giftCard.setStatus(status);
        } else if (status.equals(GiftCardStatus.CLOSED)) {
            closeButton.setEnabled(false);
            giftCard.setStatus(status);
        }
    }
}
