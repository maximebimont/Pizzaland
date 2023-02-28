package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.User;

public class UserDAO {
    private DS ds;

    public UserDAO() {
        this.ds = new DS();
    }

    public User find(String login) {
        String query = "SELECT * FROM utilisateur WHERE login = ?";

        try (Connection con = this.ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, login);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();

                user.setUid(rs.getInt("uid"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("pwd"));
                
                return user;
            }

        } catch (Exception e) {

        }
        return null;
    }

    public List<User> findAll() {
        String query = "SELECT * FROM users";
        List<User> students = new ArrayList<>();

        try (Connection con = this.ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();

                user.setUid(rs.getInt("uid"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("pwd"));

                students.add(user);
            }

            return students;

        } catch (Exception e) {

        }
        return new ArrayList<>();
    }
}
