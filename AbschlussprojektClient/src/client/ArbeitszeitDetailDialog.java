package client;



import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import klassen.Mitarbeiter;
import klassen.MitarbeiterList;
import klassen.Projekt;
import klassen.ProjektList;

public class ArbeitszeitDetailDialog extends Dialog<ButtonType>{
	
	//Instanzvariable
	Double stundensatz;
	
	public ArbeitszeitDetailDialog(ArbeitszeitFX arbeitszeitFX) {
		//GUI Elemente des Dialog Fensters
		this.setTitle("Arbeitszeitaufzeichnung");
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(5));
		gp.setId("dialog");
		this.getDialogPane().getStylesheets().add("Style.css");

		gp.add(new Label("Zeilennummer"), 0, 0);
		gp.add(new Label(Integer.toString(arbeitszeitFX.getZeilennummer())), 1, 0);
		
		DatePicker dpDatum = new DatePicker(arbeitszeitFX.getDatum());   
		dpDatum.setPrefWidth(200);
		gp.add(new Label("Datum"), 0, 1);
		gp.add(dpDatum, 1, 1);
		
		//Liste f�r ComboBox Mitarbeiter 
		ObservableList<MitarbeiterFX> olma = FXCollections.observableArrayList();
		ServiceFunctionsReturn sfrMa = ServiceFunctions.get("mitarbeiterlist", null);
		if(sfrMa.isRc()) {
			MitarbeiterList ml = new MitarbeiterList(sfrMa.getLine());
			for(Mitarbeiter einMa : ml.getMitarbeiterElemente()) {
				olma.add(new MitarbeiterFX(einMa));
			}
		}
		ComboBox <MitarbeiterFX> cobMa = new ComboBox<>();
		cobMa.setItems(olma);
		if(arbeitszeitFX.getZeilennummer() != 0) {
			int i = 0;
			for(MitarbeiterFX einM : olma) {
				if(einM.getId() == arbeitszeitFX.getMitarbeiter().getId()) {
					cobMa.getSelectionModel().select(i);
					break;
				}
				else {
					i++;
				}
			}
		}
		gp.add(new Label("Mitarbeiter"), 0, 2);
		gp.add(cobMa, 1, 2);
		
		
		//Liste f�r ComboBox Projekte
		//In der Liste werden nur die nicht abgeschlossene Projekte angezeigt
		ObservableList<ProjektFX> olpr = FXCollections.observableArrayList();
		ServiceFunctionsReturn sfrPr = ServiceFunctions.get("projektlist", null);
		if(sfrPr.isRc()) {
			ProjektList pl = new ProjektList(sfrPr.getLine());
			for(Projekt einPr : pl.getProjekte()) {
				if (einPr.isAbgeschlossen() == false) {
					olpr.add(new ProjektFX(einPr));
				}
			}
		}
		ComboBox <ProjektFX> cobPr = new ComboBox<>();
		cobPr.setItems(olpr);
		if(arbeitszeitFX.getZeilennummer() != 0) {
			int i = 0;
			for(ProjektFX einP : olpr) {
				if(einP.getId() == arbeitszeitFX.getProjekt().getId()) {
					cobPr.getSelectionModel().select(i);
					break;
				}
				else {
					i++;
				}
			}
		}
		gp.add(new Label("Projekt"), 0, 3);
		gp.add(cobPr, 1, 3);
		
		//ComboBox von  
		ComboBox <String> cbVonSt = new ComboBox<>();  
		cbVonSt.setItems(FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"));
		ComboBox <String> cbVonMin = new ComboBox<>();  
		cbVonMin.setItems(FXCollections.observableArrayList("00", "15", "30", "45"));
		HBox von = new HBox(cbVonSt, cbVonMin);
		gp.add(new Label("Von"), 0, 4);
		gp.add(von, 1, 4);
		if(arbeitszeitFX.getZeilennummer() != 0) {
			String a = arbeitszeitFX.getVon().substring(0, 2);
			cbVonSt.getSelectionModel().select(a);
			String b = arbeitszeitFX.getVon().substring(3, 5);
			cbVonMin.getSelectionModel().select(b);
		}
		
		//ComboBox bis
		ComboBox <String> cbBisSt = new ComboBox<>();  
		cbBisSt.setItems(FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"));
		ComboBox <String> cbBisMin = new ComboBox<>();  
		cbBisMin.setItems(FXCollections.observableArrayList("00", "15", "30", "45"));
		HBox bis = new HBox(cbBisSt, cbBisMin);
		gp.add(new Label("Bis"), 0, 5);
		gp.add(bis, 1, 5);
		if(arbeitszeitFX.getZeilennummer() != 0) {
			String a = arbeitszeitFX.getBis().substring(0, 2);
			cbBisSt.getSelectionModel().select(a);
			String b = arbeitszeitFX.getBis().substring(3, 5);
			cbBisMin.getSelectionModel().select(b);
		}
	
		
		TextField txtStundensatz = new TextField(Double.toString(arbeitszeitFX.getStundensatz()));
		txtStundensatz.setPrefWidth(200);
		gp.add(new Label("Stundensatz"), 0, 6);
		gp.add(txtStundensatz, 1, 6);
		
		CheckBox cb = new CheckBox("Fakturiert");
		cb.setPrefWidth(200);
		gp.add(cb, 0, 7);
		
		if(arbeitszeitFX.isFakturiert()) {
			cb.setSelected(true);
		}
		else {
			cb.setSelected(false);
		}
		
		//Event Filter f�r ComboBox Mitarbeiter, beim erstmaligen Ausw�hlen des Mitarbeiters wird
		//der Stundensatz des Mitarbeiters �bernommen
		cobMa.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MitarbeiterFX>() {

			@Override
			public void changed(ObservableValue<? extends MitarbeiterFX> arg0, MitarbeiterFX arg1, MitarbeiterFX arg2) {
				if(arbeitszeitFX.getZeilennummer() == 0 && Double.parseDouble(txtStundensatz.getText()) == 0) {
					txtStundensatz.setText(Double.toString(arg2.getStundensatz()));
				}
				
			}});


		this.getDialogPane().setContent(gp);

		//ButtonTypes des Dialog Fensters
		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType beenden = new ButtonType("Beenden", ButtonData.CANCEL_CLOSE);

		this.getDialogPane().getButtonTypes().addAll(speichern, beenden);

		//EventFilter f�r den Speichern Button, damit die Eingaben gepr�ft werden k�nnen
		
		Button btnSpeichern = (Button) this.getDialogPane().lookupButton(speichern);
		btnSpeichern.addEventFilter(ActionEvent.ACTION, e -> {
			if(cobMa.getSelectionModel().getSelectedItem() == null) {
				new Alert(AlertType.ERROR, "Mitarbeiter ausw�hlen!").showAndWait();
				//Event als abgeschlossen markieren, wird nicht mehr weiterverarbeitet
				e.consume();
				return;
			}
			else if(cobPr.getSelectionModel().getSelectedItem() == null) {
				new Alert(AlertType.ERROR, "Projekt ausw�hlen!").showAndWait();
				//Event als abgeschlossen markieren, wird nicht mehr weiterverarbeitet
				e.consume();
				return;
			}
			else if(dpDatum.getValue() == null) {
				new Alert(AlertType.ERROR, "Datum eingeben!").showAndWait();
				//Event als abgeschlossen markieren, wird nicht mehr weiterverarbeitet
				e.consume();
				return;
			}
			else if(cbVonSt.getSelectionModel().getSelectedItem() == null) {
				new Alert(AlertType.ERROR, "Von Stunde eingeben!").showAndWait();
				//Event als abgeschlossen markieren, wird nicht mehr weiterverarbeitet
				e.consume();
				return;
			}
			else if(cbVonMin.getSelectionModel().getSelectedItem() == null) {
				new Alert(AlertType.ERROR, "Von Minute eingeben!").showAndWait();
				//Event als abgeschlossen markieren, wird nicht mehr weiterverarbeitet
				e.consume();
				return;
			}
			else if(cbBisSt.getSelectionModel().getSelectedItem() == null) {
				new Alert(AlertType.ERROR, "Bis Stunde eingeben!").showAndWait();
				//Event als abgeschlossen markieren, wird nicht mehr weiterverarbeitet
				e.consume();
				return;
			}
			else if(cbBisMin.getSelectionModel().getSelectedItem() == null) {
				new Alert(AlertType.ERROR, "Bis Minute eingeben!").showAndWait();
				//Event als abgeschlossen markieren, wird nicht mehr weiterverarbeitet
				e.consume();
				return;
			}
		});

		// Abschlie�ende Aktion bei Verlassen des Dialogs
		this.setResultConverter(new Callback<ButtonType, ButtonType>(){

			@Override
			public ButtonType call(ButtonType arg0) {
				// arg0 beschreibt den ButtonType der geklickt wurde
				//wenn speichern geklickt wurde, dann den Arbeitszeit an den Server schicken
				if(arg0 == speichern) {
					// Informationen aus den DatePicker / TextField / Combobox �bertragen
					
				
					arbeitszeitFX.getServerArbeitszeit().setDatum(dpDatum.getValue());
					arbeitszeitFX.getServerArbeitszeit().setMitarbeiter(cobMa.getSelectionModel().getSelectedItem().getServerMitarbeiter());
					arbeitszeitFX.getServerArbeitszeit().setProjekt(cobPr.getSelectionModel().getSelectedItem().getServerProjekt());
					
					
					int stunden = Integer.parseInt(cbVonSt.getSelectionModel().getSelectedItem());
							
					int minuten = Integer.parseInt(cbVonMin.getSelectionModel().getSelectedItem());
							
					String von = String.format("%02d:%02d", stunden, minuten);
					
					arbeitszeitFX.getServerArbeitszeit().setVon(von);
					
					
					int stundenBis = Integer.parseInt(cbBisSt.getSelectionModel().getSelectedItem());
					int minutenBis = Integer.parseInt(cbBisMin.getSelectionModel().getSelectedItem());
					String bis = String.format("%02d:%02d", stundenBis, minutenBis);
					
					arbeitszeitFX.getServerArbeitszeit().setBis(bis);
					
					double gesamt = ((stundenBis * 60 + minutenBis) - (stunden * 60 + minuten))/ 60.0;
					arbeitszeitFX.getServerArbeitszeit().setStundengesamt(gesamt);
						
					arbeitszeitFX.getServerArbeitszeit().setStundensatz(Double.parseDouble(txtStundensatz.getText()));
					if(cb.isSelected()) {
						arbeitszeitFX.getServerArbeitszeit().setFakturiert(true);
					}
					else {
						arbeitszeitFX.getServerArbeitszeit().setFakturiert(false);
					}
					
					//falls id 0, neue Arbeitszeitzeile posten (einf�gen), sonst vonhandene Arbeitszeitzeile putten(update)
					
					ServiceFunctionsReturn sfr = arbeitszeitFX.getZeilennummer() == 0 ? ServiceFunctions.post("arbeitszeit", Long.toString(arbeitszeitFX.getZeilennummer()), arbeitszeitFX.getServerArbeitszeit().toXML()) 
							: ServiceFunctions.put("arbeitszeit", Long.toString(arbeitszeitFX.getZeilennummer()), arbeitszeitFX.getServerArbeitszeit().toXML());

				}
				return arg0;
			}
		});
	}

}
