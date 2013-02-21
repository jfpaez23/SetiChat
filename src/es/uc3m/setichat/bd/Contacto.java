package es.uc3m.setichat.bd;

import java.io.Serializable;

public class Contacto implements Serializable {

	private String nick;
	private String mobile;
	
	public Contacto(){
		
	}
	
	public Contacto(String nick, String mobile){
		this.nick = nick;
		this.mobile = mobile;
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
