package client;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import klassen.Mitarbeiter;

public class MitarbeiterDetailDialog extends Dialog<ButtonType> {
	public MitarbeiterDetailDialog(MitarbeiterFX mitarbeiterFX) {
		this.setTitle("Details des Mitarbeiters");
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(5));

		gp.add(new Label("Id"), 0, 0);
		gp.add(new Label(Integer.toString(mitarbeiterFX.getId())), 1, 0);

		TextField txtName = new TextField(mitarbeiterFX.getName());
		txtName.setPrefWidth(200);
		gp.add(new Label("Name"), 0, 1);
		gp.add(txtName, 1, 1);

		TextField txtAdresse = new TextField(mitarbeiterFX.getAdresse());
		txtAdresse.setPrefWidth(200);
		gp.add(new Label("Adresse"), 0, 2);
		gp.add(txtAdresse, 1, 2);
		
		DatePicker dpGeburtsdat = new DatePicker(mitarbeiterFX.getGeburtsdat());   
		dpGeburtsdat.setPrefWidth(200);
		gp.add(new Label("Geburtsdatum"), 0, 3);
		gp.add(dpGeburtsdat, 1, 3);
		
		TextField txtSvNummer = new TextField(mitarbeiterFX.getSvNummer());
		txtSvNummer.setPrefWidth(100);
		gp.add(new Label("SV Nummer"), 0, 4);
		gp.add(txtSvNummer, 1, 4);
		
		TextField txtTelefon = new TextField(mitarbeiterFX.getTelefon());
		txtTelefon.setPrefWidth(100);
		gp.add(new Label("Telefonnummer"), 0, 5);
		gp.add(txtTelefon, 1, 5);
		
		TextField txtEmail = new TextField(mitarbeiterFX.getEmail());
		txtEmail.setPrefWidth(200);
		gp.add(new Label("Email Adresse"), 0, 6);
		gp.add(txtEmail, 1, 6);

		TextField txtArbeitszeit = new TextField(Double.toString(mitarbeiterFX.getWochenarbeitszeit()));
		txtArbeitszeit.setPrefWidth(100);
		gp.add(new Label("Arbeitszeit"), 0, 7);
		gp.add(txtArbeitszeit, 1, 7);
		
		TextField txtStundensatz = new TextField(Double.toString(mitarbeiterFX.getStundensatz()));
		txtStundensatz.setPrefWidth(100);
		gp.add(new Label("Stundensatz"), 0, 8);
		gp.add(txtStundensatz, 1, 8);


		this.getDialogPane().setContent(gp);

		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType beenden = new ButtonType("Beenden", ButtonData.CANCEL_CLOSE);

		this.getDialogPane().getButtonTypes().addAll(speichern, beenden);

		//EventFilter für den Speichern Button, damit die Eingaben geprüft werden können
		
		Button btnSpeichern = (Button) this.getDialogPane().lookupButton(speichern);
		btnSpeichern.addEventFilter(ActionEvent.ACTION, e -> {
			if(txtName.getText() == null || txtName.getText().length() == 0) {
				new Alert(AlertType.ERROR, "Name eingeben").showAndWait();
				//Event als abgeschlossen markieren, wird nicht mehr weiterverarbeitet
				e.consume();
				return;
			}
			
			if(txtStundensatz.getText() == null || txtStundensatz.getText().length() == 0 || Double.parseDouble(txtStundensatz.getText()) == 0) {
				new Alert(AlertType.ERROR, "Stundensatz eingeben").showAndWait();
				//Event als abgeschlossen markieren, wird nicht mehr weiterverarbeitet
				e.consume();
				return;
			}
			try {
				Double.parseDouble(txtStundensatz.getText());

			}
			catch(NumberFormatException e1) {
				new Alert(AlertType.ERROR, "Bitte Gleitkommazahl eingeben").showAndWait();
				e.consume();
				return;
			}
		});

		// Abschließende Aktion bei Verlassen des Dialogs
		this.setResultConverter(new Callback<ButtonType, ButtonType>(){

			@Override
			public ButtonType call(ButtonType arg0) {
				// arg0 beschreibt den ButtonType der geklickt wurde
				//wenn speichern geklickt wurde, dann den Mitarbeiter an den Server schicken
				if(arg0 == speichern) {
					// Informationen aus den TextField in das Wein Objekt übertragen
					mitarbeiterFX.getServerMitarbeiter().setName(txtName.getText());
					mitarbeiterFX.getServerMitarbeiter().setAdresse(txtAdresse.getText());
					mitarbeiterFX.getServerMitarbeiter().setGeburtsdat(dpGeburtsdat.getValue());
					mitarbeiterFX.getServerMitarbeiter().setSvNummer(txtSvNummer.getText());
					mitarbeiterFX.getServerMitarbeiter().setTelefon(txtTelefon.getText());
					mitarbeiterFX.getServerMitarbeiter().setEmail(txtEmail.getText());
					mitarbeiterFX.getServerMitarbeiter().setWochenarbeitszeit(Double.parseDouble(txtArbeitszeit.getText()));
					mitarbeiterFX.getServerMitarbeiter().setStundensatz(Double.parseDouble(txtStundensatz.getText()));
			
					//falls id 0, neuen Mitarbeiter posten (einfügen), sonst vonhanden Mitarbeiter putten(update)
				
					//ServiceFunctionsReturn sfr = ServiceFunctions.post("mitarbeiter", Long.toString(mitarbeiterFX.getId()), mitarbeiterFX.getServerMitarbeiter().toXML());
					
					
					ServiceFunctionsReturn sfr = mitarbeiterFX.getId() == 0 ? ServiceFunctions.post("mitarbeiter", Long.toString(mitarbeiterFX.getId()), mitarbeiterFX.getServerMitarbeiter().toXML()) 
							: ServiceFunctions.put("mitarbeiter", Long.toString(mitarbeiterFX.getId()), mitarbeiterFX.getServerMitarbeiter().toXML());

//					ServiceFunctionsReturn sfr = mitarbeiterFX.getId() == 0 ? ServiceFunctions.post(mitarbeiterFX.getServerMitarbeiter(), mitarbeiterFX.getId());
//							ServiceFunctions.postWein(weinFX.getServerWein()) : ServiceFunctions.putWein(weinFX.getServerWein());
//					if(!sfr.isRc()) {
//						new Alert(AlertType.ERROR, sfr.getMeldung()).showAndWait();
//						return null;
//					}
				}
				return arg0;
			}
		});
	}


}
