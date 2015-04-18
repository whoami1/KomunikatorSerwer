package pl.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-16.
 */
public class Message {
    private static final Logger logger = LoggerFactory.getLogger(Message.class);

    public String log(Socket socket, String text)
    {
        String msg = "|" + "Port: "+ socket.getPort() + "|" + text;
        logger.info(msg);
        logger.debug("Debugowanie...");
        return msg;
    }
}
