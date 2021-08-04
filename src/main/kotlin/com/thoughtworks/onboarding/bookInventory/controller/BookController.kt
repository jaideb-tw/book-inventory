package com.thoughtworks.onboarding.bookInventory.controller

import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.responce.BookResponse
import com.thoughtworks.onboarding.bookInventory.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(
    @Autowired val bookService: BookService
) {

    @GetMapping("/fetchAll")
    fun fetchAll(): List<Book>? {
        return bookService.fetchAll()
    }

    @PostMapping("/save")
    fun addBooks(@RequestBody book: Book): BookResponse {
        val savedBook = bookService.save(book)

        if (savedBook != null) return BookResponse(HttpStatus.OK.value(), "Book saved Successfully", savedBook)

        return BookResponse(HttpStatus.NO_CONTENT.value(), "Could Not save book", savedBook)

    }

}