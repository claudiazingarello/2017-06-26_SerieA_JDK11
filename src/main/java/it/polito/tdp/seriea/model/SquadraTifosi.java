package it.polito.tdp.seriea.model;

public class SquadraTifosi implements Comparable<SquadraTifosi>{
	
	private Team team;
	private Integer tifosi;
	private Integer punti;
	
	public SquadraTifosi(Team team, Integer tifosi, Integer punti) {
		super();
		this.team = team;
		this.tifosi = tifosi;
		this.punti = punti;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Integer getTifosi() {
		return tifosi;
	}

	public void setTifosi(Integer tifosi) {
		this.tifosi = tifosi;
	}

	public Integer getPunti() {
		return punti;
	}

	public void setPunti(Integer punti) {
		this.punti = punti;
	}

	@Override
	public int compareTo(SquadraTifosi o) {
		return this.team.getTeam().compareTo(o.team.getTeam());
	}
	
	public String toString() {
		return team.getTeam() + " - " + tifosi + " tifosi - " + punti +" punti";
	}

}
