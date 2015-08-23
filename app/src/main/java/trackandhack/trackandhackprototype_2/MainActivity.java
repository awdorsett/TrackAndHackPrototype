package trackandhack.trackandhackprototype_2;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import trackandhack.trackandhackprototype_2.Activity.GiftCardActivity;
import trackandhack.trackandhackprototype_2.Activity.NewGiftCardActivity;
import trackandhack.trackandhackprototype_2.Classes.DBHelper;
import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.GiftCardStatus;
import trackandhack.trackandhackprototype_2.Classes.Goal;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.Classes.Group;
import trackandhack.trackandhackprototype_2.Classes.GroupListAdapter;
import trackandhack.trackandhackprototype_2.Classes.Status;


public class MainActivity extends Activity {
    List<Group> groupList;
    ExpandableListView groupExpandableListView;
    GroupListAdapter groupListAdapter;
    DrawerLayout navDrawer;
    DBHelper dbHelper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Group group;
        final Intent intent = getIntent();
        SQLiteDatabase db = openOrCreateDatabase("card_db", MODE_PRIVATE, null);
        dbHelper.setDb(db);

        getActionBar().setDisplayShowTitleEnabled(false);

        groupExpandableListView = (ExpandableListView) findViewById(R.id.groupList);

        if (intent.hasExtra("group")) {
            group = (Group) intent.getSerializableExtra("group");
        } else {
            group = DataProvider.getTempGroup();
        }

        if(intent.hasExtra("goalList")) {
            group.setGoalList((ArrayList<Goal>) intent.getSerializableExtra("goalList"));
        } else {
            group.setGoalList(getDemoGiftCardList());
        }

        groupList = new ArrayList<>();
        groupList.add(group);
        groupListAdapter = new GroupListAdapter(this, groupList);
        groupExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                GiftCard giftCard = (GiftCard) groupListAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(MainActivity.this, GiftCardActivity.class);
                intent.putExtra("giftCard", giftCard);
                startActivityForResult(intent, 1);
                return false;
            }
        });
        groupExpandableListView.setAdapter(groupListAdapter);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private List<GiftCard> getDemoGiftCardList() {
        List<GiftCard> gcList = (List<GiftCard>) dbHelper.getEntries(GoalType.GIFT_CARD);

        if (gcList.size() == 0) {
            Log.d("DATABASE", "list was empty");
            GiftCard gc = new GiftCard(500.0, "1234", null, 3.95, 500.0, null, GiftCardStatus.OPEN, "GiftCard - x1234", "Here are some notes");
            GiftCard gc2 = new GiftCard(500.0, "9999", null, 3.95, 500.0, null, GiftCardStatus.OPEN, "GiftCard - x9999", null);
            GiftCard gc3 = new GiftCard(500.0, "5550", null, 3.95, 500.0, null, GiftCardStatus.OPEN, "GiftCard - x5550", "Something something something");
            dbHelper.insertGiftCard(gc);
            dbHelper.insertGiftCard(gc2);
            dbHelper.insertGiftCard(gc3);

            gcList.add(gc);
            gcList.add(gc2);
            gcList.add(gc3);
        } else {
            Log.d("DATABASE", "list was NOT empty");
        }

        return gcList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data.hasExtra("giftCard")) {
                GiftCard giftCard = (GiftCard) data.getSerializableExtra("giftCard");
                Group group = (Group) groupListAdapter.getGroup(0);
                ArrayList<Goal> gcList = (ArrayList<Goal>) group.getGoalList();
                int position = gcList.size() - 1;
                if (gcList.contains(giftCard)) {
                    position = gcList.indexOf(giftCard);
                    gcList.remove(giftCard);
                } else {
                    dbHelper.insertGiftCard(giftCard);
                }
                gcList.add(position, giftCard);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("goalList", gcList);

                startActivity(intent);
            }
        }
    }
}
