package es.uc3m.setichat.activity.comunicacion;

import java.io.Serializable;

public class ContactsToChat implements Serializable {

	private static final long serialVersionUID = -7591848865679472873L;
	
	String serverId;
	String destinatario;
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	
	
}
