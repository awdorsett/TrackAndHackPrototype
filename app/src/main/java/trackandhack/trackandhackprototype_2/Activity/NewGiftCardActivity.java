package trackandhack.trackandhackprototype_2.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.*;

import trackandhack.trackandhackprototype_2.Classes.DBHelper;
import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.MainActivity;
import trackandhack.trackandhackprototype_2.R;

public class NewGiftCardActivity extends Activity {
    GiftCard giftCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gift_card);


        getActionBar().setTitle("Add New Gift Card");

        Intent intent = getIntent();

        if(intent.hasExtra("clone")) {
            giftCard = GiftCard.clone((GiftCard) intent.getSerializableExtra("clone"));
        } else {
            giftCard = new GiftCard();
        }

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

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cardUpdated = updateCardInfo();
                // TODO throw error messages on screen about required fields
                if (cardUpdated) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("giftCard", giftCard);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // TODO validate inputs
    private boolean updateCardInfo() {
        EditText digits = (EditText) findViewById(R.id.digitsInput);
        EditText fee = (EditText) findViewById(R.id.feeInput);
        EditText amount = (EditText) findViewById(R.id.amountInput);
        EditText title = (EditText) findViewById(R.id.titleInput);
        EditText notes = (EditText) findViewById(R.id.notesInput);

        giftCard.setDigits(digits.getText().toString());
        giftCard.setFee(Double.parseDouble(fee.getText().toString()));
        giftCard.setInitialAmount(Double.parseDouble(amount.getText().toString()));
        giftCard.setCurrentAmount(Double.parseDouble(amount.getText().toString()));
        giftCard.setTitle(title.getText().toString());
        giftCard.setNotes(notes.getText().toString());

        return true;
    }
}
