package client;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class MitarbeiterArbeitszeitDetailDialog extends Dialog<ButtonType>{
	
	private ObservableList<ArbeitszeitFX> olMitarbeiterArbeitszeit = FXCollections.observableArrayList();
	private TextField gesamt = new TextField();
	
	public MitarbeiterArbeitszeitDetailDialog(ArbeitszeitFX arbeitszeitFX) {
		this.setTitle("Arbeitszeitaufstellung");
		olMitarbeiterArbeitszeit.clear();
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(5));
		gp.setId("dialog");
		this.getDialogPane().getStylesheets().add("Style.css");
		
		gp.add(new Label("Mitarbeiter"), 0, 0);
		
		
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
		gp.add(cobMa, 1, 0);
		
		gp.add(new Label("Datum von"), 0, 1);
		DatePicker dpDatumVon = new DatePicker();   
		dpDatumVon.setPrefWidth(200);
		gp.add(dpDatumVon, 1, 1);
		
		gp.add(new Label("Datum bis"), 0, 2);
		DatePicker dpDatumBis = new DatePicker();   
		dpDatumBis.setPrefWidth(200);
		gp.add(dpDatumBis, 1, 2);
		
//		HBox hbButton = new HBox();
		Button abfragen = new Button("Abfragen");
		
		TableColumn<ArbeitszeitFX, Mitarbeiter> mitarbeiterName = new TableColumn<>("Mitarbeiter");
		mitarbeiterName.setPrefWidth(150);
		mitarbeiterName.setCellValueFactory(new PropertyValueFactory<>("mitarbeiter"));
		TableColumn<ArbeitszeitFX, LocalDate> datumColAz = new TableColumn<>("Datum");
		datumColAz.setPrefWidth(150);
		datumColAz.setCellValueFactory(new PropertyValueFactory<>("datum"));
		TableColumn<ArbeitszeitFX, Double> stundenGesamt = new TableColumn<>("StundenGesamt");
		stundenGesamt.setPrefWidth(150);
		stundenGesamt.setCellValueFactory(new PropertyValueFactory<>("stundengesamt"));
		TableColumn<ArbeitszeitFX, Projekt> projektName = new TableColumn<>("Projekt");
		projektName.setPrefWidth(150);
		projektName.setCellValueFactory(new PropertyValueFactory<>("projekt"));

		TableView<ArbeitszeitFX> tvMitarbeiterArbeitszeit = new TableView<>(olMitarbeiterArbeitszeit);
		tvMitarbeiterArbeitszeit.getColumns().addAll(mitarbeiterName, datumColAz, projektName, stundenGesamt);
		
		
		HBox hb = new HBox(10, new Label("Arbeitszeit gesamt: "), gesamt);
		hb.setId("HBox");
		VBox vb = new VBox(10, gp, abfragen, tvMitarbeiterArbeitszeit, hb);
		
		this.getDialogPane().setContent(vb);
		
		abfragen.setOnAction(e -> abfragenMitarbeiterArbeitszeit(
				cobMa.getSelectionModel().getSelectedItem().getServerMitarbeiter().getId(), 
				dpDatumVon.getValue(), dpDatumBis.getValue()));
		
		ButtonType beenden = new ButtonType("Beenden", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(beenden);
		
		
		
		
	}

	private void abfragenMitarbeiterArbeitszeit(int id, LocalDate value, LocalDate value2) {
		olMitarbeiterArbeitszeit.clear();
		gesamt.setText("");
		
		String paths = Integer.toString(id) + "/" + value.toString() + "/" + value2.toString();

		ServiceFunctionsReturn sfr = ServiceFunctions.get("mitarbeiterarbeitszeitlist", paths);
		if(sfr.isRc()) {
			//ArbeitszeitList aus der XML Darstellung vom Server deserialisieren
			ArbeitszeitList al = new ArbeitszeitList(sfr.getLine());
			for(Arbeitszeit einAz : al.getArbeitszeiten()) {
				olMitarbeiterArbeitszeit.add(new ArbeitszeitFX(einAz));
			}
			double gesamtArbeitszeit = 0;
			for(ArbeitszeitFX einAZ : olMitarbeiterArbeitszeit) {
				gesamtArbeitszeit += einAZ.getStundengesamt();
			}
			gesamt.setText(Double.toString(gesamtArbeitszeit));
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}
	}

}
