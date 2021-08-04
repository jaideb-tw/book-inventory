package com.thoughtworks.onboarding.bookInventory.service

import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.repository.BookRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookService {

    @Autowired
    lateinit var bookRepository: BookRepository

    fun fetchAll(): List<Book>? {
        return bookRepository.findAll().toList()
    }

    fun save(book: Book):Book {
        book.id = ObjectId.get().toHexString()
        return bookRepository.save(book)
    }
}