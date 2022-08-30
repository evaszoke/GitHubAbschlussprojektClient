package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import klassen.Auftraggeber;
import klassen.AuftraggeberList;

public class ProjektDetailDialog extends Dialog<ButtonType>{

	public ProjektDetailDialog(ProjektFX projektFX) {
		//GUI Elemente des Dialog Fensters
		this.setTitle("Details des Projekts");
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(5));
		gp.setId("dialog");
		this.getDialogPane().getStylesheets().add("Style.css");


		gp.add(new Label("Id"), 0, 0);
		gp.add(new Label(Integer.toString(projektFX.getId())), 1, 0);

		//Liste für ComboBox Auftraggeber
		ObservableList<AuftraggeberFX> olag = FXCollections.observableArrayList();

		ServiceFunctionsReturn sfrAg = ServiceFunctions.get("auftraggeberlist", null);
		if(sfrAg.isRc()) {

			AuftraggeberList al = new AuftraggeberList(sfrAg.getLine());
			for(Auftraggeber einAg : al.getAuftraggeberElemente()) {
				olag.add(new AuftraggeberFX(einAg));
			}
		}


		ComboBox <AuftraggeberFX> cobAg = new ComboBox<>();
		cobAg.setItems(olag);
		if(projektFX.getId() != 0) {
			int i = 0;
			for(AuftraggeberFX einA : olag) {
				if(einA.getId() == projektFX.getAuftraggeber().getId()) {
					cobAg.getSelectionModel().select(i);
					break;
				}
				else {
					i++;
				}
			}
		}


		gp.add(new Label("Auftraggeber"), 0, 1);
		gp.add(cobAg, 1, 1);


		TextField txtName = new TextField(projektFX.getName());
		txtName.setPrefWidth(200);
		gp.add(new Label("Name"), 0, 2);
		gp.add(txtName, 1, 2);

		TextField txtAdresse = new TextField(projektFX.getAdresse());
		txtAdresse.setPrefWidth(200);
		gp.add(new Label("Adresse"), 0, 3);
		gp.add(txtAdresse, 1, 3);

		TextField txtTelefon = new TextField(projektFX.getTelefon());
		txtTelefon.setPrefWidth(100);
		gp.add(new Label("Telefonnummer"), 0, 4);
		gp.add(txtTelefon, 1, 4);

		TextField txtKontakt = new TextField(projektFX.getKontaktperson());
		txtKontakt.setPrefWidth(200);
		gp.add(new Label("Kontaktperson"), 0, 5);
		gp.add(txtKontakt, 1, 5);

		CheckBox cb = new CheckBox("Abgeschlossen");
		cb.setPrefWidth(200);
		gp.add(cb, 0, 6);

		if(projektFX.isAbgeschlossen()) {
			cb.setSelected(true);
		}
		else{ 
			cb.setSelected(false);
		}


		this.getDialogPane().setContent(gp);

		//ButtonTypes des Dialog Fensters
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
				//wenn speichern geklickt wurde, dann den Projekt an den Server schicken
				if(arg0 == speichern) {
					// Informationen aus den TextField übertragen
					projektFX.getServerProjekt().setAuftraggeber(cobAg.getSelectionModel().getSelectedItem().getServerAuftraggeber());
					projektFX.getServerProjekt().setName(txtName.getText());
					projektFX.getServerProjekt().setAdresse(txtAdresse.getText());
					projektFX.getServerProjekt().setTelefon(txtTelefon.getText());
					projektFX.getServerProjekt().setKontaktperson(txtKontakt.getText());

					if(cb.isSelected())
						projektFX.getServerProjekt().setAbgeschlossen(true);
					else
						projektFX.getServerProjekt().setAbgeschlossen(false);




					//falls id 0, neuen Projekt posten (einfügen), sonst vonhandenen Projekt putten(update)

					ServiceFunctionsReturn sfr = projektFX.getId() == 0 ? ServiceFunctions.post("projekt", Long.toString(projektFX.getId()), projektFX.getServerProjekt().toXML()) 
							: ServiceFunctions.put("projekt", Long.toString(projektFX.getId()), projektFX.getServerProjekt().toXML());

				}
				return arg0;
			}
		});
	}

}
