package it.polito.tdp.seriea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Adiacenza;
import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SquadraTifosi;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

//controller turno A --> switchare al branch master_turnoB o master_turnoC per turno B o C

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private ChoiceBox<Season> boxStagione;

    @FXML
    private Button btnCalcolaConnessioniSquadra;

    @FXML
    private Button btnSimulaTifosi;

    @FXML
    private Button btnAnalizzaSquadre;

    @FXML
    private TextArea txtResult;

    @FXML
    void doAnalizzaSquadre(ActionEvent event) {
    	txtResult.clear();
    	List<Team> teams = this.model.buildGraph();
    	this.boxSquadra.getItems().setAll(teams);
    }

    @FXML
    void doCalcolaConnessioniSquadra(ActionEvent event) {
    	txtResult.clear();
    	Team team = this.boxSquadra.getValue();
    	if(team == null) {
    		txtResult.appendText("Selezionare una squadra");
    		return;
    	}
    	List<Adiacenza> connessi = this.model.getConnessioni(team);
    	txtResult.appendText("Squadre connesse a "+team+"\n\n");
    	for(Adiacenza a : connessi)
    		txtResult.appendText(a.toString()+"\n");
    }

    @FXML
    void doSimulaTifosi(ActionEvent event) {
    	txtResult.clear();
    	
    	Season season = this.boxStagione.getValue();
    	if(season == null) {
    		txtResult.appendText("Selezionare una stagione");
    		return;
    	}
    	
    	List<SquadraTifosi> result = this.model.simula(season);
    	result.sort(null);
    	for(SquadraTifosi s : result) 
    		txtResult.appendText(s.toString()+"\n");
    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert boxStagione != null : "fx:id=\"boxStagione\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnCalcolaConnessioniSquadra != null : "fx:id=\"btnCalcolaConnessioniSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSimulaTifosi != null : "fx:id=\"btnSimulaTifosi\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnAnalizzaSquadre != null : "fx:id=\"btnAnalizzaSquadre\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		
		this.boxStagione.getItems().setAll(this.model.getSeasons());
	}
}