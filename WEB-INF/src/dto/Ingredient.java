package dto;

public class Ingredient {

	private int iid;
	private String name;
	private double price;

	public Ingredient(int id, String name, double price) {
		super();
		this.iid = id;
		this.name = name;
		this.price = price;
	}
	
	public Ingredient() {
		super();
	}

	public int getId() {
		return iid;
	}

	public void setId(int id) {
		this.iid = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "id=" + iid + ", name=" + name + ", price=" + price;
	}

}
