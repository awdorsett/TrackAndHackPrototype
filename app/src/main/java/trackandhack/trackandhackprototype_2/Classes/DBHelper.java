package trackandhack.trackandhackprototype_2.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import trackandhack.trackandhackprototype_2.Module.DBHelperModule;

/**
 * Created by andrewdorsett on 8/22/15.
 */
public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    SimpleDateFormat iso8601Format = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public DBHelper(Context context){
        super(context, "card_db", null, 1);
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
    }

    public List<?> getEntries(GoalType type) {
        ArrayList<Goal> entries = new ArrayList<>();
        if (type.equals(GoalType.GIFT_CARD)) {
            Log.d("DATABASE", "Result set is gift card");
            Cursor resultSet = db.rawQuery(DBHelperModule.GET_GC_QUERY, null);

            while (resultSet.moveToNext()) {
                Log.d("DATABASE", "Has next");
                GiftCard gc = getGiftCardEntry(resultSet);
                if (gc != null) {
                    Log.d("DATABASE", "gc was not null");
                    entries.add(gc);
                }
            }
        }

        return entries;
    }

    public void insertGiftCard(GiftCard gc) {
        ContentValues contentValues = new ContentValues();
        insertGoal(gc);

        contentValues.put("id", gc.getUid());
        contentValues.put("digits", gc.getDigits());
        contentValues.put("fee", gc.getFee());
        db.insert("GiftCards", null, contentValues);
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
}
