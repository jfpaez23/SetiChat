package es.uc3m.setichat.mensajes;

public class MensajeSignUp extends MensajeVacio {

	// El mensaje de SignUp contiene el nick y el movil del usuario.
	private String nick;
	private String mobile;
	
	public MensajeSignUp(){
		
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
