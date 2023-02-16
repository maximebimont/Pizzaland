package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import dto.Commande;

public class CommandeDAO {

	private Connection con;

	public CommandeDAO(Connection con) {
		this.con = con;
	}

	public CommandeDAO() {
		con = new DS().getConnection();
	}
	
	public Commande find(int id) {
		Commande commande = null;
		return commande;
	}
	
	public List<Commande> findAll() {
		List<Commande> list = new ArrayList<Commande>();
		return list;
	}
	
	public void save(Commande commande) {
		
	}

}
