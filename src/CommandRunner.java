import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * <p>Класс для считывания и выполнения команд</p>
 *
 * @author Вотинцев Евгений
 * @version 1.0
 */
public class CommandRunner {
    private ByteBuffer buffer;
    private DatagramChannel channel;
    private InetSocketAddress clientAddress;
    private Sender sender;
    private Response response;
    private Command command;


    /**
     * <p>Объект команды</p>
     */
    private Animal current_object = new Pyatachok();


    /**
     * <p>Считает количество заданных символов в строке</p>
     *
     * @param word Строка
     * @param s    Символ
     * @return Количество
     */
    public int charCounter(String word, char s) {
        int count = 0;
        for (char element : word.toCharArray()) {
            if (element == s) count += 1;
        }
        return count;
    }

    private int charCounter(String word, char s, char e) {
        int countS = 0;
        int countE = 0;
        for (char el : word.toCharArray()) {
            if (countE % 2 == 0) {
                if (el == s) countS += 1;
            }
            if (el == e) countE += 1;
        }
        return countS;
    }

    String filename = System.getenv().get("FILENAME");



    public CommandRunner(ByteBuffer buffer, DatagramChannel channel, InetSocketAddress clientAddress, Command command) {
        this.buffer = buffer;
        this.channel = channel;
        this.clientAddress = clientAddress;
        this.command = command;
        sender = new Sender(buffer, channel, clientAddress);
    }

    /**
     * <p>Считывает и запускает команды</p>
     */
    public synchronized void runCom() throws IOException {
try {

    switch (command.getName()) {
        case ("remove"): {
            String message = "";
            if (CollectionManager.remove(command.getObject(),command.getLogin())) {
                message = "Объект " + command.getObject().getName() + " удален";
                response = new Response(message);
                response.setStatus(Status.LOGINED);
                sender.send(response);
            } else {
                message = "Объект " + command.getObject().getName() + " не найден в коллекции или у вас нет к нему доступа";
                response = new Response(message);
                response.setStatus(Status.LOGINED);
                sender.send(response);
            }
        }
        break;
        case ("show"): {
            String message = "";
            message = CollectionManager.show();
            response = new Response(message);
            response.setStatus(Status.LOGINED);
            sender.send(response);
        }
        break;
        case ("add_if_min"): {
            if (CollectionManager.add_if_min(command.getObject(),command.getLogin())) {
                String message = "Элемент добавлен в коллекцию";
                response = new Response(message);
                response.setStatus(Status.LOGINED);
                sender.send(response);
            } else {
                String message = "Элемент не добавлен в коллекцию";
                response = new Response(message);
                response.setStatus(Status.LOGINED);
                sender.send(response);
            }
        }
        break;
        case ("remove_greater"): {
            String message = CollectionManager.remove_greater(command.getObject(),command.getLogin());
            response = new Response(message);
            response.setStatus(Status.LOGINED);
            sender.send(response);
        }
        break;
        case ("info"): {
            String message = CollectionManager.info();
            response = new Response(message);
            response.setStatus(Status.LOGINED);
            sender.send(response);
        }
        break;
        case ("add"): {
            if (CollectionManager.add(command.getObject(),command.getLogin())) {
                String message = "Элемент добавлен в коллекцию";
                response = new Response(message);
                response.setStatus(Status.LOGINED);
                sender.send(response);
            } else {
                String message = "Элемент не добавлен в коллекцию";
                response = new Response(message);
                response.setStatus(Status.LOGINED);
                sender.send(response);
            }
        }
        break;
        case ("help"): {
            String message = CollectionManager.help();
            response = new Response(message);
            response.setStatus(Status.LOGINED);
            sender.send(response);
        }
        break;
        case ("import"): {
            String message = CollectionManager.import_(command.getFile());
            response = new Response(message);
            response.setStatus(Status.LOGINED);
            sender.send(response);
        }
        break;
        case ("save"): {
            try {
                String message = CollectionManager.save(CollectionManager.animals, filename);
                response = new Response(message);
                response.setStatus(Status.LOGINED);
                sender.send(response);
            } catch (IOException | NullPointerException e) {
                String message ="Не удалось сохранить коллекцию";
                response = new Response(message);
                response.setStatus(Status.LOGINED);
                sender.send(response);
            } catch (TransformerException | ParserConfigurationException e) {
                String message = "Ошибка парсинга";
                response = new Response(message);
                response.setStatus(Status.LOGINED);
                sender.send(response);
            }
        }break;
        case ("connect"):{
            String message ="Соединение с сервером установлено";
            response = new Response(message);
            response.setStatus(Status.NOTLOGINED);
            sender.send(response);
        }break;
        case ("register"):{
            Authorization.register(command,sender);
        }break;
        case ("login"):{
            Authorization.login(command.getLogin(), command.getPassword(),sender);
        }break;
        case ("logout"):{
            Authorization.logout(command.getLogin(),sender);
        }
    }
}catch (NullPointerException e){

}
    }
}
