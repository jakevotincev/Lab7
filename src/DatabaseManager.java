import java.sql.*;

public class DatabaseManager {

    private static Connection connection = null;
    private static PreparedStatement statement = null;
    private static String sql;

    public static void connectPSQL() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Дравер не найден");
            System.exit(1);
        }
        String URL = "jdbc:postgresql://localhost/Lab7database";
        String USER = "postgres";
        String PASSWORD = "132645124";
        /**String URL = "jdbc:postgresql://pg/studs";
        String USER = "s265076";
        String PASSWORD = "fbb556";**/
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Ошибка базы данных");
            System.exit(1);
        }
    }

    public static void addElement(Animal animal) throws SQLException {
        sql = "insert into " +
                "\"Animals\" (\"Class\", \"color\", \"height\", \"name\", \"weight\", \"width\", \"x\", \"y\", \"birthday\", \"iq\", \"owner\")\n" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, animal.getClass().toString().replace("class ", ""));
        statement.setString(2, String.valueOf(animal.getColor()));
        statement.setDouble(3, animal.getHeight());
        statement.setString(4, animal.getName());
        statement.setDouble(5, animal.getWeight());
        statement.setDouble(6, animal.getWidth());
        statement.setDouble(7, animal.getX());
        statement.setDouble(8, animal.getY());
        statement.setTimestamp(9, new Timestamp(animal.getBirthDay().getYear() - 1900, animal.getBirthDay().getMonthValue() - 1, animal.getBirthDay().getDayOfMonth(),
                animal.getBirthDay().getHour(), animal.getBirthDay().getMinute(), animal.getBirthDay().getSecond(), animal.getBirthDay().getNano()));
        switch (animal.getClass().toString()) {
            case "class Sova":
                statement.setDouble(10, ((Sova) animal).getIq());
                break;
            case "class Pyatachok":
                statement.setDouble(10, ((Pyatachok) animal).getIq());
                break;
            default:
                statement.setDouble(10, ((WinniePooh) animal).getIq());
                break;
        }
        statement.setString(11, animal.getOwner());
        statement.executeUpdate();
    }


    public static boolean checkAnimal(Animal animal) throws SQLException {
        sql = "select * from \"Animals\" where \"name\"=? and \"Class\"=?";
        boolean result = false;
        statement = connection.prepareStatement(sql);
        statement.setString(1, animal.getName());
        statement.setString(2, animal.getClass().toString().replace("class ", ""));
        ResultSet set = statement.executeQuery();
        result = set.next();

        return !result;
    }

    public static void removeElement(Animal animal) throws SQLException {
        sql = "delete from \"Animals\" where \"name\"=? and \"Class\"=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, animal.getName());
        statement.setString(2, animal.getClass().toString().replace("class ", ""));
        statement.executeUpdate();
    }

    public static boolean checkUser(String email, String login) throws SQLException {
        boolean result = false;
        sql = "select * from users where email=? or login=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, login);
        ResultSet set = statement.executeQuery();
        result = set.next();
        return result;
    }

    public static boolean checkUser(String login) throws SQLException {
        boolean result = false;
        sql = "select * from users where login=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet set = statement.executeQuery();
        result = set.next();
        return result;
    }

    public static void addUser(String email, String login, String password, String salt) throws SQLException {
        sql = "insert into users (email, login, password, salt) VALUES (?,?,?,?)";
        statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        statement.setString(2, login);
        statement.setString(3, password);
        statement.setString(4, salt);
        statement.executeUpdate();
    }

    public static String getPassword(String login) throws SQLException {
        sql = "select password from users where login=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet set = statement.executeQuery();
        set.next();
        return set.getString("password");
    }

    public static String getSalt(String login) throws SQLException {
        sql = "select salt from users where login=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, login);
        ResultSet set = statement.executeQuery();
        set.next();
        return set.getString("salt");
    }


}
