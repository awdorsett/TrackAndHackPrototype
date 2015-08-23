package trackandhack.trackandhackprototype_2.Module;

/**
 * Created by andrewdorsett on 8/22/15.
 */
public class DBHelperModule {

    public static String INSERT_INT_GOAL = "INSERT OR IGNORE INTO Goals(currentAmount, endDate, initialAmount, startDate status, title) VALUES ( ?, ?, ?, ?, ?, ?);";
    public static String CREATE_GOALS_QUERY =  "CREATE TABLE IF NOT EXISTS Goals(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "currentAmount DOUBLE," +
            "endDate TEXT," +
            "initialAmount DOUBLE," +
            "startDate TEXT," +
            "status TEXT," +
            "title TEXT);";
    public static String CREATE_GC_QUERY = "CREATE TABLE IF NOT EXISTS GiftCards(" +
            "id INTEGER PRIMARY KEY," +
            "digits TEXT," +
            "fee DOUBLE);";
    public static String GET_GC_QUERY = "SELECT Goals.id AS id, currentAmount, endDate, initialAmount, startDate, status, title, digits, fee  FROM Goals INNER JOIN GiftCards ON Goals.id = GiftCards.id;";
}
