package es.uc3m.setichat.bd;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.sax.StartElementListener;
import android.util.Log;

public class ContactosSQLiteHelper extends SQLiteOpenHelper{
	
	String sqlCreate = "CREATE TABLE Contactos (nick TEXT, mobile TEXT)";

	public ContactosSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS Contactos");
		db.execSQL(sqlCreate);
	}
	
	public void insert(SQLiteDatabase db, String nick, String mobile){
		Log.d("db", "insertando en Contactos: " + nick + ", " + mobile);
		db.execSQL("INSERT INTO Contactos (nick,mobile) VALUES ('" + nick + "','" + mobile + "')");
	}
	
	public List<Contacto> getContactos(SQLiteDatabase db){
		
		List<Contacto> resultado = new ArrayList<Contacto>();
		
		Cursor result = db.query("Contactos", new String[]{"nick,mobile"}, null, null, null, null, null);
		Log.d("db", "recuperadas " + result.getCount() + " entradas de la tabla Contactos");
		while(result.moveToNext()){
			resultado.add(new Contacto(result.getString(0), result.getString(1)));
		}
		
		return resultado;
	}
}
