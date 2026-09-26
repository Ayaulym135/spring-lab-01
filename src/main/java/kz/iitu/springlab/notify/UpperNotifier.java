package kz.iitu.springlab.notify;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("upper") // Имя бина из вашего варианта
@Order(3)
public class UpperNotifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(UpperNotifier.class);

    @PostConstruct
    public void init() {
        log.info("CUSTOM NOTIFIER >> UpperNotifier initialized via @PostConstruct");
    }

    @Override
    public String send(String message) {
        if (message == null) return "";
        return message.toUpperCase(); // Логика обработки по вашему варианту
    }

    @Override
    public String channel() {
        return "upper";
    }
}