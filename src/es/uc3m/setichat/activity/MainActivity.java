package es.uc3m.setichat.activity;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.uc3m.setichat.R;


import es.uc3m.setichat.activity.comunicacion.MainToContacts;
import es.uc3m.setichat.activity.comunicacion.SignupToMain;
import es.uc3m.setichat.bd.Contacto;
import es.uc3m.setichat.bd.ContactosSQLiteHelper;
import es.uc3m.setichat.bd.UsuariosSQLiteHelper;
import es.uc3m.setichat.mensajes.MensajeContactRequest;
import es.uc3m.setichat.mensajes.MensajeContactResponse;
import es.uc3m.setichat.parser.GeneradorXML;
import es.uc3m.setichat.parser.TextExtractor;
import es.uc3m.setichat.service.SeTIChatService;
import es.uc3m.setichat.service.SeTIChatServiceBinder;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

/**
 * This is the main activity and its used to initialize all the SeTIChat features. 
 * It configures the three tabs used in this preliminary version of SeTIChat.
 * It also start the service that connects to the SeTIChat server.
 * 
 * @author Guillermo Suarez de Tangil <guillermo.suarez.tangil@uc3m.es>
 * @author Jorge Blasco Alis <jbalis@inf.uc3m.es>
 */

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	
	// Service used to access the SeTIChat server
	private SeTIChatService mService;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	// Receivers that wait for notifications from the SeTIChat server
	private BroadcastReceiver openReceiver;
	private BroadcastReceiver chatMessageReceiver;
	private BroadcastReceiver loginOkReceiver;
	private BroadcastReceiver contactResponseReceiver;
	
	// Variable para almacenar el estado de la conexion
	boolean conectado = false;
	boolean conexionEstablecida = false;
	
	// Variable para guardar el identificador recibido desde el servidor.
	private String serverId;
	
	// Contactos de la agenda del telefono
	private List<Contacto> agendaTelefono = null;
	
	// Lista de contactos validados por el servidor.
	private List<Contacto> contactos = null;
	
	// Variables asociadas a las bases de datos
	ContactosSQLiteHelper contactosHelper = null;
	SQLiteDatabase dbContactos = null;
	
	// Clase de apoyo para generar los XML
	GeneradorXML generadorXML;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		generadorXML = new GeneradorXML();
		
		setContentView(R.layout.activity_main);

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
		Log.i("Activty", "onCreate");
		
		try{
	        
	        // Make sure the service is started.  It will continue running
	        // until someone calls stopService().  The Intent we use to find
	        // the service explicitly specifies our service component, because
	        // we want it running in our own process and don't want other
	        // applications to replace it.
	        startService(new Intent(MainActivity.this,
	                SeTIChatService.class));
	        
        }catch(Exception e){

    		Log.d("MainActivity", "Unknown Error", e);

	        stopService(new Intent(MainActivity.this,
	                SeTIChatService.class));
        }
		
		
		// Create and register broadcast receivers
		IntentFilter openFilter = new IntentFilter();
		openFilter.addAction("es.uc3m.SeTIChat.CHAT_OPEN");

		 openReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	Context context1 = getApplicationContext();
				CharSequence text = "SeTIChatConnected";
				int duration = Toast.LENGTH_SHORT;
				conexionEstablecida = true;
				Toast toast = Toast.makeText(context1, text, duration);
				toast.show();
		    }
		  };

		  registerReceiver(openReceiver, openFilter);
		  
		  chatMessageReceiver = new BroadcastReceiver() {
			    @Override
			    public void onReceive(Context context, Intent intent) {
			      //do something based on the intent's action
			    	Context context1 = getApplicationContext();
					CharSequence text = "SeTIChat Message Received";
					int duration = Toast.LENGTH_SHORT;
					Bundle bundle = intent.getExtras();
					String texto = bundle.getString("message"); 
					Log.d("onMessage", "Recibido: " + texto);
					Toast toast = Toast.makeText(context1, text, duration);
					toast.show();
					
					//Eliminar
					//TextExtractor textExtractor = new TextExtractor();
					//textExtractor.parsear(text.toString());
			    }
			  };
			  
			  
		IntentFilter chatMessageFilter = new IntentFilter(); 
		chatMessageFilter.addAction("es.uc3m.SeTIChat.CHAT_MESSAGE");
		registerReceiver(chatMessageReceiver, chatMessageFilter);
		
		loginOkReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	Log.d("onMessage", "Se ejecuta rutina de loginOkReceiver");
		      //do something based on the intent's action
				//Bundle bundle = intent.getExtras();
				//SignupToMain datos = (SignupToMain)bundle.getSerializable("datos");
		    	SignupToMain datos = (SignupToMain) intent.getSerializableExtra("datos");
				Log.d("onMessage", "Datos recibidos en Main desde SignUp: " + datos.getServerId());
				serverId = datos.getServerId();
				
				Toast toast = Toast.makeText(getApplicationContext(), "Recuperando contactos...", Toast.LENGTH_SHORT);
				toast.show();
				MensajeContactRequest mensaje = new MensajeContactRequest();
				mensaje.setContactos(obtenerContactosTelefono());
				mensaje.setEncrypted("false");
				mensaje.setIdSource(serverId);
				mensaje.setSigned("false");
				mensaje.setType("2");
				
				while(conexionEstablecida == false){
					// Cambiar este bucle y la siguiente instruccion -> Cargar agenda del telefono
					// al comienzo de la aplicación y que sea 
				}
				Log.d("onMessage", "mandando contactos al servidor");
				//mService.sendMessage(generadorXML.generarXMLContactRequest(mensaje));
		    }
		  };
		  
		IntentFilter loginOkFilter = new IntentFilter();
		loginOkFilter.addAction("es.uc3m.SeTIChat.LOGIN_OK");
		registerReceiver(loginOkReceiver, loginOkFilter);
		
		contactResponseReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		      //do something based on the intent's action
		    	MensajeContactResponse mensaje = (MensajeContactResponse) intent.getSerializableExtra("mensajeContactResponse");
		    	contactos = mensaje.getListaContactos();
				MainToContacts datos2 = new MainToContacts();
				datos2.setServerId(serverId);
				datos2.setContactos(contactos);
				Log.d("maintocontacts", "se envia a contacts serverId: " + datos2.getServerId());
				ContactsFragment listaContactos = (ContactsFragment) getFragmentManager().findFragmentById(R.id.container);
				listaContactos.actualizarDatos(datos2);
		    }
		  };
		  
		  
		IntentFilter contactResponseFilter = new IntentFilter(); 
		contactResponseFilter.addAction("es.uc3m.SeTIChat.CONTACT_RESPONSE");
		registerReceiver(contactResponseReceiver, contactResponseFilter);

		
		
	}
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub 
		super.onStart();
		
		if(conectado == false){
			mService = new SeTIChatService();
			//mService.onCreate();
			Intent intent = new Intent(this, SignUp.class);
	        //intent.setClass("es.uc3m.setichat", SeTIChatConversationActivity.class);
	        //intent.putExtra("index", position);           
	        startActivity(intent);
			conectado = true;
		}
	}
	
	
	@Override
	  public void onDestroy() {
	    super.onDestroy();
        // We stop the service if activity is destroyed
	    stopService(new Intent(MainActivity.this,
                SeTIChatService.class));
	    // We also unregister the receivers to avoid leaks.
        unregisterReceiver(chatMessageReceiver);
        unregisterReceiver(openReceiver);
	 }
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		Log.v("MainActivity", "onResume: Resuming activity...");
		super.onResume();
	}



	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, show the tab contents in the
		// container view.
		MainToContacts datos = new MainToContacts();
		datos.setServerId(serverId);
		datos.setContactos(contactos);
		Log.d("maintocontacts", "se envia a contacts serverId: " + datos.getServerId());
		if(contactos != null){
			ContactsFragment fragment = new ContactsFragment(this, datos);
			getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	public void update(){
		
	}
	
	  public void showException(Throwable t) {
		    AlertDialog.Builder builder=new AlertDialog.Builder(this);

		    builder
		      .setTitle("Exception!")
		      .setMessage(t.toString())
		      .setPositiveButton("OK", null)
		      .show();
	  }
	  
	  /** Defines callbacks for service binding, passed to bindService() */
	    private ServiceConnection mConnection = new ServiceConnection() {

	        @Override
	        public void onServiceConnected(ComponentName className,
	                IBinder service) {
	            // We've bound to LocalService, cast the IBinder and get LocalService instance
	        	Log.i("Service Connection", "Estamos en onServiceConnected");
	            SeTIChatServiceBinder binder = (SeTIChatServiceBinder) service;
	            mService = binder.getService();
	            conectado = true;
	        }

	        @Override
	        public void onServiceDisconnected(ComponentName arg0) {
	           conectado = false;
	        }
	    };



	public SeTIChatService getService() {
		// TODO Auto-generated method stub
		return mService;
	}
	
	//SeTIChatServiceDelegate Methods
	
	public void showNotification(String message){
		Context context = getApplicationContext();
		CharSequence text = message;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	

    public List<Contacto> obtenerContactosTelefono(){
    	
    	List<Contacto> resultado = null;
    	ContentResolver cr = this.getContentResolver();
    	Map<String,String> contactos = new HashMap<String,String>();
    	
    	Log.d("contactos", "metodo obtenerContactos");
    	
    	Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
            // read id
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                // read names 
                String displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                // Phone Numbers 
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
                    while (pCur.moveToNext()) {
                        String number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String typeStr = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        contactos.put(displayName, number);
                }
                pCur.close();
            }
        }
        
        Log.d("contactos", "terminada lectura de contactos");
        
        if(contactos.size() > 0){
        	resultado = new ArrayList<Contacto>();
        	Set<String> nombres = contactos.keySet();
        	Iterator<String> it = nombres.iterator();
        	while(it.hasNext()){
        		String entrada = it.next();
        		resultado.add(new Contacto(entrada, contactos.get(entrada)));
        	}
        }
        else{
        	Log.d("contactos", "no se obtuvo ningun contacto");
        }
        
        Log.d("contactos", "fin metodo obtenerContactos");
    	
    	return resultado;
    }
}
