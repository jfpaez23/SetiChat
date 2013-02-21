package es.uc3m.setichat.mensajes;

import java.io.Serializable;

public class MensajeVacio implements Serializable{

	private static final long serialVersionUID = 4685723635308490283L;

	
	// Se considera que un mensaje vacio solo contiene los valores de la cabecera
	private String idSource;
	private String idDestination;
	private String idMessage;
	private String type;
	private String encrypted;
	private String signed;
	
	public MensajeVacio(){
		
	}

	// Getters y setters
	public String getIdSource() {
		return idSource;
	}

	public void setIdSource(String idSource) {
		this.idSource = idSource;
	}

	public String getIdDestination() {
		return idDestination;
	}

	public void setIdDestination(String idDestination) {
		this.idDestination = idDestination;
	}

	public String getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(String idMessage) {
		this.idMessage = idMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}

	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}
	
	
}
