package trackandhack.trackandhackprototype_2;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import trackandhack.trackandhackprototype_2.Activity.GiftCardActivity;
import trackandhack.trackandhackprototype_2.Activity.MinSpendActivity;
import trackandhack.trackandhackprototype_2.Activity.NewGiftCardActivity;
import trackandhack.trackandhackprototype_2.Activity.NewMinSpendActivity;
import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.Goal;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.Classes.Group;
import trackandhack.trackandhackprototype_2.Classes.GroupListAdapter;
import trackandhack.trackandhackprototype_2.Classes.MinSpend;
import trackandhack.trackandhackprototype_2.Module.GroupModule;


public class MainActivity extends Activity {
    List<Group> groupList;
    ExpandableListView groupExpandableListView;
    GroupListAdapter groupListAdapter;
    HashMap<Group, List<Goal>> goalLists = new HashMap<>();
    DBHelper dbHelper =  DBHelper.getInstance(this);
    Switch showClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayShowTitleEnabled(false);
        SQLiteDatabase db = openOrCreateDatabase("card_db", MODE_PRIVATE, null);
        dbHelper.setDb(db);

        showClosed = (Switch) findViewById(R.id.closedSwitch);
        Intent intent = getIntent();
        setupGroupList();

        if (intent.hasExtra("expand")) {
            GoalType goalType = (GoalType) intent.getSerializableExtra("expand");
            for (int i = 0; i < groupListAdapter.getGroupCount(); i++) {
                Group group = (Group) groupListAdapter.getGroup(i);
                if (group.getGoalType().equals(goalType)) {
                    groupExpandableListView.expandGroup(i);
                    break;
                }
            }
        }

        showClosed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                putGoalsInList();
                groupListAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_gift_card) {
            Intent newGiftCardIntent = new Intent(this, NewGiftCardActivity.class);
            startActivityForResult(newGiftCardIntent, 1);

            return true;
        } else if (id == R.id.menu_min_spend) {
            Intent newMinSpendIntent = new Intent(this, NewMinSpendActivity.class);
            startActivityForResult(newMinSpendIntent, 1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.hasExtra("updated")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("expand", data.getSerializableExtra("updated"));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

    private void getEntriesForGroups() {
        for(Group group : groupList) {
            goalLists.put(group, (List<Goal>) dbHelper.getEntries(group.getGoalType(), true));
        }
    }

    private void setupGroupList() {
        groupExpandableListView = (ExpandableListView) findViewById(R.id.groupList);

        groupList = GroupModule.groups;
        getEntriesForGroups();
        putGoalsInList();
        groupListAdapter = new GroupListAdapter(this, groupList);
        groupListAdapter.showClosed(showClosed.isChecked());
        groupExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Group group = (Group) groupListAdapter.getGroup(groupPosition);
                Intent intent = null;
                // TODO throw error when neither GoalType is found
                if (group.getGoalType().equals(GoalType.GIFT_CARD)) {
                    intent = new Intent(MainActivity.this, GiftCardActivity.class);
                    GiftCard gc = (GiftCard) groupListAdapter.getChild(groupPosition, childPosition);
                    intent.putExtra("id", gc.getUid());
                } else if (group.getGoalType().equals(GoalType.MIN_SPEND)) {
                    intent = new Intent(MainActivity.this, MinSpendActivity.class);
                    MinSpend minSpend = (MinSpend) groupListAdapter.getChild(groupPosition, childPosition);
                    intent.putExtra("id", minSpend.getUid());
                }

                startActivityForResult(intent, 1);
                return false;
            }
        });
        groupExpandableListView.setAdapter(groupListAdapter);
    }

    private void putGoalsInList() {
        for(Group group : groupList) {
            List<Goal> openGoalList = new ArrayList<>();
            List<Goal> closedGoalList = new ArrayList<>();
            for (Goal goal : goalLists.get(group)) {
                if (goal.isClosed() && showClosed.isChecked()) {
                    closedGoalList.add(goal);
                } else if (!goal.isClosed()) {
                    openGoalList.add(goal);
                }
            }
            openGoalList.addAll(closedGoalList);
            group.setGoalList(openGoalList);
        }
    }
}
