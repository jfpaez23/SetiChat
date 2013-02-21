package es.uc3m.setichat.activity;

import es.uc3m.setichat.R;
import es.uc3m.setichat.R.id;
import es.uc3m.setichat.R.layout;
import es.uc3m.setichat.R.menu;
import es.uc3m.setichat.activity.comunicacion.SignupToMain;
import es.uc3m.setichat.bd.UsuariosSQLiteHelper;
import es.uc3m.setichat.mensajes.MensajeResponse;
import es.uc3m.setichat.mensajes.MensajeSignUp;
import es.uc3m.setichat.mensajes.MensajeVacio;
import es.uc3m.setichat.parser.GeneradorXML;
import es.uc3m.setichat.parser.TextExtractor;
import es.uc3m.setichat.service.SeTIChatService;
import es.uc3m.setichat.service.SeTIChatServiceBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends Activity {

	private SeTIChatService mService = null;
	private GeneradorXML generadorXML;
	private BroadcastReceiver messageReceiver;
	UsuariosSQLiteHelper dbUsuarios;
	SQLiteDatabase db;
	private String nick;
	private String mobile;
	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = SeTIChatServiceBinder.getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		messageReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		     
				MensajeResponse mensaje = (MensajeResponse) intent.getSerializableExtra("mensajeResponse");
				if(mensaje.getResponseCode().equals("201")){
					Toast toast = Toast.makeText(getApplicationContext(), "SignUp OK", Toast.LENGTH_SHORT);
					toast.show();
					db = dbUsuarios.getWritableDatabase();
					dbUsuarios.insert(db, nick, mensaje.getIdDestination(), mensaje.getResponseMessage());
					String intentKey = "es.uc3m.SeTIChat.LOGIN_OK";
					Intent openIntent = new Intent(intentKey);
					openIntent.setPackage("es.uc3m.setichat");
					SignupToMain datos = new SignupToMain();
					datos.setServerId(mensaje.getResponseMessage());
					openIntent.putExtra("datos", datos);
					context.sendBroadcast(openIntent);
					finish();
				}
				else{
					Toast toast = Toast.makeText(getApplicationContext(), mensaje.getResponseMessage(), Toast.LENGTH_SHORT);
					toast.show();
				}
		    }
		  };
		  
	IntentFilter chatMessageFilter = new IntentFilter();
	chatMessageFilter.addAction("es.uc3m.SeTIChat.SIGNUP_MESSAGE");
	registerReceiver(messageReceiver, chatMessageFilter);
		
		if (mService == null) {
			// Binding the activity to the service to get shared objects
			bindService(new Intent(SignUp.this,
					SeTIChatService.class), mConnection,
					Context.BIND_AUTO_CREATE);
		}
		
		generadorXML = new GeneradorXML();
		
		setContentView(R.layout.activity_sign_up);
		final Button button = (Button) findViewById(R.id.signup_aceptar);
		final EditText editNick = (EditText)findViewById(R.id.signup_nick);
		final EditText editMovil = (EditText)findViewById(R.id.signup_movil);
		
		button.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	
//	        	Toast.makeText(SignUp.this, editNick.getText().toString() + editMovil.getText().toString(), // R.string.local_service_connected,
//	    				Toast.LENGTH_SHORT).show();
	        	if(mService != null){
	        		nick = editNick.getText().toString();
	        		mobile = editMovil.getText().toString();
	        		mService.onCreate(mobile);
	        		MensajeSignUp mensaje = new MensajeSignUp();
	        		mensaje.setEncrypted("false");
	        		mensaje.setIdDestination("setichat@appspot.com");
	        		mensaje.setIdSource(mobile);
	        		mensaje.setMobile(mobile);
	        		mensaje.setNick(nick);
	        		mensaje.setSigned("false");
	        		mensaje.setType("1");
	        		mService.sendMessage(generadorXML.generarXMLSignUp(mensaje));
	        	}
	        	else{
	        		Toast.makeText(SignUp.this, "No se tiene conexion con el servicio", Toast.LENGTH_SHORT).show();
	        	}
	        }
	    });
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		// Base de datos
		dbUsuarios = new UsuariosSQLiteHelper(this, "Usuarios", null, 1);
		db = dbUsuarios.getReadableDatabase();
		String serverId = dbUsuarios.getServerId(db);
		if(serverId == null){
			Log.d("db", "serverId = null");
		}
		else{
			Log.d("db", "serverId = " + serverId);
			String intentKey = "es.uc3m.SeTIChat.LOGIN_OK";
			Intent openIntent = new Intent(intentKey);
			//openIntent.setPackage("es.uc3m.setichat");
			SignupToMain datos = new SignupToMain();
			datos.setServerId(serverId);
			openIntent.putExtra("datos", datos);
			Context context = getApplicationContext();
			context.sendBroadcast(openIntent);
			Log.d("singup", "mandando datos al main. serverId: " + datos.getServerId());
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sign_up, menu);
		return true;
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

}
