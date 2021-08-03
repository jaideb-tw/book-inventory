package com.thoughtworks.onboarding.bookInventory.controller

import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
    @Autowired val bookService: BookService
) {

    @GetMapping("/fetchAll")
    fun fetchAllBooks(): List<Book>? {
        val book = Book()
        val bookList = bookService.fetchAll();

        return bookList
    }
}