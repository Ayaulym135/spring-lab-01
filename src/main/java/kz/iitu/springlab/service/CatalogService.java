package kz.iitu.springlab.service;

import kz.iitu.springlab.audit.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class CatalogService {

    // Внедряем прокси самого себя для исправления самовызова
    @Autowired
    @Lazy
    private CatalogService self;

    public String findById(long id) {
        sleep(50);
        return "Item no. " + id;
    }

    @Audited(action = "CATALOG_LIST", logArguments = true)
    public List<String> findAll(int limit) {
        sleep(300);
        return IntStream.rangeClosed(1, limit)
                .mapToObj(i -> "Item no. " + i)
                .toList();
    }

    @Audited(action = "CATALOG_REMOVE")
    public String remove(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid identifier: " + id);
        }
        return "Removed item no. " + id;
    }

    // 1. Внутренний самовызов (аспекты НЕ сработают на методы remove)
    public String removeTwice(long id) {
        String first  = remove(id);     // Вызов через this (мимо прокси)
        String second = remove(id + 1); // Вызов через this (мимо прокси)
        return first + "; " + second;
    }

    // 2. Исправленный самовызов (аспекты СРАБОТАЮТ на методы remove)
    public String removeTwiceFixed(long id) {
        String first  = self.remove(id);     // Вызов через self-прокси
        String second = self.remove(id + 1); // Вызов через self-прокси
        return first + "; " + second;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}