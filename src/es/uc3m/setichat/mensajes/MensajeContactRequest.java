package es.uc3m.setichat.mensajes;

import java.util.List;

import es.uc3m.setichat.bd.Contacto;

public class MensajeContactRequest extends MensajeVacio {

	private List<Contacto> contactos;

	public List<Contacto> getContactos() {
		return contactos;
	}

	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}
	
	
}
