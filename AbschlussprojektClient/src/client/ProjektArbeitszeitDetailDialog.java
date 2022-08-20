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

	private ObservableList<ArbeitszeitFX> olProjektArbeitszeit = FXCollections.observableArrayList();
	private TextField gesamt = new TextField();
	Button fakturieren = new Button("Fakturieren");

	public ProjektArbeitszeitDetailDialog(ArbeitszeitFX arbeitszeitFX) {
		this.setTitle("Projektzeitaufstellung");
		olProjektArbeitszeit.clear();
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(5));
		gp.setId("dialog");
		this.getDialogPane().getStylesheets().add("Style.css");

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
		HBox hbButtons = new HBox(10, abfragen, fakturieren);

		fakturieren.setDisable(true);


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
		hb.setId("HBox");
		VBox vb = new VBox(10, gp, hbButtons, tvProjektArbeitszeit, hb);

		this.getDialogPane().setContent(vb);

		abfragen.setOnAction(e -> abfragenProjektArbeitszeit(
				cobPr.getSelectionModel().getSelectedItem().getServerProjekt().getId(), 
				dpDatumVon.getValue(), dpDatumBis.getValue()));

		fakturieren.setOnAction(e -> {


			createPdf(cobPr.getSelectionModel().getSelectedItem().getServerProjekt(), gesamt);

		});

		ButtonType beenden = new ButtonType("Beenden", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(beenden);




	}

	private void createPdf(Projekt projekt, TextField gesamt) {


		try {
			int i =1;
			Document document = new Document();
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


			document.add(logo);


			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));

			document.add(new Paragraph(projekt.getAuftraggeber().getName()));
			document.add(new Paragraph(projekt.getAuftraggeber().getAdresse()));


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


			PdfPTable table1 = new PdfPTable(3);

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

	}

	private PdfPCell getCell(String text, int alignment) {
		Font f = new Font(FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
		PdfPCell cell = new PdfPCell(new Phrase(text, f));
		cell.setPadding(0);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(Rectangle.BOX);
		cell.setPadding(5);
		return cell;
	}

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
	
	private PdfPCell getCellNoBorder(String text, int alignment) {
		Font f = new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD);
		PdfPCell cell = new PdfPCell(new Phrase(text, f));
		cell.setPadding(0);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPadding(5);
		return cell;
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
			if(!olProjektArbeitszeit.isEmpty()) {
				fakturieren.setDisable(false);
			}
		}
		else {
			//Exceptiontext aus der XML Darstellung vom Server deserialisieren
			new Alert(AlertType.ERROR, new Meldung(sfr.getLine()).toString()).showAndWait();
		}
	}


}
