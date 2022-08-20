package client;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class AuftraggeberDetailDialog extends Dialog<ButtonType>{
	public AuftraggeberDetailDialog(AuftraggeberFX auftraggeberFX) {
		this.setTitle("Details des Auftraggebers");
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(5));
		gp.setId("dialog");
		this.getDialogPane().getStylesheets().add("Style.css");

		gp.add(new Label("Id"), 0, 0);
		gp.add(new Label(Integer.toString(auftraggeberFX.getId())), 1, 0);

		TextField txtName = new TextField(auftraggeberFX.getName());
		txtName.setPrefWidth(200);
		gp.add(new Label("Name"), 0, 1);
		gp.add(txtName, 1, 1);

		TextField txtAdresse = new TextField(auftraggeberFX.getAdresse());
		txtAdresse.setPrefWidth(200);
		gp.add(new Label("Adresse"), 0, 2);
		gp.add(txtAdresse, 1, 2);
		
		TextField txtTelefon = new TextField(auftraggeberFX.getTelefon());
		txtTelefon.setPrefWidth(100);
		gp.add(new Label("Telefonnummer"), 0, 3);
		gp.add(txtTelefon, 1, 3);
		
		TextField txtEmail = new TextField(auftraggeberFX.getEmail());
		txtEmail.setPrefWidth(200);
		gp.add(new Label("Email Adresse"), 0, 4);
		gp.add(txtEmail, 1, 4);


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
		});

		// Abschließende Aktion bei Verlassen des Dialogs
		this.setResultConverter(new Callback<ButtonType, ButtonType>(){

			@Override
			public ButtonType call(ButtonType arg0) {
				// arg0 beschreibt den ButtonType der geklickt wurde
				//wenn speichern geklickt wurde, dann den Mitarbeiter an den Server schicken
				if(arg0 == speichern) {
					// Informationen aus den TextField in das Wein Objekt übertragen
					auftraggeberFX.getServerAuftraggeber().setName(txtName.getText());
					auftraggeberFX.getServerAuftraggeber().setAdresse(txtAdresse.getText());
					auftraggeberFX.getServerAuftraggeber().setTelefon(txtTelefon.getText());
					auftraggeberFX.getServerAuftraggeber().setEmail(txtEmail.getText());
			
					//falls id 0, neuen Auftraggeber posten (einfügen), sonst vonhanden Auftraggeber putten(update)
					
					ServiceFunctionsReturn sfr = auftraggeberFX.getId() == 0 ? ServiceFunctions.post("auftraggeber", Long.toString(auftraggeberFX.getId()), auftraggeberFX.getServerAuftraggeber().toXML()) 
							: ServiceFunctions.put("auftraggeber", Long.toString(auftraggeberFX.getId()), auftraggeberFX.getServerAuftraggeber().toXML());

				}
				return arg0;
			}
		});
	}


}
