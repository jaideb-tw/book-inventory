package com.thoughtworks.onboarding.bookInventory.service

import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookService {

    @Autowired
    lateinit var bookRepository: BookRepository
    fun fetchAll(): List<Book>? {
        return bookRepository.findAll().toList()
    }

    fun saveBooks(book: Book) {
        bookRepository.save(book)

    }
}