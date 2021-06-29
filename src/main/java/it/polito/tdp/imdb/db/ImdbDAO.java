package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors WHERE ";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//GET VERTICI
public void loadAllVertici(Map <Integer, Director> idMap, Integer anno){
		
	
		String sql = "SELECT d.id, d.first_name, d.last_name "
				+ "FROM directors as d, movies_directors as md, movies as m "
				+ "WHERE d.id=md.director_id and md.movie_id=m.id and m.year=? "
				+ "GROUP BY d.id, d.first_name, d.last_name";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				if(!idMap.containsKey(res.getInt("d.id"))) {
					Director d = new Director(res.getInt("d.id"),res.getString("d.first_name"),res.getString("d.last_name"));
					idMap.put(res.getInt("d.id"), d);
					System.out.println();
				}//if
				
			}//while
			
			conn.close();
			return  ;

		} catch (SQLException e) {
			e.printStackTrace();
			return  ;
		}

	}

//archi
public List <Arco> listArchi(Map <Integer,  Director > idMap, Integer anno){
	String sql = "SELECT md1.director_id as id1, md2.director_id as id2, COUNT(DISTINCT r1.actor_id) as peso "
			+ "FROM movies_directors as md1, movies_directors as md2, movies as m1, movies as m2, roles as r1, roles as r2 "
			+ "WHERE md1.movie_id=m1.id and m1.id=r1.movie_id and m1.year=? and md2.movie_id=m2.id and m2.id=r2.movie_id and m2.year=? and r1.actor_id=r2.actor_id and md1.director_id>md2.director_id "
			+ "GROUP BY md1.director_id, md2.director_id";

	Connection conn = DBConnect.getConnection();
	List <Arco> archi = new ArrayList <Arco>();

	try {
		PreparedStatement st = conn.prepareStatement(sql);
		st.setInt(1, anno);
		st.setInt(2, anno);
		ResultSet res = st.executeQuery();
		while (res.next()) {

			Director d1 = idMap.get(res.getInt("id1"));
			Director d2 = idMap.get(res.getInt("id2"));
			
			if(d1!=null & d2!=null & res.getInt("peso")>0) {
				Arco a = new Arco(d1,d2,res.getInt("peso"));
				archi.add(a);
				System.out.println(a);
			}
		

		}
		conn.close();
		return archi;
		
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}
}


	
	
	
	
	
	
}
