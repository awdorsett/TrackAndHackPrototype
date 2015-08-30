package trackandhack.trackandhackprototype_2.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import trackandhack.trackandhackprototype_2.DBHelper;
import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.MainActivity;
import trackandhack.trackandhackprototype_2.R;

public class NewGiftCardActivity extends Activity {
    public static String EDIT_MODE = "edit";
    GiftCard giftCard;
    DBHelper dbHelper;
    EditText digits, fee, amount, title, notes;
    Button doneButton, cloneButton;
    Boolean updated = false, editMode = false;
    String DEFAULT_TITLE = "Gift Card - x";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DBHelper.getInstance(null);
        setContentView(R.layout.activity_new_gift_card);
        getActionBar().setTitle("Add New Gift Card");
        setupElements();
        setupFocus();

        digits.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        Intent intent = getIntent();
        if (intent.hasExtra("mode")) {
            if (intent.getStringExtra("mode").equals(EDIT_MODE)) {
                cloneButton.setVisibility(View.INVISIBLE);
                Long id = intent.getLongExtra("id", -1);
                if (id != -1) {
                    editMode = true;
                    giftCard = dbHelper.getGiftCard(id);
                    setupEditMode();
                } else {
                    giftCard = new GiftCard();
                }
            }
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
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cardUpdated = updateCardInfo();
                // TODO throw error messages on screen about required fields
                if (cardUpdated || updated) {
                    setUpdateIntent();
                }
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updated) {
                    setUpdateIntent();
                }
                finish();
            }
        });

        cloneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO throw error if update doesn't work
                if (updateCardInfo()) {

                    Context context = getApplicationContext();
                    CharSequence text = giftCard.getDisplayTitle() + " was created";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    setupViewForClone();
                    updated = true;

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
        giftCard.setNotes(notes.getText().toString());

        if (editMode) {
            dbHelper.updateGiftCard(giftCard);
        } else {
            giftCard.setCurrentAmount(Double.parseDouble(amount.getText().toString()));
            dbHelper.insertGiftCard(giftCard);
        }

        return true;
    }

    private GiftCard setupViewForClone() {
        GiftCard clonedCard = GiftCard.clone(giftCard);
        Integer nextId = null;
        CheckBox amount = (CheckBox) findViewById(R.id.checkBox_dup_amount);
        CheckBox fee = (CheckBox) findViewById(R.id.checkBox_dup_fee);
        CheckBox title = (CheckBox) findViewById(R.id.checkBox_dup_title);
        CheckBox notes = (CheckBox) findViewById(R.id.checkBox_dup_note);

        digits.setText(null);

        if(!amount.isChecked()) {
            amount.setText(null);
            nextId = R.id.amountInput;
        }
        if(!fee.isChecked()) {
            fee.setText(null);
            nextId = nextId == null ? R.id.feeInput : nextId;
        }
        if(!title.isChecked()) {
            title.setText(null);
            nextId = nextId == null ? R.id.titleInput : nextId;
        }
        if(!notes.isChecked()) {
            notes.setText(null);
            nextId = nextId == null ? R.id.notesInput : nextId;
        }

        digits.requestFocus();
        if (nextId == null) {
            digits.setImeOptions(EditorInfo.IME_ACTION_DONE);
        } else {
            digits.setNextFocusDownId(nextId);
        }

        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);

        return clonedCard;
    }

    private void setUpdateIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("updated", GoalType.GIFT_CARD);
        setResult(RESULT_OK, intent);
    }

    private void setupFocus() {
        digits.setNextFocusDownId(R.id.amountInput);
        amount.setNextFocusDownId(R.id.feeInput);
        fee.setNextFocusDownId(R.id.titleInput);
        title.setNextFocusDownId(R.id.notesInput);
        notes.setNextFocusDownId(R.id.cloneButton);
    }

    private void setupElements() {
        digits = (EditText) findViewById(R.id.digitsInput);
        fee = (EditText) findViewById(R.id.feeInput);
        amount = (EditText) findViewById(R.id.amountInput);
        title = (EditText) findViewById(R.id.titleInput);
        notes = (EditText) findViewById(R.id.notesInput);
        cloneButton = (Button) findViewById(R.id.cloneButton);
        doneButton = (Button) findViewById(R.id.doneButton);
    }

    private void setupEditMode() {
        digits.setText(giftCard.getDigits());
        title.setText(giftCard.getTitle());
        notes.setText(giftCard.getNotes());

        if (giftCard.getInitialAmount() != null) {
            amount.setText(giftCard.getInitialAmount().toString());
        }

        if (giftCard.getFee() != null) {
            fee.setText(giftCard.getFee().toString());
        }

        if (giftCard.getDigits() != null) {
            digits.setText(giftCard.getDigits().toString());
        }
    }
}
