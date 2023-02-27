package dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Commande {

	private int cid;
	private int uid;
	private String date;
	private List<Pizza> pizzas;

	public Commande(int cid, int uid, String date, List<Pizza> pizzas) {
		super();
		this.cid = cid;
		this.uid = uid;
		this.date = date;
		this.pizzas = pizzas;
	}

	public Commande(int cid, int uid, String date) {
		super();
		this.cid = cid;
		this.uid = uid;
		this.date = date;
		this.pizzas = new ArrayList<Pizza>();
	}

	public Commande() {
		super();
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Pizza> getPizzas() {
		return pizzas;
	}

	public void setPizzas(List<Pizza> pizzas) {
		this.pizzas = pizzas;
	}

	public double getPrice() {
		double price = 0.0;
		for (int i = 0; i < pizzas.size(); i++) {
			price += pizzas.get(i).getPrice();
		}
		return price;
	}

	@Override
	public String toString() {
		return "Commande [cid=" + cid + ", uid=" + uid + ", date=" + date + ", pizzas=" + pizzas + "]";
	}

}
