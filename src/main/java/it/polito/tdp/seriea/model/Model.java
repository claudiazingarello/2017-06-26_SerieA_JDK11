package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private Graph<Team, DefaultWeightedEdge> graph;
	private SerieADAO dao;
	
	private Map<String, Team> idMap;
	
	private Simulator sim;
	
	
	public Model() {
		this.dao = new SerieADAO();
		this.sim = new Simulator();
	}
	
	public List<Team> buildGraph() {
		this.graph = new SimpleWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.idMap = new HashMap<String, Team>();
		List<Team> teams = this.dao.listTeams(idMap);
		Graphs.addAllVertices(this.graph, teams);
		
		/*
		for(Adiacenza a : this.dao.getAdiacenze(idMap)) {
			Graphs.addEdge(this.graph, a.getTeam1(), a.getTeam2(), a.getPeso());
		}
		*/
		
		for(Team t1 : this.graph.vertexSet()) {
			for(Team t2 : this.graph.vertexSet()) {
				if(!t1.equals(t2) && this.graph.getEdge(t1, t2) == null) {
					Integer peso = this.dao.getPeso(t1, t2);
					if(peso != null && peso > 0)
						Graphs.addEdge(this.graph, t1, t2, peso);
				}
			}
		}
		
		return teams;
	}
	
	
	public List<Adiacenza> getConnessioni(Team source) {
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		for(Team vicino : Graphs.neighborListOf(this.graph, source)) {
			result.add(new Adiacenza(source, vicino, (int) this.graph.getEdgeWeight(this.graph.getEdge(source, vicino))));
		}
		
		result.sort(null);
		return result;
	}
	
	public List<Season> getSeasons() {
		return this.dao.listSeasons();
	}
	
	public List<SquadraTifosi> simula(Season season) {
		this.sim.init(season);
		return this.sim.run();
	}

}
