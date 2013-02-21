package es.uc3m.setichat.parser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.util.Log;


import es.uc3m.setichat.mensajes.MensajeChat;
import es.uc3m.setichat.mensajes.MensajeContactResponse;
import es.uc3m.setichat.mensajes.MensajeResponse;
import es.uc3m.setichat.mensajes.MensajeSignUp;

public class TextExtractor implements ContentHandler {

	// Etiquetas a tener en cuenta a la hora de parsear el documento:
	boolean nombreEtiqueta = false;
	boolean otraEtiqueta = false;
	
	// Posibles tipos de mensajes a parsear:
	MensajeSignUp mensajeSignUp = null;
	MensajeContactResponse mensajeContactResponse = null;
	MensajeResponse mensajeResponse = null;
	MensajeChat mensajeChat = null;

	// Etiquetas de la cabecera de los mensajes
	boolean idSource = false;
	boolean idDestination = false;
	boolean idMessage = false;
	boolean type = false;
	boolean encrypted = false;
	boolean signed = false;
	
	// Valores recogidos de las etiquetas de la cabecera:
	String idSourceValor = null;
	String idDestinationValor = null;
	String idMessageValor = null;
	public String typeValor = null;
	String encryptedValor = null;
	String signedValor = null;
	
	// Etiquetas propias de un mensaje de tipo SignUp
	boolean nick = false;
	boolean mobile = false;
	
	// Valores de las etiquetas de un mensaje de tipo SignUp
	public String nickValor = null;
	public String mobileValor = null;
	
	// Etiquetas propias de un mensaje de tipo ContactResponse
	boolean listaContactos = false;
	
	// Etiquetas propias de un mensaje tipo Response
	boolean responseCode = false;
	boolean responseMessage = false;
	String responseCodeValor = null;
	String responseMessageValor = null;
	
	// Etiquetas propias de un mensaje tipo Chat
	boolean mensaje = false;
	String mensajeValor = null;
	
	
	// Valores de las etiquetas:
	String nombreEtiquetaValor = null;
	String otraEtiquetaValor = null;

	public TextExtractor(){
		
	}
	
	public TextExtractor(Writer out) {
		
	}
	

	public void startElement(String namespaceURI, String localName,
			String qualifiedName, Attributes atts) {

//		if (qualifiedName.equals("nombreEtiqueta")) {
//			this.nombreEtiqueta = true;
//		}
//
//		if (qualifiedName.equals("otraEtiqueta")) {
//			this.otraEtiqueta = true;
//		}
		
		 
		if(qualifiedName.equals("idSource")){
			this.idSource = true;
		}
		
		if(qualifiedName.equals("idDestination")){
			this.idDestination = true;
		}
		
		if(qualifiedName.equals("idMessage")){
			this.idMessage = true;
		}
		
		if(qualifiedName.equals("type")){
			this.type = true;
		}
		
		if(qualifiedName.equals("encrypted")){
			this.encrypted = true;
		}
		
		if(qualifiedName.equals("signed")){
			this.signed = true;
		}
		
		if(qualifiedName.equals("nick")){
			this.nick = true;
		}
		
		if(qualifiedName.equals("mobile")){
			this.mobile = true;
		}
		
		if(qualifiedName.equals("responseCode")){
			this.responseCode = true;
		}
		
		if(qualifiedName.equals("responseMessage")){
			this.responseMessage = true;
		}
		
		if(qualifiedName.equals("chatMessage")){
			this.mensaje = true;
		}
		
	}

	public void characters(char[] text, int start, int length)
			throws SAXException {

//		if (this.nombreEtiqueta) {
//			this.nombreEtiquetaValor = new String(text, start, length);
//			this.nombreEtiqueta = false;
//		}
//
//		if (this.otraEtiqueta) {
//			this.otraEtiquetaValor = new String(text, start, length);
//			this.otraEtiqueta = false;
//		}
		
		if(this.encrypted){
			this.encryptedValor = new String(text, start, length);
			this.encrypted = false;
		}
		
		if(this.idDestination){
			this.idDestinationValor = new String(text, start, length);
			this.idDestination = false;
		}
		
		if(this.idMessage){
			this.idMessageValor = new String(text, start, length);
			this.idMessage = false;
		}
		
		if(this.idSource){
			this.idSourceValor = new String(text, start, length);
			this.idSource = false;
		}
		
		if(this.signed){
			this.signedValor = new String(text, start, length);
			this.signed = false;
		}
		
		if(this.type){
			this.typeValor = new String(text, start, length);
			this.type = false;
		}
		
		if(this.nick){
			this.nickValor = new String(text, start, length);
			this.nick = false;
		}
		
		if(this.mobile){
			this.mobileValor = new String(text, start, length);
			this.mobile = false;
		}
		
		if(this.responseCode){
			this.responseCodeValor = new String(text, start, length);
			this.responseCode = false;
		}
		
		if(this.responseMessage){
			this.responseMessageValor = new String(text,start,length);
			this.responseMessage = false;
		}
		
		if(this.mensaje){
			this.mensajeValor = new String(text, start, length);
			this.mensaje = false;
		}

		// try {
		// String nuevoValor = new String(text, start, length);
		// nuevoValor = nuevoValor.trim();
		// if(nuevoValor != null && !nuevoValor.equals("")){
		// valores.add(nuevoValor);
		// }
		//
		// }
		// catch (Exception e) {
		// throw new SAXException(e);
		// }

	}

	// Metodos vacios.
	public void setDocumentLocator(Locator locator) {
	}

	public void startDocument() {
	}

	public void endDocument() {
	}

	public void startPrefixMapping(String prefix, String uri) {
	}

	public void endPrefixMapping(String prefix) {
	}

	public void endElement(String namespaceURI, String localName,
			String qualifiedName) {
		
		//Log.d("parser", "metodo endElement. localName = " + localName);
		
		// Al final de parsear la cabecera se crea la clase apropiada para
		// el tipo de mensaje y se guardan los valores de la cabecera.
		if(localName.equals("header")){
			
			Log.d("parser", "finalizando etiqueta header");
			
			if(this.typeValor != null){
				
				//Caso de un mensaje de tipo SignUp
				if(this.typeValor.equals("1")){
					this.mensajeSignUp =  new MensajeSignUp();
					this.mensajeSignUp.setEncrypted(encryptedValor);
					this.mensajeSignUp.setIdDestination(idDestinationValor);
					this.mensajeSignUp.setIdMessage(idMessageValor);
					this.mensajeSignUp.setIdSource(idSourceValor);
					this.mensajeSignUp.setSigned(signedValor);
					this.mensajeSignUp.setType(typeValor);
				}
				else
				// Caso de un mensaje de tipo ContactResponse
				if(this.typeValor.equals("3")){
					this.mensajeContactResponse = new MensajeContactResponse();
					this.mensajeContactResponse.setEncrypted(encryptedValor);
					this.mensajeContactResponse.setIdDestination(idDestinationValor);
					this.mensajeContactResponse.setIdMessage(idMessageValor);
					this.mensajeContactResponse.setIdSource(idSourceValor);
					this.mensajeContactResponse.setSigned(signedValor);
					this.mensajeContactResponse.setType(typeValor);
				}
				else
				// Caso de un mensaje de tipo Response
				if(this.typeValor.equals("6")){
					this.mensajeResponse = new MensajeResponse(); 
					this.mensajeResponse.setEncrypted(encryptedValor);
					this.mensajeResponse.setIdDestination(idDestinationValor);
					this.mensajeResponse.setIdMessage(idMessageValor);
					this.mensajeResponse.setIdSource(idSourceValor);
					this.mensajeResponse.setSigned(signedValor);
					this.mensajeResponse.setType(typeValor);
				}
				else
				// Caso de un mensaje de tipo Chat
				if(this.typeValor.equals("4")){
					this.mensajeChat = new MensajeChat();
					this.mensajeChat.setEncrypted(encryptedValor);
					this.mensajeChat.setIdDestination(idDestinationValor);
					this.mensajeChat.setIdMessage(idMessageValor);
					this.mensajeChat.setIdSource(idSourceValor);
					this.mensajeChat.setSigned(signedValor);
					this.mensajeChat.setType(typeValor);
				}
				
			}
		}
		
		// Acciones a llevar a cabo al terminar de leer la etiqueta "signup"
		if(localName.equals("signup")){
			if(this.mensajeSignUp != null){
				this.mensajeSignUp.setNick(nickValor);
				this.mensajeSignUp.setMobile(mobileValor);
			}
		}
		else
		// Acciones a llevar a cabo al terminar de leer la etiqueta "contact"
		if(localName.equals("contact")){
			if(this.mensajeContactResponse != null){
				this.mensajeContactResponse.nuevoContacto(this.nickValor, this.mobileValor);
			}
		}
		else
		// Acciones a llevar a cabo al terminar de leer la etiqueta "response"
		if(localName.equals("response")){
			if(this.mensajeResponse != null){
				this.mensajeResponse.setResponseCode(responseCodeValor);
				this.mensajeResponse.setResponseMessage(responseMessageValor);
			}
		}
		else
		// Acciones a llevar a cabo al terminar de leer la etiqueta "chatMessage"
		if(localName.equals("chatMessage")){
			if(this.mensajeChat != null){
				this.mensajeChat.setMensaje(mensajeValor);
			}
		}
	}

	public void ignorableWhitespace(char[] text, int start, int length)
			throws SAXException {
	}

	public void processingInstruction(String target, String data) {
	}

	public void skippedEntity(String name) {
	}
	
	// Metodo para parsear un xml expresado como una cadena de caracteres
	public void parsear(String xml){
		try{
			System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
			XMLReader parser = XMLReaderFactory.createXMLReader();
			parser.setContentHandler(this);
			parser.parse( new InputSource( new StringReader( xml )));
		} catch (Exception e){
			Log.d("parser", "error");
			Log.d("parser", e.getMessage());
		}
	}

	public MensajeSignUp getMensajeSignUp() {
		return mensajeSignUp;
	}

	public void setMensajeSignUp(MensajeSignUp mensajeSignUp) {
		this.mensajeSignUp = mensajeSignUp;
	}

	public MensajeContactResponse getMensajeContactResponse() {
		return mensajeContactResponse;
	}

	public void setMensajeContactResponse(
			MensajeContactResponse mensajeContactResponse) {
		this.mensajeContactResponse = mensajeContactResponse;
	}

	public MensajeResponse getMensajeResponse() {
		return mensajeResponse;
	}

	public void setMensajeResponse(MensajeResponse mensajeResponse) {
		this.mensajeResponse = mensajeResponse;
	}

	public MensajeChat getMensajeChat() {
		return mensajeChat;
	}

	public void setMensajeChat(MensajeChat mensajeChat) {
		this.mensajeChat = mensajeChat;
	}
	
	

}
