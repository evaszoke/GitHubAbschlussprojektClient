package client;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import klassen.Arbeitszeit;
import klassen.ArbeitszeitList;
import klassen.Meldung;
import klassen.Mitarbeiter;
import klassen.MitarbeiterList;
import klassen.Projekt;
import klassen.ProjektList;

public class ProjektArbeitszeitDetailDialog extends Dialog<ButtonType>{
	
	private ObservableList<ArbeitszeitFX> olProjektArbeitszeit = FXCollections.observableArrayList();
	private TextField gesamt = new TextField();
	
	public ProjektArbeitszeitDetailDialog(ArbeitszeitFX arbeitszeitFX) {
		this.setTitle("Projektzeitaufstellung");
		olProjektArbeitszeit.clear();
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(5));
		
		gp.add(new Label("Projekt"), 0, 0);
		
		
		ObservableList<ProjektFX> olpr = FXCollections.observableArrayList();
		ServiceFunctionsReturn sfrPr = ServiceFunctions.get("projektlist", null);
		if(sfrPr.isRc()) {
			ProjektList pl = new ProjektList(sfrPr.getLine());
			for(Projekt einP : pl.getProjekte()) {
				olpr.add(new ProjektFX(einP));
			}
		}
		ComboBox <ProjektFX> cobPr = new ComboBox<>();
		cobPr.setItems(olpr);
		gp.add(cobPr, 1, 0);
		
		gp.add(new Label("Datum von"), 0, 1);
		DatePicker dpDatumVon = new DatePicker();   
		dpDatumVon.setPrefWidth(200);
		gp.add(dpDatumVon, 1, 1);
		
		gp.add(new Label("Datum bis"), 0, 2);
		DatePicker dpDatumBis = new DatePicker();   
		dpDatumBis.setPrefWidth(200);
		gp.add(dpDatumBis, 1, 2);
		
		Button abfragen = new Button("Abfragen");
		gp.add(abfragen, 0, 3);
		Button fakturieren = new Button("Fakturieren");
		gp.add(fakturieren, 0, 4);
		
		
		TableColumn<ArbeitszeitFX, Projekt> projektName = new TableColumn<>("Projekt");
		projektName.setPrefWidth(150);
		projektName.setCellValueFactory(new PropertyValueFactory<>("projekt"));
		TableColumn<ArbeitszeitFX, Mitarbeiter> mitarbeiterName = new TableColumn<>("Mitarbeiter");
		mitarbeiterName.setPrefWidth(150);
		mitarbeiterName.setCellValueFactory(new PropertyValueFactory<>("mitarbeiter"));
		TableColumn<ArbeitszeitFX, LocalDate> datumColAz = new TableColumn<>("Datum");
		datumColAz.setPrefWidth(150);
		datumColAz.setCellValueFactory(new PropertyValueFactory<>("datum"));
		TableColumn<ArbeitszeitFX, Double> stundensatzColAz = new TableColumn<>("Stundensatz");
		stundensatzColAz.setPrefWidth(150);
		stundensatzColAz.setCellValueFactory(new PropertyValueFactory<>("stundensatz"));
		TableColumn<ArbeitszeitFX, Double> stundenGesamt = new TableColumn<>("StundenGesamt");
		stundenGesamt.setPrefWidth(150);
		stundenGesamt.setCellValueFactory(new PropertyValueFactory<>("stundengesamt"));
		

		TableView<ArbeitszeitFX> tvProjektArbeitszeit = new TableView<>(olProjektArbeitszeit);
		tvProjektArbeitszeit.getColumns().addAll(projektName, mitarbeiterName, datumColAz, stundensatzColAz, stundenGesamt);
		
		
		HBox hb = new HBox(10, new Label("Projektpreis gesamt: "), gesamt);
		VBox vb = new VBox(10, gp, tvProjektArbeitszeit, hb);
		
		this.getDialogPane().setContent(vb);
		
		abfragen.setOnAction(e -> abfragenProjektArbeitszeit(
				cobPr.getSelectionModel().getSelectedItem().getServerProjekt().getId(), 
				dpDatumVon.getValue(), dpDatumBis.getValue()));
		
		ButtonType beenden = new ButtonType("Beenden", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(beenden);
		
		
		
		
	}

	private void abfragenProjektArbeitszeit(int id, LocalDate value, LocalDate value2) {
		olProjektArbeitszeit.clear();
		gesamt.setText("");
		
		String paths = Integer.toString(id) + "/" + value.toString() + "/" + value2.toString();

		ServiceFunctionsReturn sfr = ServiceFunctions.get("projektarbeitszeitlist", paths);
		if(sfr.isRc()) {
			//ArbeitszeitList aus der XML Darstellung vom Server deserialisieren
			ArbeitszeitList al = new ArbeitszeitList(sfr.getLine());
			for(Arbeitszeit einAz : al.getArbeitszeiten()) {
				olProjektArbeitszeit.add(new ArbeitszeitFX(einAz));
			}
			double gesamtProjektPreis = 0;
			for(ArbeitszeitFX einAZ : olProjektArbeitszeit) {
				gesamtProjektPreis += einAZ.getStundengesamt() * einAZ.getStundensatz();
			}
			gesamt.setText(Double.toString(gesamtProjektPreis));
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}
	}


}
