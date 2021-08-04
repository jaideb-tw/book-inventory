package com.thoughtworks.onboarding.bookInventory.controller

import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(
    @Autowired val bookService: BookService
) {

    @GetMapping("/fetchAll")
    fun fetchAllBooks(): List<Book>? {
        val bookList = bookService.fetchAll();
        return bookList
    }

    @PostMapping("/save")
    fun addBooks(@RequestBody book:Book){
        bookService.saveBooks(book)
    }
}