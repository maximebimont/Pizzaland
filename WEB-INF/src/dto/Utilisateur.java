package dto;

public class Utilisateur {
	public int uid;
	public String login;
	public String password;
	public String role;
	public String token;

	public Utilisateur() {

	}

	public int getUid() {
		return uid;
	}

	public Utilisateur setUid(int uid) {
		this.uid = uid;
		return this;
	}

	public Utilisateur setLogin(String login) {
		this.login = login;
		return this;
	}

	public Utilisateur setPassword(String password) {
		this.password = password;
		return this;
	}

	public Utilisateur setRole(String role) {
		this.role = role;
		return this;

	}

	public Utilisateur setToken(String token) {
		this.token = token;
		return this;
	}

	public String getLogin() {
		return this.login;
	}

	public String getPassword() {
		return this.password;
	}

	public String getRole() {
		return this.role;
	}

	public String getToken() {
		return this.token;
	}

	public boolean hasPassword(String password) {
		return this.password.equals(password);
	}
}
