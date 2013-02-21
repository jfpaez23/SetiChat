package es.uc3m.setichat.bd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.sax.StartElementListener;
import android.util.Log;

public class UsuariosSQLiteHelper extends SQLiteOpenHelper{
	
	String sqlCreate = "CREATE TABLE Usuarios (nick TEXT, mobile TEXT, serverId TEXT)";

	public UsuariosSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS Usuarios");
		db.execSQL(sqlCreate);
	}
	
	public void insert(SQLiteDatabase db, String nick, String mobile, String serverId){
		Log.d("db", "insertando en Usuarios: " + nick + ", " + mobile + ", " + serverId);
		db.execSQL("INSERT INTO Usuarios (nick,mobile,serverId) VALUES ('" + nick + "','" + mobile + "','" + serverId + "')");
	}
	
	public String getServerId(SQLiteDatabase db){
		String resultado = null;
		
		Cursor result = db.query("Usuarios", new String[]{"nick,mobile,serverId"}, null, null, null, null, null);
		Log.d("db", "recuperadas " + result.getCount() + " entradas de la tabla Usuarios");
		if(result.moveToFirst()){
			resultado = result.getString(2);
		}
		
		return resultado;
	}
}
