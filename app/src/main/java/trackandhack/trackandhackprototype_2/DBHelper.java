package trackandhack.trackandhackprototype_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.GiftCardStatus;
import trackandhack.trackandhackprototype_2.Classes.Goal;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.Classes.HistoryItem;
import trackandhack.trackandhackprototype_2.Classes.MinSpend;
import trackandhack.trackandhackprototype_2.Classes.MinSpendStatus;
import trackandhack.trackandhackprototype_2.Module.DBHelperModule;


/**
 * Created by andrewdorsett on 8/22/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance = null;
    SQLiteDatabase db;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    private DBHelper(Context context){
        super(context, "card_db", null, 1);
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null && context != null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public void onCreate(SQLiteDatabase db) {}

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}

    public void setDb(SQLiteDatabase db) {
        this.db = db;
        setupTables();
    }

    private void setupTables() {
        db.execSQL(DBHelperModule.CREATE_GOALS_QUERY);
        db.execSQL(DBHelperModule.CREATE_GC_QUERY);
        db.execSQL(DBHelperModule.CREATE_MS_QUERY);
        db.execSQL(DBHelperModule.CREATE_HISTORY_QUERY);
    }

    public List<?> getEntries(GoalType type, Boolean includeClosed) {
        ArrayList<Goal> entries = new ArrayList<>();
        Cursor resultSet;
        if (type.equals(GoalType.GIFT_CARD)) {
            resultSet = db.rawQuery(DBHelperModule.GET_GC_QUERY, null);

            while (resultSet.moveToNext()) {
                GiftCard gc = getGiftCardEntry(resultSet);
                if (gc != null) {
                    if (includeClosed || !gc.getStatus().equals(GiftCardStatus.CLOSED)) {
                        entries.add(gc);
                    }
                }
            }
        } else if (type.equals(GoalType.MIN_SPEND)) {
            resultSet = db.rawQuery(DBHelperModule.GET_MS_QUERY, null);

            while (resultSet.moveToNext()) {
                MinSpend gc = getMinSpendEntry(resultSet);
                if (gc != null) {
                    if (includeClosed || !gc.getStatus().equals(MinSpendStatus.CLOSED)) {
                        entries.add(gc);
                    }
                }
            }
        }

        return entries;
    }
    public List<?> getEntries(GoalType type) {
        return getEntries(type, false);
    }

    public void insertGiftCard(GiftCard gc) {
        ContentValues contentValues = new ContentValues();
        insertGoal(gc);

        contentValues.put("id", gc.getUid());
        contentValues.put("digits", gc.getDigits());
        contentValues.put("fee", gc.getFee());
        db.insert(DBHelperModule.GC_TABLE, null, contentValues);
    }

    public void insertGoal(Goal goal) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("currentAmount", goal.getCurrentAmount());
        if (goal.getEndDate() != null) {
            contentValues.put("endDate", dateFormat.format(goal.getEndDate()));
        }
        contentValues.put("initialAmount", goal.getInitialAmount());
        if (goal.getStartDate() != null) {
            contentValues.put("startDate", dateFormat.format(goal.getStartDate()));
        }
        contentValues.put("status", goal.getStatus().getName());
        contentValues.put("title", goal.getTitle());
        contentValues.put("notes", goal.getNotes());

        Long row = db.insert("Goals", null, contentValues);
        if (row != -1) {
            goal.setUid(row);
        }
    }

    private MinSpend getMinSpendEntry (Cursor rs) {
        MinSpend ms = new MinSpend();
        try {
            ms.setUid(rs.getLong(0));
            ms.setCurrentAmount(rs.getDouble(1));
            if (rs.getString(2) != null) {
                ms.setEndDate(dateFormat.parse(rs.getString(2)));
            } else {
                ms.setEndDate(null);
            }
            ms.setInitialAmount(rs.getDouble(3));
            if (rs.getString(4) != null) {
                ms.setStartDate(dateFormat.parse(rs.getString(4)));
            } else {
                ms.setStartDate(null);
            }
            ms.setStatus(MinSpendStatus.valueOf(rs.getString(5).toUpperCase()));
            ms.setTitle(rs.getString(6));
            ms.setNotes(rs.getString(7));
            ms.setBonus(rs.getDouble(8));

            return ms;
        } catch (Exception e) {
            Log.d("DATABASE", "EXCEPTION! " + e);
            return null;
        }
    }
    private GiftCard getGiftCardEntry (Cursor rs) {
        GiftCard gc = new GiftCard();
        try {
            gc.setUid(rs.getLong(0));
            gc.setCurrentAmount(rs.getDouble(1));
            if (rs.getString(2) != null) {
                gc.setEndDate(dateFormat.parse(rs.getString(2)));
            } else {
                gc.setEndDate(null);
            }
            gc.setInitialAmount(rs.getDouble(3));
            if (rs.getString(4) != null) {
                gc.setStartDate(dateFormat.parse(rs.getString(4)));
            } else {
                gc.setStartDate(null);
            }
            gc.setStatus(GiftCardStatus.valueOf(rs.getString(5).toUpperCase()));
            gc.setTitle(rs.getString(6));
            gc.setNotes(rs.getString(7));
            gc.setDigits(rs.getString(8));
            gc.setFee(rs.getDouble(9));


            return gc;
        } catch (Exception e) {
            Log.d("DATABASE", "EXCEPTION! " + e);
            return null;
        }
    }

    public void insertMinSpend(MinSpend ms) {
        ContentValues contentValues = new ContentValues();
        insertGoal(ms);

        contentValues.put("id", ms.getUid());
        contentValues.put("bonus", ms.getBonus());
        db.insert("MinSpends", null, contentValues);
    }

    public boolean updateGiftCard(GiftCard gc) {
        if (gc.getUid() == null || !updateGoal(gc)) {
            return false;
        }
        ContentValues contentValues = giftCardContentValues(gc);
        String idString = "id=" + gc.getUid();
        return db.update(DBHelperModule.GC_TABLE, contentValues, idString, null) == 1;
    }

    // TODO update min spend table when starting to use bonus and bonus type
    public boolean updateMinSpend(MinSpend ms) {
        if (ms.getUid() == null) {
            return false;
        }
        return updateGoal(ms);
//        ContentValues contentValues = minSpendContentValues(ms);
//        String idString = "id=" + ms.getUid();
//        return db.update(DBHelperModule.MIN_TABLE, contentValues, idString, null) == 1;
    }

    public boolean updateGoal(Goal goal) {
        if (goal.getUid() == null) {
            return false;
        }
        ContentValues contentValues = goalContentValues(goal);
        String idString = "id=" + goal.getUid();
        return db.update(DBHelperModule.GOAL_TABLE, contentValues, idString, null) == 1;
    }

    public GiftCard getGiftCard(Long id) {
        Cursor rs = db.rawQuery(DBHelperModule.GET_GC_BY_ID_QUERY, new String[]{id.toString()});

        return rs.moveToFirst() ? getGiftCardEntry(rs) : null;
    }

    public MinSpend getMinSpend(Long id) {
        Cursor rs = db.rawQuery(DBHelperModule.GET_MS_BY_ID_QUERY, new String[]{id.toString()});

        return rs.moveToFirst() ? getMinSpendEntry(rs) : null;
    }

    private ContentValues goalContentValues(Goal goal) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("currentAmount", goal.getCurrentAmount());
        if (goal.getEndDate() != null) {
            contentValues.put("endDate", dateFormat.format(goal.getEndDate()));
        }
        contentValues.put("initialAmount", goal.getInitialAmount());
        if (goal.getStartDate() != null) {
            contentValues.put("startDate", dateFormat.format(goal.getStartDate()));
        }
        contentValues.put("status", goal.getStatus().getName());
        contentValues.put("title", goal.getTitle());
        contentValues.put("notes", goal.getNotes());
        return contentValues;
    }

    private ContentValues giftCardContentValues(GiftCard gc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("digits", gc.getDigits());
        contentValues.put("fee", gc.getFee());
        return contentValues;
    }

    private ContentValues minSpendContentValues(MinSpend ms) {
        ContentValues contentValues = new ContentValues();
        return contentValues;
    }

    public boolean updateHistory(HistoryItem historyItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("goal_id", historyItem.getGoalId());
        contentValues.put("date", dateFormat.format(historyItem.getDate()));
        contentValues.put("notes", historyItem.getNotes());
        contentValues.put("amount", historyItem.getAmount());
        return db.update(DBHelperModule.HISTORY_TABLE, contentValues, "id = ?", new String[]{historyItem.getUid().toString()}) == 1;
    }

    // TODO optimize updating into 1 query
    public boolean updateHistory(List<HistoryItem> historyItems) {
        int count = 0;
        for (HistoryItem item : historyItems) {
            updateHistory(item);
            count++;
        }
        return count == historyItems.size();
    }

    public boolean insertHistory(List<HistoryItem> historyItems) {
        if (historyItems.size() > 0) {
            List<String> updateValues = new ArrayList<>();
            String query = DBHelperModule.INSERT_HS;
            for (int i = 0; i < historyItems.size(); i++) {
                HistoryItem hs = historyItems.get(i);
                updateValues.add(hs.getGoalId().toString());
                updateValues.add(dateFormat.format(hs.getDate()));
                updateValues.add(hs.getAmount().toString());
                updateValues.add(hs.getNotes());
                query += DBHelperModule.INSERT_HS_VALUE_SET;
            }
            query += ";";

            String[] valueArray = updateValues.toArray(new String[updateValues.size()]);

            Cursor rs = db.rawQuery(query, valueArray);

            return rs.getCount() > 0;
        }

        return false;
    }

    public void insertHistory(HistoryItem historyItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("goal_id", historyItem.getGoalId());
        contentValues.put("date", dateFormat.format(historyItem.getDate()));
        contentValues.put("notes", historyItem.getNotes());
        contentValues.put("amount", historyItem.getAmount());

        Long row = db.insert(DBHelperModule.HISTORY_TABLE, null, contentValues);

        if (row != -1) {
            historyItem.setUid(row);
        }
    }

    public List<HistoryItem> getHistory(Goal goal) {
        Cursor resultSet = db.rawQuery(DBHelperModule.GET_HS_FOR_ID, new String[]{goal.getUid().toString()});
        List<HistoryItem> historyItems = new ArrayList<>();
        while (resultSet.moveToNext()) {
            historyItems.add(getHistoryItem(resultSet));
        }

        return historyItems;
    }
    private HistoryItem getHistoryItem (Cursor rs) {
        HistoryItem hs = new HistoryItem();
        try {
            hs.setUid(rs.getLong(0));
            hs.setGoalId(rs.getLong(1));
            hs.setDate(dateFormat.parse(rs.getString(2)));
            hs.setAmount(rs.getDouble(3));
            if (rs.getString(4) != null && !rs.getString(4).isEmpty()) {
                hs.setNotes(rs.getString(4));
            }

            return hs;
        } catch (Exception e) {
            Log.d("DATABASE", "EXCEPTION! " + e);
            return null;
        }
    }


}
