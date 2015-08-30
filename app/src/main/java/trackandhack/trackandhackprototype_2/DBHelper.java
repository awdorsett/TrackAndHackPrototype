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

import trackandhack.trackandhackprototype_2.Classes.GiftCard;
import trackandhack.trackandhackprototype_2.Classes.GiftCardStatus;
import trackandhack.trackandhackprototype_2.Classes.Goal;
import trackandhack.trackandhackprototype_2.Classes.GoalType;
import trackandhack.trackandhackprototype_2.Classes.MinSpend;
import trackandhack.trackandhackprototype_2.Classes.MinSpendStatus;
import trackandhack.trackandhackprototype_2.Module.DBHelperModule;


/**
 * Created by andrewdorsett on 8/22/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance = null;
    SQLiteDatabase db;
    SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            contentValues.put("endDate",  iso8601Format.format(goal.getEndDate()));
        }
        contentValues.put("initialAmount", goal.getInitialAmount());
        if (goal.getStartDate() != null) {
            contentValues.put("startDate", iso8601Format.format(goal.getStartDate()));
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
                ms.setEndDate(iso8601Format.parse(rs.getString(2)));
            } else {
                ms.setEndDate(null);
            }
            ms.setInitialAmount(rs.getDouble(3));
            if (rs.getString(4) != null) {
                ms.setStartDate(iso8601Format.parse(rs.getString(4)));
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
                gc.setEndDate(iso8601Format.parse(rs.getString(2)));
            } else {
                gc.setEndDate(null);
            }
            gc.setInitialAmount(rs.getDouble(3));
            if (rs.getString(4) != null) {
                gc.setStartDate(iso8601Format.parse(rs.getString(4)));
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
            contentValues.put("endDate",  iso8601Format.format(goal.getEndDate()));
        }
        contentValues.put("initialAmount", goal.getInitialAmount());
        if (goal.getStartDate() != null) {
            contentValues.put("startDate", iso8601Format.format(goal.getStartDate()));
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


}
