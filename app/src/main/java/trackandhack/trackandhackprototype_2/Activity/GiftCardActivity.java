package trackandhack.trackandhackprototype_2.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import trackandhack.trackandhackprototype_2.Classes.HistoryItem;
import trackandhack.trackandhackprototype_2.Classes.HistoryListAdapter;
import trackandhack.trackandhackprototype_2.DBHelper;
import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.GiftCardStatus;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.Fragments.HistoryFragment;
import trackandhack.trackandhackprototype_2.MainActivity;
import trackandhack.trackandhackprototype_2.R;

// TODO fix update card values to save to DB
public class GiftCardActivity extends Activity implements HistoryFragment.Communicator {
    int HISTORY_TAB_ID = 1;
    List<String> tabList = new ArrayList<>();
    List<HistoryItem> historyItems, pendingHistoryItems, updatedHistoryItems;
    LinearLayout actionTab;
    GiftCard giftCard;
    Button closeButton, editButton;
    DBHelper dbHelper = DBHelper.getInstance(null);
    Long giftCardId;
    boolean edited = false, updated = false;
    InputMethodManager inputManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card);
        actionTab = (LinearLayout) findViewById(R.id.action_tab);
        Intent intent = getIntent();

        getActionBar().setTitle(R.string.title_activity_gift_card);

        giftCardId = intent.getLongExtra("id", -1);
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setupTabs();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.hasExtra("updated")) {
                setupView();
                updated = true;
            }
        }
    }

    private void setupView() {
        giftCard = dbHelper.getGiftCard(giftCardId);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView title = (TextView) findViewById(R.id.titleText);
        TextView digits = (TextView) findViewById(R.id.digitsText);
        TextView currentAmount = (TextView) findViewById(R.id.currentAmountText);
        TextView initialAmount = (TextView) findViewById(R.id.initialAmountText);
        TextView notes = (TextView) findViewById(R.id.notesText);

        title.setText(giftCard.getTitle());
        digits.setText(giftCard.getDigits());
        currentAmount.setText(Double.toString(giftCard.getCurrentAmount()));
        initialAmount.setText(Double.toString(giftCard.getInitialAmount()));
        notes.setText(giftCard.getNotes());

        progressBar.setMax((int) Math.ceil(giftCard.getInitialAmount()));
        progressBar.setProgress((int) Math.ceil(giftCard.getCurrentAmount()));

        setupButtons();
        setupTabs();

        // List of items
        historyItems = dbHelper.getHistory(giftCard);
        pendingHistoryItems = new ArrayList<>();
        updatedHistoryItems = new ArrayList<>();

        // Create an array adapter
        final HistoryListAdapter adapter = new HistoryListAdapter(this, R.layout.history_list_item, historyItems);
        // Create a ListView object
        ListView listView = (ListView) findViewById(R.id.historyList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragMgr = getFragmentManager();
                HistoryFragment historyFragment = HistoryFragment.newInstance(parent.getId(), (HistoryItem) adapter.getItem(position));
                historyFragment.show(fragMgr, "historyDetails");
            }
        });
        // set the adapter to the view
        listView.setAdapter(adapter);

    }

    private void setupButtons() {
        editButton = (Button) findViewById(R.id.editButton);
        Button saveButton = (Button) findViewById(R.id.saveButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        final Button adjustmentButton = (Button) actionTab.findViewById(R.id.adjustmentButton);
        closeButton = (Button) actionTab.findViewById(R.id.closeButton);
        final Switch adjustmentSwitch = (Switch) actionTab.findViewById(R.id.adjustmentSwitch);
        final EditText adjustmentInput = (EditText) actionTab.findViewById(R.id.adjustmentInput);
        final TextView currentAmount = (TextView) actionTab.findViewById(R.id.currentAmountText);
        final ProgressBar progressBar = (ProgressBar) actionTab.findViewById(R.id.progressBar);

        saveButton.setOnClickListener(new View.OnClickListener() {
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
                    v.clearFocus();

                    adjustHistory(adjustmentDirection * adjustment);

                    edited = true;
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
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
                edited = true;
                returnActivity();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GiftCardActivity.this, NewGiftCardActivity.class);
                intent.putExtra("mode", NewGiftCardActivity.EDIT_MODE);
                intent.putExtra("id", giftCard.getUid());
                dbHelper.updateGiftCard(giftCard);
                startActivityForResult(intent, 1);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void returnActivity() {
        Intent intent = new Intent(GiftCardActivity.this, MainActivity.class);
        if (edited || updated) {
            intent.putExtra("updated", GoalType.GIFT_CARD);
            dbHelper.updateGiftCard(giftCard);
            dbHelper.insertHistory(pendingHistoryItems);
            dbHelper.updateHistory(updatedHistoryItems);
        }
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

    private void setupTabs() {
        TabHost tabs = (TabHost)findViewById(R.id.tabHost);
        tabs.setup();

        // Notes
        if (!tabList.contains("Action")) {
            TabHost.TabSpec notesTab = tabs.newTabSpec("Action");
            notesTab.setContent(R.id.action_tab);
            notesTab.setIndicator("Action");
            tabs.addTab(notesTab);
            tabList.add("Action");
        }

        // History
        if (!tabList.contains("History")) {
            TabHost.TabSpec historyTab = tabs.newTabSpec("History");
            historyTab.setIndicator("History");
            historyTab.setContent(R.id.historyList);
            tabs.addTab(historyTab);
            tabList.add("History");
            tabs.setCurrentTab(1);
        }

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                ListView listView = (ListView) findViewById(R.id.historyList);
                listView.refreshDrawableState();
            }
        });
        tabs.setCurrentTab(0);
    }

    @Override
    public void onBackPressed() {
        if (edited) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Adjustments not saved")
                    .setMessage("If you don't hit Save any adjustments will not be saved")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            finish();
        }
    }

    private void adjustHistory(Double amount) {
        Calendar calendar = Calendar.getInstance();
        HistoryItem historyItem = new HistoryItem();
        historyItem.setAmount(amount);
        historyItem.setGoalId(giftCard.getUid());
        historyItem.setDate(calendar.getTime());
        pendingHistoryItems.add(historyItem);
        historyItems.add(historyItem);
    }


    @Override
    public void onDialogMessage(HistoryItem historyItem) {
        TextView currentAmount = (TextView) actionTab.findViewById(R.id.currentAmountText);

        ListView listView = (ListView) findViewById(R.id.historyList);
        ((HistoryListAdapter)listView.getAdapter()).notifyDataSetChanged();
        currentAmount.setText(giftCard.getCurrentAmount().toString());

        updatedHistoryItems.add(historyItem);
    }

    @Override
    public void adjustGiftCard(Double adjustment) {
        giftCard.adjustCurrentAmount(adjustment);
        edited = true;
    }
}
