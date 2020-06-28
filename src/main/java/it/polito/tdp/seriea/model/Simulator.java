package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.seriea.db.SerieADAO;

public class Simulator {
	
	//Coda eventi
	private PriorityQueue<Match> queue;
	
	//Parametri di simulazione
	private SerieADAO dao;
	private Season season;
	
	private final Integer TIFOSI_INIZIALI = 1000;
	private final Integer P = 10;
	
	//Modello del mondo
	private Map<String, Team> idMap;
	
	//Parametri da calcolare
	private Map<String, SquadraTifosi> tifosi;
	
	
	public void init(Season season) {
		this.dao = new SerieADAO();
		this.season = season;
		
		this.tifosi = new HashMap<>();
		this.idMap = new HashMap<>();
		for(Team t : this.dao.listTeams(this.season, idMap)) {
			tifosi.put(t.getTeam(), new SquadraTifosi(t, this.TIFOSI_INIZIALI, 0));
		}
		
		this.queue = new PriorityQueue<Match>(this.dao.getMatches(this.season, idMap));
		
	}
	
	public List<SquadraTifosi> run() {
		while(!this.queue.isEmpty()) {
			Match m = this.queue.poll();
			this.processEvent(m);
		}
		
		return new ArrayList<>(this.tifosi.values());
	}

	private void processEvent(Match m) {
		SquadraTifosi tifosiH = this.tifosi.get(m.getHomeTeam().getTeam());
		SquadraTifosi tifosiA = this.tifosi.get(m.getAwayTeam().getTeam());
		
		if(tifosiH.getTifosi() > tifosiA.getTifosi()) {
			Random r = new Random();
			if(r.nextDouble() < (1-tifosiA.getTifosi()/tifosiH.getTifosi()))
				if(m.getFtag() > 0)
					m.setFtag(m.getFtag()-1);
		} else if(tifosiH.getTifosi() < tifosiA.getTifosi()) {
			Random r = new Random();
			if(r.nextDouble() < (1-tifosiH.getTifosi()/tifosiA.getTifosi()))
				if(m.getFthg() > 0)
					m.setFthg(m.getFthg()-1);
		}
		
		
		if(m.getFthg() > m.getFtag()) {
			Integer perc = (m.getFthg() - m.getFtag())*this.P;
			Integer sposta = (tifosiA.getTifosi()/100)*perc;
			tifosiA.setTifosi(tifosiA.getTifosi()-sposta);
			tifosiH.setTifosi(tifosiH.getTifosi()+sposta);
			tifosiH.setPunti(tifosiH.getPunti()+3);
		} else if(m.getFthg() < m.getFtag()){
			Integer perc = (m.getFtag() - m.getFthg())*this.P;
			Integer sposta = (tifosiH.getTifosi()/100)*perc;
			tifosiA.setTifosi(tifosiA.getTifosi()+sposta);
			tifosiH.setTifosi(tifosiH.getTifosi()-sposta);
			tifosiA.setPunti(tifosiA.getPunti()+3);
		} else {
			tifosiH.setPunti(tifosiH.getPunti()+1);
			tifosiA.setPunti(tifosiA.getPunti()+1);
		}
		
	}

}
