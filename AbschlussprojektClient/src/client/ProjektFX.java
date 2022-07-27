package client;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import klassen.Auftraggeber;
import klassen.Projekt;

public class ProjektFX {
	
	private Projekt serverProjekt;
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty adresse;
	private SimpleStringProperty telefon;
	private SimpleStringProperty kontaktperson;
	private Auftraggeber auftraggeber;
	
	public ProjektFX(Projekt serverProjekt) {
		super();
		this.serverProjekt = serverProjekt;
		this.auftraggeber = serverProjekt.getAuftraggeber();
		id = new SimpleIntegerProperty(serverProjekt.getId());
		name = new SimpleStringProperty(serverProjekt.getName());
		adresse = new SimpleStringProperty(serverProjekt.getAdresse());
		telefon = new SimpleStringProperty(serverProjekt.getTelefon());
		kontaktperson = new SimpleStringProperty(serverProjekt.getKontaktperson());
	}

	public Projekt getServerProjekt() {
		return serverProjekt;
	}

	public void setServerProjekt(Projekt serverProjekt) {
		this.serverProjekt = serverProjekt;
	}

	public Auftraggeber getAuftraggeber() {
		return auftraggeber;
	}

	public void setAuftraggeber(Auftraggeber auftraggeber) {
		this.auftraggeber = auftraggeber;
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
	

	public final SimpleStringProperty kontaktpersonProperty() {
		return this.kontaktperson;
	}
	

	public final String getKontaktperson() {
		return this.kontaktpersonProperty().get();
	}
	

	public final void setKontaktperson(final String kontaktperson) {
		this.kontaktpersonProperty().set(kontaktperson);
	}

	@Override
	public String toString() {
		return name.get();
	}

	
	
	
	
	
	
	
	
	
	

}