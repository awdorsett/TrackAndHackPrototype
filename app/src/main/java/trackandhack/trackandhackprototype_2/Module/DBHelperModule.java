package trackandhack.trackandhackprototype_2.Module;

/**
 * Created by andrewdorsett on 8/22/15.
 */
public class DBHelperModule {
    public static String GC_TABLE = "GiftCards";
    public static String GOAL_TABLE = "Goals";
    public static String MIN_TABLE = "MinSpends";
    public static String HISTORY_TABLE = "History";

    // INSERT GOAL
    public static String INSERT_INT_GOAL = "INSERT OR IGNORE INTO Goals(currentAmount, endDate, initialAmount, startDate status, title) VALUES ( ?, ?, ?, ?, ?, ?);";

    // CREATE TABLES
    public static String CREATE_GOALS_QUERY =  "CREATE TABLE IF NOT EXISTS Goals(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "currentAmount DOUBLE," +
            "endDate TEXT," +
            "initialAmount DOUBLE," +
            "startDate TEXT," +
            "status TEXT," +
            "title TEXT," +
            "notes VARCHAR(255));";
    public static String CREATE_GC_QUERY = "CREATE TABLE IF NOT EXISTS GiftCards(" +
            "id INTEGER PRIMARY KEY," +
            "digits TEXT," +
            "fee DOUBLE);";
    public static String CREATE_HISTORY_QUERY = "CREATE TABLE IF NOT EXISTS History(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "goal_id INTEGER," +
            "date TEXT," +
            "amount DOUBLE," +
            "notes VARCHAR(255));";
    public static String CREATE_MS_QUERY = "CREATE TABLE IF NOT EXISTS MinSpends(" +
            "id INTEGER PRIMARY KEY," +
            "bonus DOUBLE);";

    // GET GIFT CARDS
    public static String GET_GC_QUERY = "SELECT Goals.id AS id, currentAmount, endDate, initialAmount, startDate, status, title, notes, digits, fee  FROM Goals INNER JOIN GiftCards ON Goals.id = GiftCards.id;";
    public static String GET_GC_QUERY_OPEN = "SELECT Goals.id AS id, currentAmount, endDate, initialAmount, startDate, status, title, notes, digits, fee  FROM Goals INNER JOIN GiftCards ON Goals.id = GiftCards.id WHERE status != ?;";
    public static String GET_GC_BY_ID_QUERY = "SELECT Goals.id AS id, currentAmount, endDate, " +
            "initialAmount, startDate, status, title, notes, digits, fee " +
            "FROM Goals INNER JOIN GiftCards ON Goals.id = GiftCards.id WHERE Goals.id = ?;";

    // GET MIN SPEND
    public static String GET_MS_BY_ID_QUERY = "SELECT Goals.id AS id, currentAmount, endDate, " +
            "initialAmount, startDate, status, title, notes, bonus FROM Goals " +
            "INNER JOIN MinSpends ON Goals.id = MinSpends.id WHERE Goals.id = ?;";
    public static String GET_MS_QUERY = "SELECT Goals.id AS id, currentAmount, endDate, initialAmount, startDate, status, title, notes, bonus  FROM Goals INNER JOIN MinSpends ON Goals.id = MinSpends.id;";
    public static String GET_MS_QUERY_OPEN = "SELECT Goals.id AS id, currentAmount, endDate, initialAmount, startDate, status, title, notes, bonus  FROM Goals INNER JOIN MinSpends ON Goals.id = MinSpends.id WHERE status != ?;";

    // GET HISTORY
    public static String GET_HS_FOR_ID = "SELECT History.id AS id, History.goal_id AS goal_id, History.date AS date, History.amount AS amount, History.notes AS notes FROM History INNER JOIN Goals ON Goals.id = ?;";

    // SET HISTORY
    public static String INSERT_HS = "INSERT INTO History (goal_id, date, amount, notes) VALUES";
    public static String INSERT_HS_VALUE_SET = " (?, ?, ?, ?)";
}
