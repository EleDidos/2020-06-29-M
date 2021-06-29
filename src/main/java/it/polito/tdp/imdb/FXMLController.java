/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	Integer n=0;
    	
    	try {
    		n=boxAnno.getValue();
    		if(n==null) {
    			txtResult.setText("Inserisci un numero intero di ...");
    			return;
    		}
    	}catch(NumberFormatException nfe) {
    		txtResult.setText("Inserisci un numero intero di ...");
    		return;
    	}catch(NullPointerException npe) {
    		txtResult.setText("Inserisci un numero intero di ...");
    		return;
    	}
    	
    	model.creaGrafo(n);
    	txtResult.appendText("Caratteristiche del grafo:\n#VERTICI = "+model.getNVertici()+"\n#ARCHI = "+model.getNArchi());
    	
    	boxRegista.getItems().addAll(model.getVertici());

    }
    
    Director scelto;

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	if(model.getGraph()==null) {
    		txtResult.setText("Devi prima creare il grafo!");
    		return;
    	}
    	
    	
    	try {
    		scelto=boxRegista.getValue();
    		
    		if(scelto==null) {
    			txtResult.setText("Scegli un regista");
    			return;
    		}
    	}catch(NullPointerException npe) {
    		txtResult.setText("Scegli un regista");
    		return;
    	}
    	txtResult.appendText("\n\nI registi adiacenti a quello scelto sono:\n");
    	for(Vicino v: model.listVicini(scelto)) {
    		txtResult.appendText(v+"\n");
    	}

    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	if(model.getGraph()==null) {
    		txtResult.setText("Devi prima creare il grafo!");
    		return;
    	}
    	
    	Integer c=0;
    	
    	try {
    		c=Integer.parseInt(txtAttoriCondivisi.getText());
    		
    	}catch(NumberFormatException nfe) {
    		txtResult.setText("Inserisci un numero intero corrispondente al n° max di attori condivisi");
    		return;
    	}catch(NullPointerException npe) {
    		txtResult.setText("Inserisci un numero intero corrispondente al n° max di attori condivisi");
    		return;
    	}
    	
    	List <Director> best = model.trovaPercorso(scelto,c);
    	txtResult.appendText("\n\nIl percorso con lunghezza massima ha peso = "+model.getPesoMAX()+" e coinvolge i seguenti registi:\n");
    	for(Director d:best) {
    		txtResult.appendText(d+"\n");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	Integer [] anni = {2004,2005,2006};
    	boxAnno.getItems().addAll(anni);
    	
    }
    
}
