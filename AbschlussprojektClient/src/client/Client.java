package client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import javax.imageio.ImageIO;

import com.itextpdf.text.Font.FontFamily;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import klassen.Arbeitszeit;
import klassen.ArbeitszeitList;
import klassen.Auftraggeber;
import klassen.AuftraggeberList;
import klassen.Meldung;
import klassen.Mitarbeiter;
import klassen.MitarbeiterList;
import klassen.Projekt;
import klassen.ProjektList;

public class Client extends Application {

	private ObservableList<MitarbeiterFX> olMitarbeiter = FXCollections.observableArrayList();
	private ObservableList<AuftraggeberFX> olAuftraggeber = FXCollections.observableArrayList();
	private ObservableList<ProjektFX> olProjekt = FXCollections.observableArrayList();
	private ObservableList<ArbeitszeitFX> olArbeitszeit = FXCollections.observableArrayList();


	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) {

		//Tabs für TabPane erstellen
		Tab homeTab = new Tab();
		homeTab.setText("STARTSEITE");
		URI uri = Paths.get("C:\\Evi\\JavaWifi\\Abschlussprojekt\\bild_startseite.jpg").toUri();
		StackPane sp = new StackPane();
		ImageView iv = new ImageView(uri.toString());
		Text willkommen = new Text("Willkommen!");
		willkommen.setFill(Color.RED);
	
		sp.getChildren().add(iv);
		sp.getChildren().add(willkommen);
		sp.setId("Text_Startseite");
		sp.setAlignment(Pos.CENTER);
		homeTab.setContent(sp);
		
		Tab mitarbeiterTab = new Tab();
		mitarbeiterTab.setText("MITARBEITER");
		Tab auftraggeberTab = new Tab();
		auftraggeberTab.setText("AUFTRAGGEBER");
		Tab projektTab = new Tab();
		projektTab.setText("PROJEKT");
		Tab arbeitszeitTab = new Tab();
		arbeitszeitTab.setText("ARBEITSZEIT");

		//TabPane erstellen
		TabPane tabPane = new TabPane();
		tabPane.getTabs().addAll(homeTab, mitarbeiterTab, auftraggeberTab, projektTab, arbeitszeitTab);
		tabPane.setSide(Side.LEFT);
		tabPane.setRotateGraphic(false);
		tabPane.getStylesheets().add("Style.css");
		tabPane.setId("pane");
		
		

		//TableView Mitarbeiter
		TableColumn<MitarbeiterFX, Integer> idCol = new TableColumn<>("Id");
		idCol.setPrefWidth(50);
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<MitarbeiterFX, String> nameCol = new TableColumn<>("Name");
		nameCol.setPrefWidth(150);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<MitarbeiterFX, String> adresseCol = new TableColumn<>("Adresse");
		adresseCol.setPrefWidth(150);
		adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
		TableColumn<MitarbeiterFX, LocalDate> geburtsdatCol = new TableColumn<>("Geburtsdatum");
		geburtsdatCol.setPrefWidth(100);
		geburtsdatCol.setCellValueFactory(new PropertyValueFactory<>("geburtsdat"));
		TableColumn<MitarbeiterFX, String> svNummerCol = new TableColumn<>("SVNummer");
		svNummerCol.setPrefWidth(100);
		svNummerCol.setCellValueFactory(new PropertyValueFactory<>("svNummer"));
		TableColumn<MitarbeiterFX, String> telefonCol = new TableColumn<>("Telefon");
		telefonCol.setPrefWidth(150);
		telefonCol.setCellValueFactory(new PropertyValueFactory<>("telefon"));
		TableColumn<MitarbeiterFX, String> emailCol = new TableColumn<>("Email");
		emailCol.setPrefWidth(150);
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		TableColumn<MitarbeiterFX, Double> arbeitszeitCol = new TableColumn<>("Wochenarbeitszeit");
		arbeitszeitCol.setPrefWidth(150);
		arbeitszeitCol.setCellValueFactory(new PropertyValueFactory<>("wochenarbeitszeit"));
		TableColumn<MitarbeiterFX, Double> stundensatzCol = new TableColumn<>("Stundensatz");
		stundensatzCol.setPrefWidth(150);
		stundensatzCol.setCellValueFactory(new PropertyValueFactory<>("stundensatz"));

		TableView<MitarbeiterFX> tvMitarbeiter = new TableView<>(olMitarbeiter);
		tvMitarbeiter.getColumns().addAll(idCol, nameCol, adresseCol, geburtsdatCol, svNummerCol, 
				telefonCol, emailCol,  arbeitszeitCol, stundensatzCol);
		tvMitarbeiter.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		//Beim Starten Mitarbeiterliste lesen
		ServiceFunctionsReturn sfr = ServiceFunctions.get("mitarbeiterlist", null);
		if(sfr.isRc()) {
			//MitarbeiterList aus der XML Darstellung vom Server deserialisieren
			MitarbeiterList ml = new MitarbeiterList(sfr.getLine());
			for(Mitarbeiter einM : ml.getMitarbeiterElemente()) {
				olMitarbeiter.add(new MitarbeiterFX(einM));
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}

		//Buttons zu Mitarbeiter Tab
		Button neuMitarbeiter = new Button("Neu");
		Button bearbeitenMitarbeiter = new Button("Bearbeiten");
		bearbeitenMitarbeiter.setDisable(true);
		Button entfernenMitarbeiter = new Button("Entfernen");
		entfernenMitarbeiter.setDisable(true);


		tvMitarbeiter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MitarbeiterFX>() {

			@Override
			public void changed(ObservableValue<? extends MitarbeiterFX> arg0, MitarbeiterFX arg1, MitarbeiterFX arg2) {
				if(arg2 == null) {
					bearbeitenMitarbeiter.setDisable(true);
					entfernenMitarbeiter.setDisable(true);
				}
				else {
					bearbeitenMitarbeiter.setDisable(false);
					entfernenMitarbeiter.setDisable(false);
				}

			}

		});
		//Mitarbeiter Tab fertigstellen
		HBox buttonsMitarbeiter = new HBox(10, neuMitarbeiter, bearbeitenMitarbeiter, entfernenMitarbeiter);
		VBox vbMitarbeiter = new VBox(10, tvMitarbeiter, buttonsMitarbeiter);
		vbMitarbeiter.setPadding(new Insets(5));
		mitarbeiterTab.setContent(vbMitarbeiter);

		//Event Handler zu Buttons Mitarbeiter Tab
		neuMitarbeiter.setOnAction(e -> neuerMitarbeiter());
		entfernenMitarbeiter.setOnAction(e -> loescheMitarbeiter(tvMitarbeiter.getSelectionModel().getSelectedItem()));
		bearbeitenMitarbeiter.setOnAction(e -> bearbeiteMitarbeiter(tvMitarbeiter.getSelectionModel().getSelectedItem()));


		//TableView Auftraggeber
		TableColumn<AuftraggeberFX, Integer> idColAg = new TableColumn<>("Id");
		idColAg.setPrefWidth(100);
		idColAg.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<AuftraggeberFX, String> nameColAg = new TableColumn<>("Name");
		nameColAg.setPrefWidth(250);
		nameColAg.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<AuftraggeberFX, String> adresseColAg = new TableColumn<>("Adresse");
		adresseColAg.setPrefWidth(300);
		adresseColAg.setCellValueFactory(new PropertyValueFactory<>("adresse"));
		TableColumn<AuftraggeberFX, String> telefonColAg = new TableColumn<>("Telefon");
		telefonColAg.setPrefWidth(250);
		telefonColAg.setCellValueFactory(new PropertyValueFactory<>("telefon"));
		TableColumn<AuftraggeberFX, String> emailColAg = new TableColumn<>("Email");
		emailColAg.setPrefWidth(250);
		emailColAg.setCellValueFactory(new PropertyValueFactory<>("email"));


		TableView<AuftraggeberFX> tvAuftraggeber = new TableView<>(olAuftraggeber);
		tvAuftraggeber.getColumns().addAll(idColAg, nameColAg, adresseColAg,
				telefonColAg, emailColAg);
		tvAuftraggeber.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		//Beim Starten Auftraggeberliste lesen
		ServiceFunctionsReturn sfrAg = ServiceFunctions.get("auftraggeberlist", null);
		if(sfrAg.isRc()) {
			//AuftraggeberList aus der XML Darstellung vom Server deserialisieren
			AuftraggeberList al = new AuftraggeberList(sfrAg.getLine());
			for(Auftraggeber einAg : al.getAuftraggeberElemente()) {
				olAuftraggeber.add(new AuftraggeberFX(einAg));
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}

		//Buttons zu Auftraggeber Tab
		Button neuAuftraggeber = new Button("Neu");
		Button bearbeitenAuftraggeber = new Button("Bearbeiten");
		bearbeitenAuftraggeber.setDisable(true);
		Button entfernenAuftraggeber = new Button("Entfernen");
		entfernenAuftraggeber.setDisable(true);

		tvAuftraggeber.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AuftraggeberFX>() {

			@Override
			public void changed(ObservableValue<? extends AuftraggeberFX> arg0, AuftraggeberFX arg1, AuftraggeberFX arg2) {
				if(arg2 == null) {
					bearbeitenAuftraggeber.setDisable(true);
					entfernenAuftraggeber.setDisable(true);
				}
				else {
					bearbeitenAuftraggeber.setDisable(false);
					entfernenAuftraggeber.setDisable(false);
				}

			}

		});

		//Auftraggeber Tab fertigstellen
		HBox buttonsAuftraggeber = new HBox(10, neuAuftraggeber, bearbeitenAuftraggeber, entfernenAuftraggeber);
		VBox vbAuftraggeber = new VBox(10, tvAuftraggeber, buttonsAuftraggeber);
		vbAuftraggeber.setPadding(new Insets(5));
		auftraggeberTab.setContent(vbAuftraggeber);

		//Event Handler zu Buttons Auftraggeber Tab
		neuAuftraggeber.setOnAction(e -> neuerAuftraggeber());
		entfernenAuftraggeber.setOnAction(e -> loescheAuftraggeber(tvAuftraggeber.getSelectionModel().getSelectedItem()));
		bearbeitenAuftraggeber.setOnAction(e -> bearbeiteAuftraggeber(tvAuftraggeber.getSelectionModel().getSelectedItem()));


		//TableView Projekt
		TableColumn<ProjektFX, Integer> idColProj = new TableColumn<>("Id");
		idColProj.setPrefWidth(100);
		idColProj.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<ProjektFX, String> nameColProj = new TableColumn<>("Name");
		nameColProj.setPrefWidth(250);
		nameColProj.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<ProjektFX, String> adresseColProj = new TableColumn<>("Adresse");
		adresseColProj.setPrefWidth(300);
		adresseColProj.setCellValueFactory(new PropertyValueFactory<>("adresse"));
		TableColumn<ProjektFX, String> telefonColProj = new TableColumn<>("Telefon");
		telefonColProj.setPrefWidth(200);
		telefonColProj.setCellValueFactory(new PropertyValueFactory<>("telefon"));
		TableColumn<ProjektFX, String> kontaktCol = new TableColumn<>("Kontaktperson");
		kontaktCol.setPrefWidth(200);
		kontaktCol.setCellValueFactory(new PropertyValueFactory<>("kontaktperson"));
		TableColumn<ProjektFX, Boolean> abgeschlossenCol = new TableColumn<>("Abgeschlossen");
		abgeschlossenCol.setPrefWidth(100);
		abgeschlossenCol.setCellValueFactory(new PropertyValueFactory<>("abgeschlossen"));
		abgeschlossenCol.setCellFactory(col -> new TableCell<ProjektFX, Boolean>(){
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? null :
					item.booleanValue() ? "abgeschlossen" : "offen");
			}
		});
		
		
		
		

		TableView<ProjektFX> tvProjekt = new TableView<>(olProjekt);
		tvProjekt.getColumns().addAll(idColProj, nameColProj, adresseColProj, telefonColProj, kontaktCol, abgeschlossenCol);
		tvMitarbeiter.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		//Beim Starten Projektliste lesen
		ServiceFunctionsReturn sfrPr = ServiceFunctions.get("projektlist", null);
		if(sfrPr.isRc()) {
			//ProjektList aus der XML Darstellung vom Server deserialisieren
			ProjektList pl = new ProjektList(sfrPr.getLine());
			for(Projekt einP : pl.getProjekte()) {
				olProjekt.add(new ProjektFX(einP));
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfrPr.getLine()).toString()).showAndWait();
		}

		//Buttons zu Projekt Tab
		Button neuProjekt = new Button("Neu");
		Button bearbeitenProjekt = new Button("Bearbeiten");
		bearbeitenProjekt.setDisable(true);
		Button entfernenProjekt = new Button("Entfernen");
		entfernenProjekt.setDisable(true);

		tvProjekt.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProjektFX>() {

			@Override
			public void changed(ObservableValue<? extends ProjektFX> arg0, ProjektFX arg1, ProjektFX arg2) {
				if(arg2 == null) {
					bearbeitenProjekt.setDisable(true);
					entfernenProjekt.setDisable(true);
				}
				else {
					bearbeitenProjekt.setDisable(false);
					entfernenProjekt.setDisable(false);
				}

			}

		});

		//Projekt Tab fertigstellen
		HBox buttonsProjekt = new HBox(10, neuProjekt, bearbeitenProjekt, entfernenProjekt);
		VBox vbProjekt = new VBox(10, tvProjekt, buttonsProjekt);
		vbProjekt.setPadding(new Insets(5));
		projektTab.setContent(vbProjekt);


		//Event Handler zu Buttons Projekt Tab
		neuProjekt.setOnAction(e -> neuerProjekt());
		entfernenProjekt.setOnAction(e -> loescheProjekt(tvProjekt.getSelectionModel().getSelectedItem()));
		bearbeitenProjekt.setOnAction(e -> bearbeiteProjekt(tvProjekt.getSelectionModel().getSelectedItem()));

		//TableView Arbeitszeit
		TableColumn<ArbeitszeitFX, Integer> nrColAz = new TableColumn<>("Zeilennummer");
		nrColAz.setPrefWidth(100);
		nrColAz.setCellValueFactory(new PropertyValueFactory<>("zeilennummer"));
		TableColumn<ArbeitszeitFX, Mitarbeiter> mitarbeiterName = new TableColumn<>("Mitarbeiter");
		mitarbeiterName.setPrefWidth(150);
		mitarbeiterName.setCellValueFactory(new PropertyValueFactory<>("mitarbeiter"));
		TableColumn<ArbeitszeitFX, LocalDate> datumColAz = new TableColumn<>("Datum");
		datumColAz.setPrefWidth(150);
		datumColAz.setCellValueFactory(new PropertyValueFactory<>("datum"));
		TableColumn<ArbeitszeitFX, String> vonColAz = new TableColumn<>("Von");
		vonColAz.setPrefWidth(100);
		vonColAz.setCellValueFactory(new PropertyValueFactory<>("von"));
		TableColumn<ArbeitszeitFX, String> bisColAz = new TableColumn<>("Bis");
		bisColAz.setPrefWidth(100);
		bisColAz.setCellValueFactory(new PropertyValueFactory<>("bis"));
		TableColumn<ArbeitszeitFX, Double> stundenGesamt = new TableColumn<>("StundenGesamt");
		stundenGesamt.setPrefWidth(150);
		stundenGesamt.setCellValueFactory(new PropertyValueFactory<>("stundengesamt"));
		TableColumn<ArbeitszeitFX, Projekt> projektName = new TableColumn<>("Projekt");
		projektName.setPrefWidth(150);
		projektName.setCellValueFactory(new PropertyValueFactory<>("projekt"));
		TableColumn<ArbeitszeitFX, Double> stundensatzColAz = new TableColumn<>("Stundensatz");
		stundensatzColAz.setPrefWidth(100);
		stundensatzColAz.setCellValueFactory(new PropertyValueFactory<>("stundensatz"));
		TableColumn<ArbeitszeitFX, Boolean> fakturiertCol = new TableColumn<>("Fakturiert");
		fakturiertCol.setPrefWidth(150);
		fakturiertCol.setCellValueFactory(new PropertyValueFactory<>("fakturiert"));
		
		fakturiertCol.setCellFactory(col -> new TableCell<ArbeitszeitFX, Boolean>(){
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? null :
					item.booleanValue() ? "fakturiert" : " ");
			}
		});

		TableView<ArbeitszeitFX> tvArbeitszeit = new TableView<>(olArbeitszeit);
		tvArbeitszeit.getColumns().addAll(nrColAz, mitarbeiterName, datumColAz, projektName, vonColAz, bisColAz, stundenGesamt, stundensatzColAz, fakturiertCol);
		tvArbeitszeit.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		//Beim Starten Arbeitszeitliste lesen
		ServiceFunctionsReturn sfrAz = ServiceFunctions.get("arbeitszeitlist", null);
		if(sfrAz.isRc()) {
			//ArbeitszeitList aus der XML Darstellung vom Server deserialisieren
			ArbeitszeitList al = new ArbeitszeitList(sfrAz.getLine());
			for(Arbeitszeit einAz : al.getArbeitszeiten()) {
				olArbeitszeit.add(new ArbeitszeitFX(einAz));
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfrAz.getLine()).toString()).showAndWait();
		}

		//Buttons zu Arbeitszeit Tab
		Button neuArbeitszeit = new Button("Neu");
		Button bearbeitenArbeitszeit = new Button("Bearbeiten");
		bearbeitenArbeitszeit.setDisable(true);
		Button entfernenArbeitszeit = new Button("Entfernen");
		entfernenArbeitszeit.setDisable(true);
		Button arbeitszeitMitarbeiter = new Button("Arbeitszeitaufstellung Mitarbeiter");
		Button arbeitszeitProjekt = new Button("Arbeitszeitaufstellung Projekt");

		tvArbeitszeit.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ArbeitszeitFX>() {

			@Override
			public void changed(ObservableValue<? extends ArbeitszeitFX> arg0, ArbeitszeitFX arg1, ArbeitszeitFX arg2) {
				if(arg2 == null) {
					bearbeitenArbeitszeit.setDisable(true);
					entfernenArbeitszeit.setDisable(true);
				}
				else {
					bearbeitenArbeitszeit.setDisable(false);
					entfernenArbeitszeit.setDisable(false);
				}

			}

		});

		//Arbeitszeit Tab fertigstellen
		HBox buttonsArbeitszeit = new HBox(10, neuArbeitszeit, bearbeitenArbeitszeit, entfernenArbeitszeit, arbeitszeitMitarbeiter, arbeitszeitProjekt);
		VBox vbArbeitszeit = new VBox(10, tvArbeitszeit, buttonsArbeitszeit);
		vbArbeitszeit.setPadding(new Insets(5));
		arbeitszeitTab.setContent(vbArbeitszeit);

		//Event Hanlder zu Buttons Arbeitszeit Tab
		neuArbeitszeit.setOnAction(e -> neueArbeitszeit());
		
		entfernenArbeitszeit.setOnAction(e -> {
		if(tvArbeitszeit.getSelectionModel().getSelectedItem().isFakturiert() == true) {
			new Alert(AlertType.INFORMATION, "Die Arbeitszeitzeile wurde bereits fakturiert,\n"
					+ "kann nicht gelöscht werden").showAndWait();
		}
		else {
			loescheArbeitszeit(tvArbeitszeit.getSelectionModel().getSelectedItem());
		}
		});
		
		bearbeitenArbeitszeit.setOnAction(e -> {
			if(tvArbeitszeit.getSelectionModel().getSelectedItem().isFakturiert() == true) {
				new Alert(AlertType.INFORMATION, "Die Arbeitszeitzeile wurde bereits fakturiert,\n"
						+ "kann nicht bearbeitet werden").showAndWait();
			}
			else {
				bearbeiteArbeitszeit(tvArbeitszeit.getSelectionModel().getSelectedItem());
			}
			});
		
		arbeitszeitMitarbeiter.setOnAction(e -> abfragenArbeitszeit());
		arbeitszeitProjekt.setOnAction(e -> abfragenProjektzeit());



		primaryStage.setScene(new Scene(tabPane));
		primaryStage.setTitle("Zeiterfassung Tool");
		primaryStage.show();



	}

	//Methoden zu Buttons Arbeitszeit

	/**
	 * Aufrufen eines Dialogfensters zum Abfragen der Gesamtarbeitszeit zu einem ausgewählten Projekt
	 */
	private void abfragenProjektzeit() {
		ArbeitszeitFX arbeitszeitFX = new ArbeitszeitFX(new Arbeitszeit());
		new ProjektArbeitszeitDetailDialog(arbeitszeitFX).showAndWait();
		leseArbeitszeitliste();
	}

	/**
	 * Aufrufen eines Dialogfensters zum Abfragen der Gesamtarbeitszeit von einem ausgewählten Mitarbeiter
	 */
	private void abfragenArbeitszeit() {
		ArbeitszeitFX arbeitszeitFX = new ArbeitszeitFX(new Arbeitszeit());
		new MitarbeiterArbeitszeitDetailDialog(arbeitszeitFX).showAndWait();
	}

	/**
	 * Entfernen der ausgewählten Arbeitszeitzeile
	 * 
	 * @param arbeitszeitFX Arbeitszeit Objekt aus der TableView
	 */
	private void loescheArbeitszeit(ArbeitszeitFX arbeitszeitFX) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Wollen Sie die Arbeitszeitzeile löschen?");
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			ServiceFunctionsReturn sfr = ServiceFunctions.delete("arbeitszeit", Long.toString(arbeitszeitFX.getZeilennummer()));
			if(sfr.isRc()) {
				new Alert(AlertType.INFORMATION, "gelöscht.").showAndWait();
				leseArbeitszeitliste();
			}

			else {
				new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
			}
		}

	}

	/**
	 * Bearbeiten und aktualisieren der ausgewählten Arbeitszeitzeile
	 * 
	 * @param arbeitszeitFX Arbeitszeit Objekt aus der TableView
	 */
	private void bearbeiteArbeitszeit(ArbeitszeitFX arbeitszeitFX) {
		Optional<ButtonType> r = new ArbeitszeitDetailDialog(arbeitszeitFX).showAndWait();
		if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
			//neuer Arbeitszeitzeile wurde gespeichert, daher neue Arbeitszeitliste vom Server holen
			leseArbeitszeitliste();
		}
		else {
			new Alert(AlertType.INFORMATION, "nicht geändert").showAndWait();
		}
	}

	/**
	 * Erstellen einer neuen Arbeitszeitzeile
	 */
	private void neueArbeitszeit() {
		ArbeitszeitFX arbeitszeitFX = new ArbeitszeitFX(new Arbeitszeit());
		Optional<ButtonType> r = new ArbeitszeitDetailDialog(arbeitszeitFX).showAndWait();
		if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
			leseArbeitszeitliste();
		}
	}

	/**
	 * Objekte aus der Arbeitszeit Tabelle werden in eine ArrayList gespeichert
	 */
	private void leseArbeitszeitliste() {
		olArbeitszeit.clear();
		ServiceFunctionsReturn sfr = ServiceFunctions.get("arbeitszeitlist", null);
		if(sfr.isRc()) {
			//ArbeitszeitList aus der XML Darstellung vom Server deserialisieren
			ArbeitszeitList al = new ArbeitszeitList(sfr.getLine());
			for(Arbeitszeit einAz : al.getArbeitszeiten()) {
				olArbeitszeit.add(new ArbeitszeitFX(einAz));
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}

	}

	//Methoden zu Buttons Projekt

	/**
	 * Entfernen des ausgewählten Projektes
	 * Falls das Projekt bereits in einer Arbeitszeitzeile verwendet wurde, dann kann nicht mehr gelöscht werden
	 * 
	 * @param projektFX Projekt Objekt aus der TableView
	 */
	private void loescheProjekt(ProjektFX projektFX) {
		boolean inVerwendung = false;
		for(ArbeitszeitFX einAz : olArbeitszeit) {
			if(projektFX.getId() == einAz.getProjekt().getId()) {
				new Alert(AlertType.INFORMATION, "Für das Projekt " + projektFX.getName() +  " wurden bereits Arbeitszeiten eingegeben\n,"
						+ " kann nicht gelöscht werden").showAndWait();
				inVerwendung = true;
				break;
			}
		}
		if(inVerwendung == false) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Wollen Sie das Projekt " + projektFX.getName() +  " löschen?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				ServiceFunctionsReturn sfr = ServiceFunctions.delete("projekt", Long.toString(projektFX.getId()));
				if(sfr.isRc()) {
					new Alert(AlertType.INFORMATION, "gelöscht.").showAndWait();
					leseProjektliste();
				}
				else {
					new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
				}
			}



		}

	}

	/**
	 * Bearbeiten und aktualisieren des ausgewählten Projektes
	 * 
	 * @param projektFX Projekt Objekt aus der TableView
	 */
	private void bearbeiteProjekt(ProjektFX projektFX) {
		Optional<ButtonType> r = new ProjektDetailDialog(projektFX).showAndWait();
		if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
			//neuer Projekt wurde gespeichert, daher neue Auftraggeberliste vom Server holen
			leseProjektliste();
		}
		else {
			new Alert(AlertType.INFORMATION, "nicht geändert").showAndWait();
		}
	}

	/**
	 * Erstellen eines Projektes
	 */
	private void neuerProjekt() {
		ProjektFX projektFX = new ProjektFX(new Projekt());
		Optional<ButtonType> r = new ProjektDetailDialog(projektFX).showAndWait();
		if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
			leseProjektliste();
		}
	}

	/**
	 * Objekte aus der Projekt Tabelle werden in eine ArrayList gespeichert
	 */
	private void leseProjektliste() {
		olProjekt.clear();
		ServiceFunctionsReturn sfr = ServiceFunctions.get("projektlist", null);
		if(sfr.isRc()) {
			//ProjektList aus der XML Darstellung vom Server deserialisieren
			ProjektList pl = new ProjektList(sfr.getLine());
			for(Projekt einP : pl.getProjekte()) {
				olProjekt.add(new ProjektFX(einP));
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}

	}

	//Methoden zu Buttons Auftraggeber

	/**
	 * Bearbeiten und aktualisieren des ausgewählten Auftraggebers
	 * 
	 * @param auftraggeberFX Auftraggeber Objekt aus der TableVieW
	 */
	private void bearbeiteAuftraggeber(AuftraggeberFX auftraggeberFX) {
		Optional<ButtonType> r = new AuftraggeberDetailDialog(auftraggeberFX).showAndWait();
		if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
			//neuer Auftraggeber wurde gespeichert, daher neue Auftraggeberliste vom Server holen
			leseAuftraggeberliste();
		}
		else {
			new Alert(AlertType.INFORMATION, "nicht geändert").showAndWait();
		}
	}

	/**
	 * Entfernen des ausgewählten Auftraggebers
	 * Falls Auftraggeber bereits zu einem Projekt zugeordnet wurde, kann nicht gelöscht werden
	 * 
	 * @param auftraggeberFX Auftraggeber Objekt aus der TableView
	 */
	private void loescheAuftraggeber(AuftraggeberFX auftraggeberFX) {
		boolean inVerwendung = false;
		for(ProjektFX einP : olProjekt) {
			if(auftraggeberFX.getId() == einP.getAuftraggeber().getId()) {
				new Alert(AlertType.INFORMATION, auftraggeberFX.getName() + " wurde bereits zu einem Projekt zugeordnet,\n"
						+ "kann nicht gelöscht werden").showAndWait();
				inVerwendung = true;
				break;
			}
		}
		if(inVerwendung == false) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Wollen Sie den Auftraggeber " + auftraggeberFX.getName() + " löschen?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {
				ServiceFunctionsReturn sfr = ServiceFunctions.delete("auftraggeber", Long.toString(auftraggeberFX.getId()));
				if(sfr.isRc()) {
					new Alert(AlertType.INFORMATION, "gelöscht.").showAndWait();
					leseAuftraggeberliste();
				}
				else {
					new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
				}
			}
		}
	}

	/**
	 * Erstellen eines neuen Auftraggebers
	 */
	private void neuerAuftraggeber() {
		AuftraggeberFX auftraggeberFX = new AuftraggeberFX(new Auftraggeber());
		Optional<ButtonType> r = new AuftraggeberDetailDialog(auftraggeberFX).showAndWait();
		if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
			leseAuftraggeberliste();
		}
	}

	/**
	 * Objekte aus der Auftraggeber Tabelle werden in eine ArrayList gespeichert
	 */
	private void leseAuftraggeberliste() {
		olAuftraggeber.clear();
		ServiceFunctionsReturn sfr = ServiceFunctions.get("auftraggeberlist", null);
		if(sfr.isRc()) {
			//AuftraggeberList aus der XML Darstellung vom Server deserialisieren
			AuftraggeberList al = new AuftraggeberList(sfr.getLine());
			for(Auftraggeber einAg : al.getAuftraggeberElemente()) {
				olAuftraggeber.add(new AuftraggeberFX(einAg));
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}

	}

	//Methoden zu Buttons Mitarbeiter

	/**
	 * Bearbeiten und aktualisieren des ausgewählten Mitarbeiters
	 * 
	 * @param mitarbeiterFX Mitarbeiter Objekt aus der TableView
	 */
	private void bearbeiteMitarbeiter(MitarbeiterFX mitarbeiterFX) {
		Optional<ButtonType> r = new MitarbeiterDetailDialog(mitarbeiterFX).showAndWait();
		if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
			//neuer Mitarbeiter wurde gespeichert, daher neue Mitarbeiterliste vom Server holen
			leseMitarbeiterliste();
		}
		else {
			new Alert(AlertType.INFORMATION, "nicht geändert").showAndWait();
		}
	}
	/**
	 * Entfernen des ausgewählten Mitarbeiters
	 * Falls das Mitarbeiter Objekt bereits in einer Arbeitszeitzeile verwendet wird, kann nicht gelöscht werden
	 * 
	 * @param mitarbeiterFX Mitarbeiter Objekt aus der TableView
	 */
	private void loescheMitarbeiter(MitarbeiterFX mitarbeiterFX) {
		boolean inVerwendung = false;
		for(ArbeitszeitFX einAz : olArbeitszeit) {
			if(mitarbeiterFX.getId() == einAz.getMitarbeiter().getId()) {
				new Alert(AlertType.INFORMATION, "Für " + mitarbeiterFX.getName() + " wurden bereits Arbeitszeiten eingegeben,\n"
						+ "kann nicht gelöscht werden").showAndWait();
				inVerwendung = true;
				break;

			}
		}
		if(inVerwendung == false) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Wollen Sie " + mitarbeiterFX.getName() + " löschen?");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == ButtonType.OK) {

				ServiceFunctionsReturn sfr = ServiceFunctions.delete("mitarbeiter", Long.toString(mitarbeiterFX.getId()));
				if(sfr.isRc()) {
					new Alert(AlertType.INFORMATION, "gelöscht.").showAndWait();
					leseMitarbeiterliste();
				}

				else {
					new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
				}
			}
		}
	}

	/**
	 * Erstellen eines neuen Mitarbeiters
	 */
	private void neuerMitarbeiter() {
		MitarbeiterFX mitarbeiterFX = new MitarbeiterFX(new Mitarbeiter());
		Optional<ButtonType> r = new MitarbeiterDetailDialog(mitarbeiterFX).showAndWait();
		if(r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE) {
			leseMitarbeiterliste();
		}
	}

	/**
	 * Objekte aus der Mitarbeiter Tabelle werden in eine ArrayList gespeichert
	 */
	private void leseMitarbeiterliste() {
		olMitarbeiter.clear();
		ServiceFunctionsReturn sfr = ServiceFunctions.get("mitarbeiterlist", null);
		if(sfr.isRc()) {
			//MitarbeiterList aus der XML Darstellung vom Server deserialisieren
			MitarbeiterList ml = new MitarbeiterList(sfr.getLine());
			for(Mitarbeiter einM : ml.getMitarbeiterElemente()) {
				olMitarbeiter.add(new MitarbeiterFX(einM));
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
