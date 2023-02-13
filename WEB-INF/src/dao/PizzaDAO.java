package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;
import dto.Pizza;

public class PizzaDAO {

	private Connection con;

	public PizzaDAO(Connection con) {
		this.con = con;
	}

	public PizzaDAO() {
		con = new DS().getConnection();
	}

	public Pizza findByName(String name) {
		Pizza pizza = null;
		try {
			String query = "SELECT * FROM pizzas WHERE name = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				pizza = new Pizza(rs.getInt("id"), name, rs.getString("type"), rs.getInt("prix"),
//						rs.getString("ingredient"));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pizza;
	}

	public Pizza findById(int id) {
		Pizza pizza = null;
		try {
			String query = "SELECT * FROM pizzas WHERE id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				pizza = new Pizza(id, rs.getString("name"), rs.getString("type"), rs.getInt("prix"), rs.getString("ingredient");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pizza;
	}

	public List<Pizza> findAll() {
		List<Pizza> list = new ArrayList<>();
		String query = "SELECT * FROM pizzas";
		String id, name, type, prix;
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
//			while (rs.next()) {
//				id = rs.getString(1);
//				name = rs.getString(2);
//				type = rs.getString(3);
//				prix = rs.getString(4);
//				ingredients = rs.getString(5);
//				list.add(new Pizza(Integer.parseInt(id), name , type, Integer.parseInt(prix),type,ingredients));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void save(Pizza Pizza) {
//		try {
//			String query = "INSERT INTO ingredients VALUES( ? , ?)";
//			PreparedStatement ps = con.prepareStatement(query);
//			ps.setInt(1, ingredient.getId());
//			ps.setString(2, ingredient.getName());
//			ps.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void remove(int id) {
		try {
			String query = "DELETE FROM pizzas WHERE id = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
