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

	public Ingredient findByName(String name) {
		Ingredient ingredient = null;
		try {
			String query = "SELECT * FROM ingredients WHERE name = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ingredient = new Ingredient(rs.getInt("id"), name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ingredient;
	}
	
	public Ingredient findById(int id) {
		Ingredient ingredient = null;
		try {
			String query = "SELECT * FROM ingredients WHERE id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ingredient = new Ingredient(id, rs.getString("name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ingredient;
	}

	public List<Ingredient> findAll() {
		List<Ingredient> list = new ArrayList<>();
		String query = "SELECT * FROM ingredients";
		String id, name;
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				id = rs.getString(1);
				name = rs.getString(2);
				list.add(new Ingredient(Integer.parseInt(id), name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void save(Ingredient ingredient) {
		try {
			String query = "INSERT INTO ingredients VALUES( ? , ?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, ingredient.getId());
			ps.setString(2, ingredient.getName());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void remove(int id) {
		try {
			String query="DELETE FROM ingredients WHERE id = ? ";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1,id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
