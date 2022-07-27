package client;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import klassen.Arbeitszeit;
import klassen.Auftraggeber;
import klassen.Mitarbeiter;
import klassen.Projekt;

public class ArbeitszeitFX {
	private Arbeitszeit serverArbeitszeit;
	private SimpleIntegerProperty zeilennummer;
	private SimpleObjectProperty <LocalDate> datum;
	private SimpleObjectProperty <Mitarbeiter> mitarbeiter;
	private SimpleObjectProperty <Projekt> projekt;
	private SimpleStringProperty von;
	private SimpleStringProperty bis;
	private SimpleDoubleProperty stundengesamt;
	private SimpleDoubleProperty stundensatz;
	
	
	
	public ArbeitszeitFX(Arbeitszeit serverArbeitszeit) {
		super();
		this.serverArbeitszeit = serverArbeitszeit;
		mitarbeiter = new SimpleObjectProperty<Mitarbeiter>(serverArbeitszeit.getMitarbeiter());
		projekt = new SimpleObjectProperty<Projekt>(serverArbeitszeit.getProjekt());
		zeilennummer = new SimpleIntegerProperty(serverArbeitszeit.getZeilennummer());
		datum = new SimpleObjectProperty<LocalDate>(serverArbeitszeit.getDatum());
		von = new SimpleStringProperty(serverArbeitszeit.getVon());
		bis = new SimpleStringProperty(serverArbeitszeit.getBis());
		stundengesamt = new SimpleDoubleProperty(serverArbeitszeit.getStundengesamt());
		stundensatz = new SimpleDoubleProperty(serverArbeitszeit.getStundensatz());
		
	}


	public Arbeitszeit getServerArbeitszeit() {
		return serverArbeitszeit;
	}


	public void setServerArbeitszeit(Arbeitszeit serverArbeitszeit) {
		this.serverArbeitszeit = serverArbeitszeit;
	}

	
	public final SimpleIntegerProperty zeilennummerProperty() {
		return this.zeilennummer;
	}
	


	public final int getZeilennummer() {
		return this.zeilennummerProperty().get();
	}
	


	public final void setZeilennummer(final int zeilennummer) {
		this.zeilennummerProperty().set(zeilennummer);
	}
	


	public final SimpleObjectProperty<LocalDate> datumProperty() {
		return this.datum;
	}
	


	public final LocalDate getDatum() {
		return this.datumProperty().get();
	}
	


	public final void setDatum(final LocalDate datum) {
		this.datumProperty().set(datum);
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


	public final SimpleStringProperty vonProperty() {
		return this.von;
	}
	


	public final String getVon() {
		return this.vonProperty().get();
	}
	


	public final void setVon(final String von) {
		this.vonProperty().set(von);
	}
	


	public final SimpleStringProperty bisProperty() {
		return this.bis;
	}
	


	public final String getBis() {
		return this.bisProperty().get();
	}
	


	public final void setBis(final String bis) {
		this.bisProperty().set(bis);
	}


	public final SimpleDoubleProperty stundengesamtProperty() {
		return this.stundengesamt;
	}
	


	public final double getStundengesamt() {
		return this.stundengesamtProperty().get();
	}
	


	public final void setStundengesamt(final double stundengesamt) {
		this.stundengesamtProperty().set(stundengesamt);
	}


	public final SimpleObjectProperty<Mitarbeiter> mitarbeiterProperty() {
		return this.mitarbeiter;
	}
	


	public final Mitarbeiter getMitarbeiter() {
		return this.mitarbeiterProperty().get();
	}
	


	public final void setMitarbeiter(final Mitarbeiter mitarbeiter) {
		this.mitarbeiterProperty().set(mitarbeiter);
	}


	public final SimpleObjectProperty<Projekt> projektProperty() {
		return this.projekt;
	}
	


	public final Projekt getProjekt() {
		return this.projektProperty().get();
	}
	


	public final void setProjekt(final Projekt projekt) {
		this.projektProperty().set(projekt);
	}
	
	
	
	
	
	
}
