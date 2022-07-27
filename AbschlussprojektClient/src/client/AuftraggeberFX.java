package client;

import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import klassen.Auftraggeber;
import klassen.Mitarbeiter;

public class AuftraggeberFX {
	private Auftraggeber serverAuftraggeber;
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty adresse;
	private SimpleStringProperty telefon;
	private SimpleStringProperty email;
	
	
	public AuftraggeberFX(Auftraggeber serverAuftraggeber) {
		super();
		this.serverAuftraggeber = serverAuftraggeber;
		id = new SimpleIntegerProperty(serverAuftraggeber.getId());
		name = new SimpleStringProperty(serverAuftraggeber.getName());
		adresse = new SimpleStringProperty(serverAuftraggeber.getAdresse());
		telefon = new SimpleStringProperty(serverAuftraggeber.getTelefon());
		email= new SimpleStringProperty(serverAuftraggeber.getEmail());
	}


	public Auftraggeber getServerAuftraggeber() {
		return serverAuftraggeber;
	}


	public void setServerAuftraggeber(Auftraggeber serverAuftraggeber) {
		this.serverAuftraggeber = serverAuftraggeber;
	}


	public final SimpleIntegerProperty idProperty() {
		return this.id;
	}
	


	public final int getId() {
		return this.idProperty().get();
	}
	


	public final void setId(final int id) {
		this.idProperty().set(id);
	}
	


	public final SimpleStringProperty nameProperty() {
		return this.name;
	}
	


	public final String getName() {
		return this.nameProperty().get();
	}
	


	public final void setName(final String name) {
		this.nameProperty().set(name);
	}
	


	public final SimpleStringProperty adresseProperty() {
		return this.adresse;
	}
	


	public final String getAdresse() {
		return this.adresseProperty().get();
	}
	


	public final void setAdresse(final String adresse) {
		this.adresseProperty().set(adresse);
	}
	


	public final SimpleStringProperty telefonProperty() {
		return this.telefon;
	}
	


	public final String getTelefon() {
		return this.telefonProperty().get();
	}
	


	public final void setTelefon(final String telefon) {
		this.telefonProperty().set(telefon);
	}
	


	public final SimpleStringProperty emailProperty() {
		return this.email;
	}
	


	public final String getEmail() {
		return this.emailProperty().get();
	}
	


	public final void setEmail(final String email) {
		this.emailProperty().set(email);
	}


	@Override
	public String toString() {
		return name.get();
	}
	
	
	
	
	

	

}
