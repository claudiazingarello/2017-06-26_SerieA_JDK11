package it.polito.tdp.seriea.model;

public class Adiacenza implements Comparable<Adiacenza> {
	
	private Team team1;
	private Team team2;
	private Integer peso;
	
	public Adiacenza(Team team1, Team team2, Integer peso) {
		super();
		this.team1 = team1;
		this.team2 = team2;
		this.peso = peso;
	}

	public Team getTeam1() {
		return team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public Integer getPeso() {
		return peso;
	}

	@Override
	public int compareTo(Adiacenza o) {
		return -this.peso.compareTo(o.peso);
	}
	
	public String toString() {
		return team2 + " - " + peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((team1 == null) ? 0 : team1.hashCode());
		result = prime * result + ((team2 == null) ? 0 : team2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiacenza other = (Adiacenza) obj;
		if (team1 == null) {
			if (other.team1 != null)
				return false;
		} else if (!team1.equals(other.team1))
			return false;
		if (team2 == null) {
			if (other.team2 != null)
				return false;
		} else if (!team2.equals(other.team2))
			return false;
		return true;
	}
	
	

}
