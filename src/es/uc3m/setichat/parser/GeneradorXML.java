package es.uc3m.setichat.parser;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Iterator;

import android.util.Log;
import es.uc3m.setichat.bd.Contacto;
import es.uc3m.setichat.mensajes.MensajeChat;
import es.uc3m.setichat.mensajes.MensajeContactRequest;
import es.uc3m.setichat.mensajes.MensajeSignUp;

public class GeneradorXML {
	
	private final String cabeceraXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private final String idServidor = "setichat@appspot.com";
	private SecureRandom random = new SecureRandom();

	private String generarAleatorio(){
//		String resultado = Integer.toString((int)(Math.random()*256));
//		resultado += Integer.toString((int)(Math.random()*256));
//		resultado += Integer.toString((int)(Math.random()*256));
//		resultado += Integer.toString((int)(Math.random()*256));
//		return resultado;
		return new BigInteger(128, random).toString(32);
	}
	
	public String generarXMLSignUp(MensajeSignUp mensaje){
		
		String xmlCodificado = null;
		String xml = new String(cabeceraXML);
		xml += "<message>";
		xml += "<header>";
			xml += "<idSource>";
			xml += mensaje.getIdSource();
			xml += "</idSource>";
			xml += "<idDestination>";
			xml += idServidor;
			xml += "</idDestination>";
			xml += "<idMessage>";
			xml += generarAleatorio();
			xml += "</idMessage>";
			xml += "<type>1</type>";
			xml += "<encrypted>";
			xml += mensaje.getEncrypted();
			xml += "</encrypted>";
			xml += "<signed>";
			xml += mensaje.getSigned();
			xml += "</signed>";
		xml += "</header>";
		xml += "<content>";
			xml += "<signup>";
			xml += "<nick>";
			xml += mensaje.getNick();
			xml += "</nick>";
			xml += "<mobile>";
			xml += mensaje.getMobile();
			xml += "</mobile>";
			xml += "</signup>";
		xml += "</content>";
		xml += "</message>";
		
//		try{
//			xmlCodificado = new String(xml.getBytes("UTF-8"), "UTF-8");
//		} catch (Exception e){
//			
//		}
		
		Log.d("generadorXML", xml);
		
		return xml;
	}
	
	public String generarXMLChat(MensajeChat mensaje){
		
		String xml = cabeceraXML;
		xml += "<message>";
		xml += "<header>";
			xml += "<idSource>";
			xml += mensaje.getIdSource();
			xml += "</idSource>";
			xml += "<idDestination>";
			xml += mensaje.getIdDestination();
			xml += "</idDestination>";
			xml += "<idMessage>";
			xml += generarAleatorio();
			xml += "</idMessage>";
			xml += "<type>4</type>";
			xml += "<encrypted>";
			xml += mensaje.getEncrypted();
			xml += "</encrypted>";
			xml += "<signed>";
			xml += mensaje.getSigned();
			xml += "</signed>";
		xml += "</header>";
		xml += "<content>";
			xml += "<chatMessage>";
			xml += mensaje.getMensaje();
			xml += "</chatMessage>";
		xml += "</content>";
		xml += "</message>";
		
		Log.d("generadorXML", "XMLchat: " + xml);
		
		return xml;
	}
	
	
public String generarXMLContactRequest(MensajeContactRequest mensaje){
		
		String xml = cabeceraXML;
		xml += "<message>";
		xml += "<header>";
			xml += "<idSource>";
			xml += mensaje.getIdSource();
			xml += "</idSource>";
			xml += "<idDestination>";
			xml += idServidor;
			xml += "</idDestination>";
			xml += "<idMessage>";
			xml += generarAleatorio();
			xml += "</idMessage>";
			xml += "<type>2</type>";
			xml += "<encrypted>";
			xml += mensaje.getEncrypted();
			xml += "</encrypted>";
			xml += "<signed>";
			xml += mensaje.getSigned();
			xml += "</signed>";
		xml += "</header>";
		xml += "<content>";
			xml += "<mobileList>";
			Iterator<Contacto> it = mensaje.getContactos().iterator();
			while(it.hasNext()){
				xml += "<mobile>"+it.next().getMobile()+"</mobile>";
			}
			xml += "</mobileList>";
		xml += "</content>";
		xml += "</message>";
		
		Log.d("generadorXML", "XMLcontacts: " + xml);
		
		return xml;
	}
}
