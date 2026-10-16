package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService service;

    public BookRestController(BookService service) {
        this.service = service;
    }

    // 1. Список с фильтрацией
    @GetMapping
    public List<Book> list(@RequestParam(required = false) String author,
                           @RequestParam(defaultValue = "10") int limit) {
        return service.findAll(author).stream().limit(limit).toList();
    }

    // 2. Чтение одной записи (200 или 404)
    @GetMapping("/{id}")
    public ResponseEntity<Book> find(@PathVariable long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. Создание (201 Created + Location)
    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book saved = service.create(book);
        return ResponseEntity
                .created(URI.create("/api/books/" + saved.id()))
                .body(saved);
    }

    // 4. Замена (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable long id, @RequestBody Book book) {
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        Book updated = service.create(new Book(id, book.title(), book.author(), book.year()));
        return ResponseEntity.ok(updated);
    }

    // 5. Удаление (204 No Content или 404)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // ИНДИВИДУАЛЬНОЕ ЗАДАНИЕ (Вариант 1): Поиск по фрагменту названия без учета регистра
    @GetMapping("/search")
    public List<Book> searchByTitle(@RequestParam(defaultValue = "") String title) {
        return service.findAll(null).stream()
                .filter(b -> b.title().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }
}