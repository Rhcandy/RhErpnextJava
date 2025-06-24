package rhjava.erpnext.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDBConnection {
    /* 
    POWERSHELL ADMIN : netsh interface portproxy add v4tov4 listenport=3306 listenaddress=127.0.0.1 connectport=3306 connectaddress=172.28.206.132
    sudo service mysql restart
    GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'ton_mot_de_passe' WITH GRANT OPTION;
    FLUSH PRIVILEGES;*/

    public static void main(String[] args) {
        String url = "jdbc:mysql://172.28.206.132:3306/_118c93b1c37e0774"; // ← remplace avec ton IP WSL
        String user = "root"; 
        String password = "ton_mot_de_passe";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Connexion réussie !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur : " + e.getMessage());
        }
    }
}
