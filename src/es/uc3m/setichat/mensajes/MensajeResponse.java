package es.uc3m.setichat.mensajes;

import android.os.Parcel;

public class MensajeResponse extends MensajeVacio {

	String responseCode;
	String responseMessage;
	
	public MensajeResponse() { 
    }
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
}
