package dto;

import java.util.List;

public class Pizza {

	private int id;
	private String name;
	private String type;
	private int prix;
	List<Ingredient> ingredients;

	public Pizza(int id, String name, String type, int prix, List<Ingredient> ingredients) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.prix = prix;
		this.ingredients = ingredients;
	}

	public Pizza() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getPrix() {
		return prix;
	}

	public void setPrix(int prix) {
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
		return "Pizza [id=" + id + ", name=" + name + ", type=" + type + ", prix=" + prix + ", ingredients="
				+ ingredients + "]";
	}

}
