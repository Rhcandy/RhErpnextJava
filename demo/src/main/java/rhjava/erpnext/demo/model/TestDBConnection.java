package rhjava.erpnext.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;

public class TestDBConnection {
    /* 
    POWERSHELL ADMIN : netsh interface portproxy add v4tov4 listenport=3306 listenaddress=127.0.0.1 connectport=3306 connectaddress=172.28.206.132
    sudo service mysql restart
    GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'ton_mot_de_passe' WITH GRANT OPTION;
    FLUSH PRIVILEGES;*/

    @Value("${base.ip}")
    private static String url;

    @Value("${base.base}")
    private static String base;

    @Value("${base.user}")
    private static String user;

    @Value("${base.password}")
    private static String password;

    public static Connection getConnection() throws SQLException {
        String fullUrl = "jdbc:mysql://" + url + ":3306/" + base + "?useSSL=false&serverTimezone=UTC";
        return DriverManager.getConnection(fullUrl, user, password);
    }

}
