package client;

import java.time.LocalDate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import klassen.Mitarbeiter;

public class MitarbeiterFX {
	
	//Instanzvariablen
	private Mitarbeiter serverMitarbeiter;
	private SimpleIntegerProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty adresse;
	private SimpleObjectProperty <LocalDate> geburtsdat;
	private SimpleStringProperty svNummer;
	private SimpleStringProperty telefon;
	private SimpleStringProperty email;
	private SimpleDoubleProperty wochenarbeitszeit;
	private SimpleDoubleProperty stundensatz;
	
	//Konstruktor
	public MitarbeiterFX(Mitarbeiter serverMitarbeiter) {
		super();
		this.serverMitarbeiter = serverMitarbeiter;
		id = new SimpleIntegerProperty(serverMitarbeiter.getId());
		name = new SimpleStringProperty(serverMitarbeiter.getName());
		adresse = new SimpleStringProperty(serverMitarbeiter.getAdresse());
		geburtsdat = new SimpleObjectProperty<LocalDate>(serverMitarbeiter.getGeburtsdat());
		svNummer = new SimpleStringProperty(serverMitarbeiter.getSvNummer());
		telefon = new SimpleStringProperty(serverMitarbeiter.getTelefon());
		email= new SimpleStringProperty(serverMitarbeiter.getEmail());
		wochenarbeitszeit = new SimpleDoubleProperty(serverMitarbeiter.getWochenarbeitszeit());
		stundensatz = new SimpleDoubleProperty(serverMitarbeiter.getStundensatz());
		
	}
	
	
	//Getters und Setters
	public Mitarbeiter getServerMitarbeiter() {
		return serverMitarbeiter;
	}



	public void setServerMitarbeiter(Mitarbeiter serverMitarbeiter) {
		this.serverMitarbeiter = serverMitarbeiter;
	}


	//FX Getters und Setters
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
	


	public final SimpleObjectProperty<LocalDate> geburtsdatProperty() {
		return this.geburtsdat;
	}
	


	public final LocalDate getGeburtsdat() {
		return this.geburtsdatProperty().get();
	}
	


	public final void setGeburtsdat(final LocalDate geburtsdat) {
		this.geburtsdatProperty().set(geburtsdat);
	}
	


	public final SimpleStringProperty svNummerProperty() {
		return this.svNummer;
	}
	


	public final String getSvNummer() {
		return this.svNummerProperty().get();
	}
	


	public final void setSvNummer(final String svNummer) {
		this.svNummerProperty().set(svNummer);
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
	
	public final SimpleDoubleProperty wochenarbeitszeitProperty() {
		return this.wochenarbeitszeit;
	}
	
	public final double getWochenarbeitszeit() {
		return this.wochenarbeitszeitProperty().get();
	}
	
	public final void setWochenarbeitszeit(final double wochenarbeitszeit) {
		this.wochenarbeitszeitProperty().set(wochenarbeitszeit);
	}
	
	public final SimpleDoubleProperty stundensatzProperty() {
		return this.stundensatz;
	}
	


	public final double getStundensatz() {
		return this.stundensatzProperty().get();
	}
	


	public final void setStundensatz(final double stundensatz) {
		this.stundensatzProperty().set(stundensatz);
	}

	
	//toString Methode
	@Override
	public String toString() {
		return name.get();
	}



	
	
	
	
	
	
	
	


}
