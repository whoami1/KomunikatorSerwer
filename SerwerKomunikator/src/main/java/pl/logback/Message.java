package pl.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-16.
 */
public class Message {
    private static final Logger logger = LoggerFactory.getLogger(Message.class);

    public String log()
    {
        String msg = "Tutaj jest testowa wiadomosc";
        logger.info("The message is: {}", msg);
        logger.debug("Debugowanie...");
        return msg;
    }
}
