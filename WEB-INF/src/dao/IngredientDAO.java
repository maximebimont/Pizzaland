package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Ingredient;

public class IngredientDAO {

	private Connection con;

	public IngredientDAO(Connection con) {
		this.con = con;
	}

	public IngredientDAO() {
		con = new DS().getConnection();
	}

	public Ingredient find(String name) {
		Ingredient ingredient = null;
		try {
			String query = "SELECT * FROM ingredients WHERE name = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ingredient = new Ingredient(rs.getInt("iid"), name, rs.getInt("price"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ingredient;
	}

	public Ingredient find(int id) {
		Ingredient ingredient = null;
		try {
			String query = "SELECT * FROM ingredients WHERE iid = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ingredient = new Ingredient(id, rs.getString("name"), rs.getInt("price"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ingredient;
	}

	public List<Ingredient> findAll() {
		List<Ingredient> list = new ArrayList<>();
		String query = "SELECT * FROM ingredients";
		String iid, name, price;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				iid = rs.getString(1);
				name = rs.getString(2);
				price = rs.getString(3);
				list.add(new Ingredient(Integer.parseInt(iid), name, Double.parseDouble(price)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void save(Ingredient ingredient) {
		try {
			String query = "INSERT INTO ingredients VALUES( ? , ?, ?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, ingredient.getId());
			ps.setString(2, ingredient.getName());
			ps.setDouble(3, ingredient.getPrice());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(int id) {
		try {
			String query = "DELETE FROM ingredients WHERE iid = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
