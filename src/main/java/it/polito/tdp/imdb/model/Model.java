package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	
	private SimpleWeightedGraph< Director , DefaultWeightedEdge>graph;
	private Map <Integer, Director  > idMap;
	private ImdbDAO dao;
	private List <Director> best;
	private int c;
	private int LMax; //peso Max di best <= c
	
	public Model() {
		idMap= new HashMap <Integer, Director >();
		dao=new ImdbDAO();
	}
	
	public void creaGrafo(Integer anno) {
		graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		dao.loadAllVertici(idMap,anno );
		Graphs.addAllVertices(graph, idMap.values());
		
		for(Arco a : dao.listArchi(idMap,anno))
			Graphs.addEdge(graph,a.getD1(),a.getD2(),a.getPeso());
		
		
	}
	
	public Integer getNVertici() {
		return graph.vertexSet().size();
	}
	
	public Integer getNArchi() {
		return graph.edgeSet().size();
	}
	
	public List <Director> getVertici(){
		List <Director> vertici = new ArrayList <Director>();
		for(Director d     : graph.vertexSet())
			vertici.add(d  );
		Collections.sort(vertici);
		return vertici;
	}

	public SimpleWeightedGraph< Director , DefaultWeightedEdge> getGraph() {
		return graph;
	}
	
	public List <Vicino> listVicini(Director scelto){
		List <Vicino> vicini = new ArrayList <Vicino>();
		for(Director d: Graphs.neighborListOf(graph, scelto))
			vicini.add(new Vicino ( d, graph.getEdgeWeight(graph.getEdge(d, scelto) ) ));
		Collections.sort(vicini);
		return vicini;
		
	}
	
	
	
	public List <Director> trovaPercorso(Director partenza, Integer c){
		this.c=c;
		best=new ArrayList <Director>();
		List <Director> parziale = new ArrayList <Director>();
		LMax=0;
		
		parziale.add(partenza);
		cerca(parziale);
		
		return best;
	}
	
	private void cerca(List <Director> parziale) {
		
			// se ho superato c
			//tolgo ultimo elemento che me l'ha fatto superare
			if(calcolaPeso(parziale)>c) {
				parziale.remove(parziale.size()-1);
			
				if(best.size()==0 || parziale.size()>LMax) {
					best=new ArrayList <Director> (parziale);
					LMax=parziale.size();
				}
				return; 
			}
			//sia che ci sia un nuovo best, sia in caso contrario
			//faccio backtracking e torno al "remove" del LIVELLO PRECEDENTE di "cerca"
		
		
		for(Director p: Graphs.neighborListOf(graph, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(p)) {
				parziale.add(p);
				//prima di andare oltre, mi tengo parziale, perché potrebbe 
				//essere una best
				if(best.size()==0 || parziale.size()>LMax) {
					best=new ArrayList <Director> (parziale);
					LMax=parziale.size();
				}
				//poi vado avanti con "cerca"
				cerca(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	
	
	
	private Integer calcolaPeso(List <Director> lista) {
		Integer peso=0;
		
		//se ci sono almeno 2 elementi
		if(lista.size()>=2) {
			for(int i=0;i<lista.size()-1;i++) {
				Director d1=lista.get(i);
				Director d2=lista.get(i+1);
				DefaultWeightedEdge e=graph.getEdge(d1, d2);
				peso+=(int)graph.getEdgeWeight(e);
			}
		}
		return peso;
	}

	
	public Integer getPesoMAX() {
		
		return calcolaPeso(best);
	}
	
}
