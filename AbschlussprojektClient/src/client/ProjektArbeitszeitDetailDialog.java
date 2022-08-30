package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Font.FontStyle;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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
import klassen.Projekt;
import klassen.ProjektList;

public class ProjektArbeitszeitDetailDialog extends Dialog<ButtonType>{

	//Instanzvariable
	private ObservableList<ArbeitszeitFX> olProjektArbeitszeit = FXCollections.observableArrayList();
	private TextField gesamtZeit = new TextField();
	private TextField gesamtPreis = new TextField();
	private ComboBox cbfakt = new ComboBox<>();
	Button fakturieren = new Button("Fakturieren");
	private static final String ALLE = "alle";
	private static final String FAKTURIERT = "fakturiert";
	private static final String NICHT_FAKTURIERT = "nicht fakturiert";

	public ProjektArbeitszeitDetailDialog(ArbeitszeitFX arbeitszeitFX) {
		//GUI Elemente des Dialog Fensters
		this.setTitle("Projektzeitaufstellung");
		olProjektArbeitszeit.clear();
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(5));
		gp.setId("dialog");
		this.getDialogPane().getStylesheets().add("Style.css");

		gp.add(new Label("Projekt"), 0, 0);

		//Liste für ComboBox Projekt
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
		
		gp.add(new Label("Auswahl"), 0, 3);
		
		ObservableList<String> olfakt = FXCollections.observableArrayList(ALLE, FAKTURIERT, NICHT_FAKTURIERT);
		cbfakt.setItems(olfakt);
		cbfakt.getSelectionModel().select(0);
		cbfakt.setPrefWidth(200);
		gp.add(cbfakt, 1, 3);

		Button abfragen = new Button("Abfragen");
		HBox hbButtons = new HBox(10, abfragen, fakturieren);

		fakturieren.setDisable(true);

		//TableView Arbeitszeit
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

		HBox hbZeit = new HBox(10, new Label("Projektzeit gesamt: "), gesamtZeit);
		HBox hbPreis = new HBox(10, new Label("Projektpreis gesamt: "), gesamtPreis);
		hbZeit.setId("HBox");
		hbPreis.setId("HBox");
		VBox vb = new VBox(10, gp, hbButtons, tvProjektArbeitszeit, hbZeit, hbPreis);
		vb.setPrefSize(600, 600);

		this.getDialogPane().setContent(vb);

		//Event Handler für Buttons
		abfragen.setOnAction(e -> abfragenProjektArbeitszeit(
				cobPr.getSelectionModel().getSelectedItem().getServerProjekt().getId(), 
				dpDatumVon.getValue(), dpDatumBis.getValue(), cbfakt.getSelectionModel().getSelectedItem().toString()));

		fakturieren.setOnAction(e -> {
			createPdf(cobPr.getSelectionModel().getSelectedItem().getServerProjekt(), gesamtPreis);
			for(ArbeitszeitFX einAz : olProjektArbeitszeit) {
				einAz.getServerArbeitszeit().setFakturiert(true);
				ServiceFunctionsReturn sfr = ServiceFunctions.put("arbeitszeit", Long.toString(einAz.getZeilennummer()), einAz.getServerArbeitszeit().toXML());
			}
			

			
		});

		ButtonType beenden = new ButtonType("Beenden", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(beenden);




	}


	//PDF Datei erzeugen
	/**
	 * 
	 * @param projekt: Projekt Objekt aus dem ComboBox
	 * @param gesamt: Ergebnis aus dem TextField
	 */
	private void createPdf(Projekt projekt, TextField gesamt) {


		try {
			//Dokument erstellen
			int i =1;
			Document document = new Document();
			//File erstellen, um die fortlaufende Nummerierung der Rechnungen zu ermöglichen
			String nummer = Integer.toString(i);
			File f = new File(nummer);
			while(f.exists()) {
				i++;
				nummer = Integer.toString(i);
				f = new File(nummer);
			}
			f.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(new File("C:\\Evi\\JavaWifi\\Abschlussprojekt\\Rechnungen\\Rechnung" + 
					nummer + ".pdf"));


			PdfWriter.getInstance(document, outputStream);
			
			Image logo = Image.getInstance("C:\\Evi\\JavaWifi\\Abschlussprojekt\\logo.jpg");
			logo.scaleAbsolute(180, 100);
			logo.setAbsolutePosition(370, 720);

			document.open();

			//Logo zum Dokument hinzufügen
			document.add(logo);
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			
			//Auftraggeber Daten zu Dokument hinzufügen
			document.add(new Paragraph(projekt.getAuftraggeber().getName()));
			document.add(new Paragraph(projekt.getAuftraggeber().getAdresse()));

			//Rechnungsdatum, Rechnungsnummer und Daten des Rechnungsausstellers hinzufügen
			Paragraph datum = new Paragraph("Datum: " + LocalDate.now());
			datum.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
			document.add(datum);
			Paragraph rechnungNr = new Paragraph("Rechnungsnr.: " + nummer + "/" + LocalDate.now().getYear());
			rechnungNr.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
			document.add(rechnungNr);
			Paragraph bank = new Paragraph("Bank: Raiffeinsen NOE-Wien");
			bank.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
			document.add(bank);
			Paragraph bic = new Paragraph("BIC: XXXXXXXX");
			bic.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
			document.add(bic);
			Paragraph iban = new Paragraph("IBAN: AT00 0000 0000 0000 0000");
			iban.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
			document.add(iban);
			
			Paragraph rechnung = new Paragraph("Rechnung Nr.: " + nummer + "/" + LocalDate.now().getYear(), 
					new Font(FontFamily.TIMES_ROMAN, 22, Font.BOLD));
			rechnung.setIndentationLeft(50);


			document.add(rechnung);

			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));

			//Tabelle im pdf Dokument 
			PdfPTable table1 = new PdfPTable(3);

			//Tabelle über die fakturierten Projektes inkl. gesamtbetrag hinzufügen
			table1.setWidthPercentage(100);
			table1.addCell(getCellHeadline("BEZEICHNUNG", PdfPCell.ALIGN_LEFT));
			table1.addCell(getCellHeadline(" BETRAG", PdfPCell.ALIGN_LEFT));
			table1.addCell(getCellHeadline(" GESAMT", PdfPCell.ALIGN_LEFT));
			document.add(table1);

			PdfPTable table2 = new PdfPTable(3);

			table2.setWidthPercentage(100);
			table2.addCell(getCell("Projekt " + projekt.getName(), PdfPCell.ALIGN_LEFT));
			table2.addCell(getCell("€ " + gesamt.getText(), PdfPCell.ALIGN_LEFT));
			table2.addCell(getCell("€ " + gesamt.getText(), PdfPCell.ALIGN_LEFT));
			document.add(table2);

			PdfPTable table3 = new PdfPTable(3);

			table3.setWidthPercentage(100);
			table3.addCell(getCellNoBorder("Rechnungsbetrag Netto", PdfPCell.ALIGN_LEFT));
			table3.addCell(getCellNoBorder("", PdfPCell.ALIGN_LEFT));
			table3.addCell(getCellNoBorder("€ " + gesamt.getText(), PdfPCell.ALIGN_LEFT));
			document.add(table3);
			
			//Abschliessende Informationen hinzufügen
			Paragraph zahlung = new Paragraph("Zahlungsbedingungen: ", 
					new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD|Font.UNDERLINE));

			document.add(zahlung);
			document.add(new Paragraph(" "));
			document.add(new Paragraph("Bis14 Tage 3 % Skonto, Rein netto innerhalb 30 Tagen nach Erhalt der Rechnung spätestens bis zum " + 
					LocalDate.now().plusDays(30) + "."));
			document.add(new Paragraph(" "));
			document.add(new Paragraph("Ich möchte Sie bitten, den offenen Betrag an obenstehende Bankverbindung zu überweisen.\r\n"
					+ "Bei Überweisung mittels Internet-Banking tragen Sie bitte im Feld \"Verwendungszweck\" die Rechnungsnummer ein.\r"));
			document.add(new Paragraph("Einwendungen gegen in Rechnung gestellte Dienstleistungen sind innerhalb von 7 Tagen nach Erhalt der Rechnung "
					+ "schriftlich zu erheben, andernfalls gilt die Forderung als anerkannt.\r\n"
					+ "Alle gelieferten Waren bleiben bis zur vollständigen Bezahlung unser Eigentum."));
			document.add(new Paragraph(" "));
			document.add(new Paragraph("Nach § 19 Absatz 1a des Umsatzsteuergesetzes geht die Steuerschuld auf den Leistungsempfänger über."));



			document.close();
			outputStream.close();

		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new Alert(AlertType.INFORMATION, "Rechnung erstellt").showAndWait();
		
		

	}
	
	//Methoden zur Tabellen Erstellung im pdf Dokument
	//Mittlere Zeile in der Tabelle
	private PdfPCell getCell(String text, int alignment) {
		Font f = new Font(FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
		PdfPCell cell = new PdfPCell(new Phrase(text, f));
		cell.setPadding(0);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(Rectangle.BOX);
		cell.setPadding(5);
		return cell;
	}

	//Oberste Zeile in der Tabelle
	private PdfPCell getCellHeadline(String text, int alignment) {
		Font f = new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD);
		PdfPCell cell = new PdfPCell(new Phrase(text, f));
		cell.setPadding(0);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(Rectangle.BOX);
		cell.setBorderWidth(3);
		cell.setPadding(5);
		return cell;
	}

	//Untere Zeile in der Tabelle 
	private PdfPCell getCellNoBorder(String text, int alignment) {
		Font f = new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD);
		PdfPCell cell = new PdfPCell(new Phrase(text, f));
		cell.setPadding(0);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPadding(5);
		return cell;
	}

	//Methode zu Abfragen Button
	/**
	 * Abfragen einer Liste über die Arbeitszeitzeilen eines ausgewählten Projektes
	 * innerhalb des ausgewählten Zeitraums
	 * Berechnung des Projektzeites
	 * Berechnung des Projektpreises
	 * 
	 * @param id: ID des ausgewählten Projekt Objektes aus ComboBox
	 * @param value: Anfangsdatum der Abfrage aus DatePicker
	 * @param value2: Enddatum der Abfrage aus DatePicker
	 * @param selected: Ergebnis aus dem ComboBox
	 */
	private void abfragenProjektArbeitszeit(int id, LocalDate value, LocalDate value2, String selected) {

		olProjektArbeitszeit.clear();
		gesamtPreis.setText("");
		
		if(selected.equals(NICHT_FAKTURIERT)) {
			selected = "nichtFakturiert";
		}
		String paths = Integer.toString(id) + "/" + value.toString() + "/" + value2.toString() + "/" + selected;

		ServiceFunctionsReturn sfr = ServiceFunctions.get("projektarbeitszeitlist", paths);
		if(sfr.isRc()) {
			//ArbeitszeitList aus der XML Darstellung vom Server deserialisieren
			ArbeitszeitList al = new ArbeitszeitList(sfr.getLine());
			for(Arbeitszeit einAz : al.getArbeitszeiten()) {
				olProjektArbeitszeit.add(new ArbeitszeitFX(einAz));
			}
			double gesamtProjektZeit = 0;
			for(ArbeitszeitFX einAZ : olProjektArbeitszeit) {
				gesamtProjektZeit += einAZ.getStundengesamt();
			}
			double gesamtProjektPreis = 0;
			for(ArbeitszeitFX einAZ : olProjektArbeitszeit) {
				gesamtProjektPreis += einAZ.getStundengesamt() * einAZ.getStundensatz();
			}
			gesamtZeit.setText(Double.toString(gesamtProjektZeit));
			gesamtPreis.setText(Double.toString(gesamtProjektPreis));
			if(!olProjektArbeitszeit.isEmpty() && cbfakt.getSelectionModel().getSelectedItem().equals(NICHT_FAKTURIERT)) {
				fakturieren.setDisable(false);
			}
			else {
				fakturieren.setDisable(true);	
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}
	}


}
