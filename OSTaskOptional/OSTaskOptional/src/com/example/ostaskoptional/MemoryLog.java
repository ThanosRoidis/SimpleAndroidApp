package com.example.ostaskoptional;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Η κλάση μέσω της οποίας γίνεται η διαχείριση της βασης δεδομένων για τισ
 * καταστάσεις της εξωτερικής και της εσωτερικής μνήμης
 * 
 * @author Θανάσης
 * 
 */
public class MemoryLog {

	public static final String KEY_ROWID = "_id";

	public static final String KEY_FREE_INTERNAL_STORAGE = "free_internal_memory";
	public static final String KEY_INTERNAL_STORAGE_SIZE = "internal_memory_size";

	public static final String KEY_FREE_EXTERNAL_STORAGE = "free_external_memory";
	public static final String KEY_EXTERNAL_STORAGE_SIZE = "external_memory_size";

	public static final String KEY_DATE = "entry_date";

	private static final String DATABASE_NAME = "MemoryLogDatabase";
	private static final String DATABASE_TABLE = "MemoryLogTable";
	private static final int DATABASE_VERSION = 1;

	private DatabaseHelper myHelper;
	private final Context context;
	private SQLiteDatabase database;

	public MemoryLog(Context context) {
		this.context = context;
	}

	/**
	 * Ανοίγει την βάσει δεδομένων. Πρέπει να καλείται πριν απο κάθε φορά που
	 * θέλουμε να πάρουμε ή να αλλάξουμε τα δεδομένα απο την βάση
	 * 
	 * @return
	 * @throws SQLException
	 */
	public MemoryLog open() throws SQLException {
		myHelper = new DatabaseHelper(context);
		database = myHelper.getWritableDatabase();

		return this;
	}

	/**
	 * Κλείνει την βάση δεδομένων που ήταν ανοιχτή. Πρέπει να καλείται κάθε φορά
	 * που την ανοίγουμε
	 */
	public void close() {
		myHelper.close();
	}

	/**
	 * Προσθέτει μια καταχώρηση στην βάση δεδομένων. Αν υπάρχει μια καταχώρηση
	 * με την ίδια ημερομηνία, τότε διαγράφεται και στην θέση της μπαίνει η
	 * καινούργια
	 * 
	 * @param entry Η καταχώρηση που προστίθεται
	 * @see MemoryLogEntry
	 */
	public void addEntry(MemoryLogEntry entry) {
		// TODO Auto-generated method stub

		String lastDate = getLastEntryDate();

		String freeInternalStorage = entry.getFreeInternalStorage();
		String internalStorageSize = entry.getInternalStorageSize();
		String freeExternalStorage = entry.getFreeExternalStorage();
		String externalStorageSize = entry.getExternalStorageSize();
		String date = entry.getDate();

		ContentValues cv = new ContentValues();
		cv.put(KEY_FREE_INTERNAL_STORAGE, freeInternalStorage);
		cv.put(KEY_INTERNAL_STORAGE_SIZE, internalStorageSize);
		cv.put(KEY_FREE_EXTERNAL_STORAGE, freeExternalStorage);
		cv.put(KEY_EXTERNAL_STORAGE_SIZE, externalStorageSize);
		cv.put(KEY_DATE, date);

		if (date.equals(lastDate)) {
			int i = database.update(DATABASE_TABLE, cv, KEY_DATE + "='" + date
					+ "'", null);

		} else {	
			database.insert(DATABASE_TABLE, null, cv);
		}
	}

	
	/**
	 * Επιστρέφει όλες τις καταχωρήσεις που υπάρχουν στην βάση δεδομένων
	 * @return Το ArrayList με τις καταχωρήσεις
	 * @throws SQLException
	 */
	public ArrayList<MemoryLogEntry> getData() throws SQLException {
		String[] columns = new String[] { KEY_ROWID, KEY_FREE_INTERNAL_STORAGE,
				KEY_INTERNAL_STORAGE_SIZE, KEY_FREE_EXTERNAL_STORAGE,
				KEY_EXTERNAL_STORAGE_SIZE, KEY_DATE };
		Cursor c = database.query(DATABASE_TABLE, columns, null, null, null,
				null, KEY_ROWID + " DESC");
		ArrayList<MemoryLogEntry> data = new ArrayList<MemoryLogEntry>();

		int iRow = c.getColumnIndex(KEY_ROWID);
		int iFreeIntStor = c.getColumnIndex(KEY_FREE_INTERNAL_STORAGE);
		int iIntStorSize = c.getColumnIndex(KEY_INTERNAL_STORAGE_SIZE);
		int iFreeExtStor = c.getColumnIndex(KEY_FREE_EXTERNAL_STORAGE);
		int iExtStorSize = c.getColumnIndex(KEY_EXTERNAL_STORAGE_SIZE);
		int iDate = c.getColumnIndex(KEY_DATE);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			MemoryLogEntry entry = new MemoryLogEntry(
					c.getString(iFreeIntStor), c.getString(iIntStorSize),
					c.getString(iFreeExtStor), c.getString(iExtStorSize),
					c.getString(iDate));
			data.add(entry);

		}
		return data;

	}

	
	/**
	 * Επιστρέφει την τελευταία χρονολογικά καταχώρηση που έγινε στην βάση
	 * @return Η τελευταία καταχώρηση
	 * @throws SQLException
	 */
	private String getLastEntryDate() throws SQLException {
		String[] columns = new String[] { KEY_DATE };
		Cursor c = /*
					 * = database.query(DATABASE_TABLE, columns,
					 * "? = (SELECT max(?) FROM " + DATABASE_TABLE +")" , new
					 * String[]{KEY_ROWID,KEY_ROWID}, null, null, null);
					 */
		database.rawQuery("SELECT " + KEY_DATE + " FROM " + DATABASE_TABLE
				+ " WHERE " + KEY_ROWID + " =(SELECT MAX(" + KEY_ROWID
				+ ") FROM " + DATABASE_TABLE + ")", null);
		if (c.moveToFirst()) {
			String date = c.getString(c.getColumnIndex(KEY_DATE));
			return date;
		}
		return null;
	}

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_FREE_INTERNAL_STORAGE + " TEXT NOT NULL, "
					+ KEY_INTERNAL_STORAGE_SIZE + " TEXT NOT NULL, "
					+ KEY_FREE_EXTERNAL_STORAGE + " TEXT NOT NULL, "
					+ KEY_EXTERNAL_STORAGE_SIZE + " TEXT NOT NULL, " + KEY_DATE
					+ " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_NAME);
			onCreate(db);
		}

	}

	public void deleteTable() {
		// TODO Auto-generated method stub
		database.delete(DATABASE_TABLE, null, null);
	}

	public void deleteEntry(MemoryLogEntry entry) {
		// TODO Auto-generated method stub
		database.execSQL("DELETE FROM " + DATABASE_TABLE + " WHERE " + KEY_DATE
				+ "='" + entry.getDate() + "'");
	}
}
