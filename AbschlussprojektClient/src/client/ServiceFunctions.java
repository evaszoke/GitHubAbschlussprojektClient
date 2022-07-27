package client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import klassen.Mitarbeiter;

public class ServiceFunctions {
	private static final String basisURI = "http://localhost:8080/";

	public static ServiceFunctionsReturn get(String item, String id) {
		ServiceFunctionsReturn sfr = new ServiceFunctionsReturn();
		// URI der Client Anfrage bauen
		String uriS = basisURI + item;
		if(id != null && id.length() > 0) {
			uriS += "/" + id;
		}
		try {
			URI uri = new URI(uriS);
			// GET Anfrage erstellen
			HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
			//Client Objekt erzeugen
			HttpClient client = HttpClient.newHttpClient();
			//Anfrage absenden und auf Response warten
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			// Statuscode aus der Antwort holen
			int statusCode = response.statusCode();
			// Body(XML String) aus der Antwort holen
			sfr.setLine(response.body());
			if(statusCode == 200) {
				sfr.setRc(true);
			}
		} catch (URISyntaxException | IOException | InterruptedException e) {
			sfr.setLine(e.toString());
		}
		return sfr;
	}

	public static ServiceFunctionsReturn post(String item, String id, String detail) {
		ServiceFunctionsReturn sfr = new ServiceFunctionsReturn();
		// URI der Client Anfrage bauen
		String uriS = basisURI + item;
		if(id != null && id.length() > 0) {
			uriS += "/" + id;
		}
		try {
			URI uri = new URI(uriS);
			// POST Anfrage erstellen
			HttpRequest request = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(detail)).build();
			//Client Objekt erzeugen
			HttpClient client = HttpClient.newHttpClient();
			//Anfrage absenden und auf Response warten
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			// Statuscode aus der Antwort holen
			int statusCode = response.statusCode();
			// Body(XML String) aus der Antwort holen
			sfr.setLine(response.body());
			if(statusCode == 201) {
				sfr.setRc(true);
			}
		} catch (URISyntaxException | IOException | InterruptedException e) {
			sfr.setLine(e.toString());
		}
		return sfr;
	}

	public static ServiceFunctionsReturn delete(String item, String id) {
		ServiceFunctionsReturn sfr = new ServiceFunctionsReturn();
		// URI der Client Anfrage bauen
		String uriS = basisURI + item;
		if(id != null && id.length() > 0) {
			uriS += "/" + id;
		}
		try {
			URI uri = new URI(uriS);
			// DELETE Anfrage erstellen
			HttpRequest request = HttpRequest.newBuilder(uri).DELETE().build();
			HttpClient client = HttpClient.newHttpClient();
			//Anfrage absenden und auf Response warten
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			// Statuscode aus der Antwort holen
			int statusCode = response.statusCode();
			// Body(XML String) aus der Antwort holen
			sfr.setLine(response.body());
			if(statusCode == 204) {
				sfr.setRc(true);
			}
		} catch (URISyntaxException | IOException | InterruptedException e) {
			sfr.setLine(e.toString());
		}
		return sfr;

	}
	
	public static ServiceFunctionsReturn put(String item, String id, String detail) {
		ServiceFunctionsReturn sfr = new ServiceFunctionsReturn();
		// URI der Client Anfrage bauen
		String uriS = basisURI + item;
		if(id != null && id.length() > 0) {
			uriS += "/" + id;
		}
		try {
			URI uri = new URI(uriS);
			// UPDATE Anfrage erstellen
			HttpRequest request = HttpRequest.newBuilder(uri).PUT(BodyPublishers.ofString(detail)).build();
			//Client Objekt erzeugen
			HttpClient client = HttpClient.newHttpClient();
			//Anfrage absenden und auf Response warten
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			// Statuscode aus der Antwort holen
			int statusCode = response.statusCode();
			// Body(XML String) aus der Antwort holen
			sfr.setLine(response.body());
			if(statusCode == 201) {
				sfr.setRc(true);
			}
		} catch (URISyntaxException | IOException | InterruptedException e) {
			sfr.setLine(e.toString());
		}
		return sfr;
	}



}
