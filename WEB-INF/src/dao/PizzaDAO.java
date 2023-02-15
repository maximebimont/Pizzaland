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

	public Pizza find(int id) {
		Pizza pizza = null;
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		try {
			String queryPizza = "SELECT * FROM pizzasvide WHERE pid = ? ";
			String queryIngredients = "SELECT * FROM pizzas JOIN ingredients ON pizzas.idingredient = ingredients.iid"
					+ " WHERE pizzas.idpizza = ? ";
			PreparedStatement ps = con.prepareStatement(queryPizza);
			PreparedStatement psIngredients = con.prepareStatement(queryIngredients);
			ps.setInt(1,id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				psIngredients.setInt(1,id);
				ResultSet rsIngredients = psIngredients.executeQuery();
				while (rsIngredients.next()) {
					ingredients.add(new Ingredient(Integer.parseInt(rsIngredients.getString("iid")),
							rsIngredients.getString("name"),Double.parseDouble(rsIngredients.getString("price"))));
				}
				pizza = new Pizza(id,rs.getString("name"),rs.getString("type"),rs.getDouble("prix"),ingredients);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return pizza;
	}

	public List<Pizza> findAll() {
		ArrayList<Pizza> list = new ArrayList<Pizza>();
		String query = "SELECT * FROM pizzasvide";
		String queryIngredients = "SELECT * FROM pizzas JOIN ingredients ON pizzas.idingredient = ingredients.iid"
				+ " WHERE pizzas.idpizza = ? ";
		int id;
		String name,type;
		double prix;
		ArrayList<Ingredient> ingredients;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			PreparedStatement psIngredients = con.prepareStatement(queryIngredients);
			while(rs.next()) {
				id = rs.getInt(1);
				name = rs.getString(2);
				type = rs.getString(3);
				prix = rs.getDouble(4);
				ingredients =  new ArrayList<Ingredient>();
				psIngredients.setInt(1,id);
				ResultSet rsIngredients = psIngredients.executeQuery();
				while (rsIngredients.next()) {
					ingredients.add(new Ingredient(rsIngredients.getInt("iid"),
							rsIngredients.getString("name"),rsIngredients.getDouble("price")));
				}
				list.add(new Pizza(id, name, type, prix, ingredients));
			}
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
