package com.thoughtworks.onboarding.bookInventory.controller

import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.responce.BookResponse
import com.thoughtworks.onboarding.bookInventory.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/books")
class BookController(
    @Autowired val bookService: BookService,
) {

    @GetMapping("/fetchAll")
    fun fetchAll(
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) author: String?,
    ): ResponseEntity<List<Book>>? {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.fetchAll(title, author))
    }

    @PostMapping("/save")
    fun save(@RequestBody book: Book): ResponseEntity<BookResponse> {
        val savedBook = bookService.save(book)
        return if (savedBook != null) ResponseEntity.status(HttpStatus.CREATED)
            .body(BookResponse(HttpStatus.OK.value(), "Book saved Successfully", savedBook))
        else ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(BookResponse(HttpStatus.NO_CONTENT.value(), "Could Not save book", savedBook))

    }

    @PutMapping("/update/{id}")
    fun update(@PathVariable id: String, @RequestBody book: Book): ResponseEntity<BookResponse> {
        val updatedBook = bookService.update(id, book)
        return if (updatedBook != null)
            ResponseEntity.status(HttpStatus.OK)
                .body(BookResponse(HttpStatus.OK.value(), "Book updated successfully", updatedBook))
        else
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BookResponse(HttpStatus.NOT_FOUND.value(), "Book not found", updatedBook))
    }

    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable id: String): BookResponse {
        val deletedBook = bookService.delete(id)
        return if (deletedBook != null)
            BookResponse(HttpStatus.OK.value(), "Book deleted successfully", deletedBook)
        else BookResponse(HttpStatus.NOT_FOUND.value(), "Book not found", null)
    }

    @GetMapping("/search")
    fun search(
        @RequestParam(required = false) title: String?,
        ): Any {

        return ResponseEntity.status(HttpStatus.OK).body(bookService.search(title))
    }
}