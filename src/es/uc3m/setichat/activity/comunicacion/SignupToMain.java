package es.uc3m.setichat.activity.comunicacion;

import java.io.Serializable;

public class SignupToMain implements Serializable {

	private static final long serialVersionUID = 7058261784673995584L;

	String serverId;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	
	
}
