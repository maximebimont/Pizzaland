package dto;

import java.util.List;

public class Pizza {

	private int pid;
	private String name;
	private String type;
	private double prix;
	List<Ingredient> ingredients;

	public Pizza(int id, String name, String type, double prix, List<Ingredient> ingredients) {
		super();
		this.pid = id;
		this.name = name;
		this.type = type;
		this.prix = prix;
		this.ingredients = ingredients;
	}

	public Pizza() {
		super();
	}

	public int getId() {
		return pid;
	}

	public void setId(int id) {
		this.pid = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	@Override
	public String toString() {
		return "Pizza [id=" + pid + ", name=" + name + ", type=" + type + ", prix=" + prix + ", ingredients="
				+ ingredients + "]";
	}

}
