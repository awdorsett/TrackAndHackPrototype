package trackandhack.trackandhackprototype_2.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import trackandhack.trackandhackprototype_2.DBHelper;
import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.MainActivity;
import trackandhack.trackandhackprototype_2.R;

public class NewGiftCardActivity extends Activity {
    GiftCard giftCard;
    DBHelper dbHelper;
    EditText digits;
    EditText fee;
    EditText amount;
    EditText title;
    EditText notes;
    boolean sourceClonedCard = false;
    String DEFAULT_TITLE = "Gift Card - x";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gift_card);
        digits = (EditText) findViewById(R.id.digitsInput);
        fee = (EditText) findViewById(R.id.feeInput);
        amount = (EditText) findViewById(R.id.amountInput);
        title = (EditText) findViewById(R.id.titleInput);
        notes = (EditText) findViewById(R.id.notesInput);
        digits.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        getActionBar().setTitle("Add New Gift Card");

        Intent intent = getIntent();

        if(intent.hasExtra("clone")) {
            sourceClonedCard = true;
            giftCard = GiftCard.clone((GiftCard) intent.getSerializableExtra("clone"));
            setupClonedDetails();
        } else {
            giftCard = new GiftCard();
        }

        dbHelper = DBHelper.getInstance(null);

        setupButtons();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_gift_card, menu);
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

    private void setupButtons() {
        Button doneButton = (Button) findViewById(R.id.doneButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button cloneButton = (Button) findViewById(R.id.cloneButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cardUpdated = updateCardInfo();
                // TODO throw error messages on screen about required fields
                if (cardUpdated) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("updated", GoalType.GIFT_CARD);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sourceClonedCard) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("updated", GoalType.GIFT_CARD);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

        cloneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewGiftCardActivity.class);
                // TODO throw error if update doesn't work
                if (updateCardInfo()) {
                    intent.putExtra("clone", cloneGiftCard());
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    // TODO validate inputs
    private boolean updateCardInfo() {
        if (title.getText().toString().equals("")) {
            giftCard.setTitle(DEFAULT_TITLE);
        } else {
            giftCard.setTitle(title.getText().toString());
        }

        giftCard.setDigits(digits.getText().toString());
        giftCard.setFee(Double.parseDouble(fee.getText().toString()));
        giftCard.setInitialAmount(Double.parseDouble(amount.getText().toString()));
        giftCard.setCurrentAmount(Double.parseDouble(amount.getText().toString()));
        giftCard.setNotes(notes.getText().toString());

        dbHelper.insertGiftCard(giftCard);

        return true;
    }

    private GiftCard cloneGiftCard() {
        GiftCard clonedCard = GiftCard.clone(giftCard);
        CheckBox amount = (CheckBox) findViewById(R.id.checkBox_dup_amount);
        CheckBox fee = (CheckBox) findViewById(R.id.checkBox_dup_fee);
        CheckBox title = (CheckBox) findViewById(R.id.checkBox_dup_title);
        CheckBox notes = (CheckBox) findViewById(R.id.checkBox_dup_note);

        if(!amount.isChecked()) {
            clonedCard.setInitialAmount(null);
        }
        if(!fee.isChecked()) {
            clonedCard.setFee(null);
        }
        if(!title.isChecked()) {
            clonedCard.setTitle(null);
        }
        if(!notes.isChecked()) {
            clonedCard.setNotes(null);
        }

        return clonedCard;
    }

    private void setupClonedDetails() {
        digits.setText(giftCard.getDigits());
        fee.setText(giftCard.getFee().toString());
        amount.setText(giftCard.getInitialAmount().toString());
        title.setText(giftCard.getTitle());
        notes.setText(giftCard.getNotes());
    }
}
