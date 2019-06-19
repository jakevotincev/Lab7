
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;

public class Authorization {
    private static String token = "";

    public static HashSet<String> users = new HashSet<>();

    public static synchronized void register(Command command, Sender sender) {
        String message = "";
        try {
            if (DatabaseManager.checkUser(command.getEmail(), command.getLogin())) {
                message = "Пользователь с таким логином или с такой почтой уже зарегистрирован";
                Response response = new Response(message);
                response.setStatus(Status.NOTLOGINED);
                sender.send(response);
            } else {
                Response response = new Response();
                if (command.getStatus() == Status.NOSTATUS) {
                    token = getToken();
                    message = EmailSender.sendEmail(command.getEmail(), token);
                    response.setResponse(message);
                    if (message.equals("Не удалось отправить сообщение на вашу почту, возможно вы ввели ее неверно"))
                        response.setStatus(Status.NOTLOGINED);
                    else {
                        response.setStatus(Status.WAITPASSWORD);
                    }
                    sender.send(response);
                } else {
                    if (token.equals(command.getToken())) {
                        message = "Вы успешно зарегистрированы";
                        String salt = Cryptographer.getSalt();
                        String hashPassword = Cryptographer.getMD5String(salt + command.getPassword());
                        DatabaseManager.addUser(command.getEmail(), command.getLogin(), hashPassword, salt);
                        response.setResponse(message);
                        response.setStatus(Status.NOTLOGINED);
                        sender.send(response);
                    } else {
                        message = "Неверный пароль. Количество оставшихся попыток: " + (3 - command.getAttempts()) + ". Попробуйте еще раз:";
                        response.setResponse(message);
                        response.setStatus(Status.WAITPASSWORD);
                        if (command.getAttempts() == 3) {
                            response.setStatus(Status.NOTLOGINED);
                            message = "Неверный пароль. Вы истратили все попытки, попробуйте зарегистрироваться еще раз.";
                            response.setResponse(message);
                        }
                        sender.send(response);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void login(String login, String password, Sender sender) {
        String message = "";
        Response response = new Response();
        response.setStatus(Status.NOTLOGINED);
        try {
            if (DatabaseManager.checkUser(login)) {
                if (checkPassword(login, password)) {
                    if (users.add(login)) {
                        message = "Вы успешно авторизировались";
                        response.setStatus(Status.LOGINED);
                        response.setResponse(message);
                        sender.send(response);
                    } else {
                        message = "Данный пользователь уже авторизирован";
                        response.setResponse(message);
                        sender.send(response);
                    }
                } else {
                    message = "Не верный логин или пароль";
                    response.setResponse(message);
                    sender.send(response);
                }
            } else {
                message = "Не верный логин или пароль";
                response.setResponse(message);
                sender.send(response);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    public static void logout(String login, Sender sender){
        users.remove(login);
        Response response = new Response("Вы вышли из вашей учетной записи");
        response.setStatus(Status.NOTLOGINED);
        try {
            sender.send(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getToken() {
        String password = new Random().ints(15, 33, 122).collect(StringBuilder::new,
                StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return password;
    }

    private static boolean checkPassword(String login, String password) {
        boolean result = false;
        try {
            result = DatabaseManager.getPassword(login).equals(Cryptographer.getMD5String(DatabaseManager.getSalt(login) + password));
        } catch (SQLException e) {
            e.printStackTrace();
  //          System.err.println("Проблема с базой данных");
            System.exit(1);
        }
        return result;
    }
}
