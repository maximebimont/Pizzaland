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
			String queryPizza = "SELECT * FROM pizzas WHERE pid = ? ";
			String queryIngredients = "SELECT * FROM confection JOIN ingredients ON confection.ings = ingredients.iid"
					+ " WHERE confection.pizza = ? ";
			PreparedStatement psPizzas = con.prepareStatement(queryPizza);
			PreparedStatement psIngredients = con.prepareStatement(queryIngredients);
			psPizzas.setInt(1, id);
			ResultSet rsPizzas = psPizzas.executeQuery();
			if (rsPizzas.next()) {
				psIngredients.setInt(1, id);
				ResultSet rsIngredients = psIngredients.executeQuery();
				while (rsIngredients.next()) {
					ingredients.add(new Ingredient(Integer.parseInt(rsIngredients.getString("iid")),
							rsIngredients.getString("name"), Double.parseDouble(rsIngredients.getString("price"))));
				}
				pizza = new Pizza(id, rsPizzas.getString("name"), rsPizzas.getString("type"),
						rsPizzas.getDouble("price"), ingredients);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pizza;
	}

	public List<Pizza> findAll() {
		ArrayList<Pizza> list = new ArrayList<Pizza>();
		String queryPizzas = "SELECT * FROM pizzas";
		String queryIngredients = "SELECT * FROM confection JOIN ingredients ON confection.ings = ingredients.iid"
				+ " WHERE confection.pizza = ? ";
		int id;
		String name, type;
		double price;
		ArrayList<Ingredient> ingredients;
		try {
			Statement st = con.createStatement();
			ResultSet rsPizzas = st.executeQuery(queryPizzas);
			PreparedStatement psIngredients = con.prepareStatement(queryIngredients);
			while (rsPizzas.next()) {
				id = rsPizzas.getInt(1);
				name = rsPizzas.getString(2);
				type = rsPizzas.getString(3);
				price = rsPizzas.getDouble(4);
				ingredients = new ArrayList<Ingredient>();
				psIngredients.setInt(1, id);
				ResultSet rsIngredients = psIngredients.executeQuery();
				while (rsIngredients.next()) {
					ingredients.add(new Ingredient(rsIngredients.getInt("iid"), rsIngredients.getString("name"),
							rsIngredients.getDouble("price")));
				}
				list.add(new Pizza(id, name, type, price, ingredients));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void post(Pizza pizza) {
		try {
			String queryPizzas = "INSERT INTO pizzas VALUES( ? , ? , ? , ? )";
			String queryIngredients = "INSERT INTO confection VALUES( ? , ? )";
			PreparedStatement psPizzas = con.prepareStatement(queryPizzas);
			PreparedStatement psIngredients = con.prepareStatement(queryIngredients);
			psPizzas.setInt(1, pizza.getId());
			psPizzas.setString(2, pizza.getName());
			psPizzas.setString(3, pizza.getType());
			psPizzas.setDouble(4, pizza.getPrice());
			psPizzas.executeUpdate();
			for (Ingredient i : pizza.getIngredients()) {
				psIngredients.setInt(1, pizza.getId());
				psIngredients.setInt(2, i.getId());
				psIngredients.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try {
			String queryPizzas = "DELETE FROM pizzas WHERE pid = ? ";
			String queryIngredients = "DELETE FROM confection WHERE pizza = ? ";
			PreparedStatement psPizzas = con.prepareStatement(queryPizzas);
			PreparedStatement psIngredients = con.prepareStatement(queryIngredients);
			psPizzas.setInt(1, id);
			psIngredients.setInt(1, id);
			psIngredients.executeUpdate();
			psPizzas.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean hasIng(int pid, int iid) {
		try {
			String query = "SELECT FROM confection WHERE pizza = ? AND ings = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, pid);
			ps.setInt(2, iid);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void addIng(int pid, int iid) {
		try {
			String query = "INSERT INTO confection VALUES ( ? , ? )";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, pid);
			ps.setInt(2, iid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeIng(int pid, int iid) {
		try {
			String query = "DELETE FROM confection WHERE pizza = ? AND ings = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, pid);
			ps.setInt(2, iid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatePrice(int id, double price) {
		try {
			if (price == -1)
				return;
			String query = "UPDATE pizzas SET price = ? WHERE pid = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setDouble(1, price);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
