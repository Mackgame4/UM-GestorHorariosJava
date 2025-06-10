package Data;

public class DAOConfig {
    static final String USERNAME = "root";
    static final String PASSWORD = "root";
    private static final String DATABASE = "gh";
    private static final String DRIVER = "jdbc:mysql"; // options: ""jdbc:mariadb", jdbc:postgresql", "jdbc:sqlite"
    private static final String PARAMS = "?useSSL=false&serverTimezone=UTC";
    static final String URL = DRIVER+"://localhost:3306/"+DATABASE+PARAMS;
}
