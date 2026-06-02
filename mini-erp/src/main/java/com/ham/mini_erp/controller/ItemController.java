package com.ham.mini_erp.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.ham.mini_erp.entity.Item;
import com.ham.mini_erp.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

// @RestController = @Controller + @ResponseBody
// Tells Spring this class handles HTTP requests and automatically serializes return values to JSON
@RestController
@RequestMapping("/api/items") // Base path for all endpoints in this controller
public class ItemController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);
    private final ItemRepository repository;

    // Constructor injection (Spring best practice over @Autowired on fields)
    // Interview point: "I prefer constructor injection because it makes dependencies explicit and enables immutability"
    public ItemController(ItemRepository repository) {
        this.repository = repository;
    }

    // ====== CRUD - READ ALL ======
    // GET /api/items
    // Returns all items from the database
    // In a real app, you'd add pagination (Pageable) for large datasets
    @GetMapping
    public List<Item> getAll() {
        return repository.findAll();
    }

    // ====== CRUD - READ BY ID ======
    // GET /api/items/{id}
    // Returns a single item or 404 if not found
    // ResponseEntity gives us control over HTTP status codes
    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable Long id) {
        Optional<Item> item = repository.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    // ====== CRUD - CREATE ======
    // POST /api/items
    // @RequestBody: Spring deserializes JSON body into Item object
    // @Valid: Triggers bean validation (@NotBlank, @Min) defined on Item entity
    // @Transactional: Ensures code generation + save happen atomically (prevents race conditions)
    // ResponseEntity.status(201): HTTP 201 Created is the standard response for resource creation
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Returns HTTP 201 instead of default 200
    @Transactional
    public Item create(@RequestBody @Valid Item item) {
        // Prevent mass assignment: force INSERT by nullifying any client-provided id
        item.setId(null);

        // Auto-generate values that shouldn't come from the client
        item.setCreatedDate(LocalDateTime.now());
        item.setCode(generateCode());

        return repository.save(item);
    }

    // ====== CRUD - UPDATE ======
    // PUT /api/items/{id}
    // @Valid: Triggers bean validation on the incoming request body
    // Proper update: fetch existing, update fields, save back
    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable Long id, @RequestBody @Valid Item updatedItem) {
        // Find existing item - throw 404 if not found
        Item existingItem = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));

        // Update only the fields that should be changeable
        existingItem.setName(updatedItem.getName());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setQuantity(updatedItem.getQuantity());
        existingItem.setActive(updatedItem.getActive());
        existingItem.setDescription(updatedItem.getDescription());
        // Note: code and createdDate are NOT updated - they're immutable after creation

        return ResponseEntity.ok(repository.save(existingItem));
    }

    // ====== CRUD - DELETE ======
    // DELETE /api/items/{id}
    // Returns 204 No Content on success, 404 if item doesn't exist
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    // ====== CUSTOM ENDPOINTS ======

    // GET /api/items/active
    // Returns only active items (soft-delete pattern)
    @GetMapping("/active")
    public List<Item> getActiveItems() {
        return repository.findByActiveTrue();
    }

    // ====== HELPER METHODS ======

    // Auto-generates item code in format: A0001, A0002, A0003, etc.
    // Finds the latest code, extracts the number, increments it
    private String generateCode() {
        return repository.findTopByOrderByCodeDesc().map(item -> {
            String code = item.getCode();
            int runningNo = Integer.parseInt(code.substring(1));
            runningNo++;
            return String.format("A%04d", runningNo);
        }).orElse("A0001"); // Default if no items exist yet
    }

    // ====== GLOBAL ERROR HANDLING (within this controller) ======
    // Catches all exceptions and returns a clean error response
    // In production, you'd use @ControllerAdvice for app-wide error handling
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(EntityNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralError(Exception ex) {
        log.error("Internal server error", ex);
        return "An internal server error occurred";
    }
}
