package com.thoughtworks.onboarding.bookInventory.service

import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.repository.BookRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class BookServiceTest {

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var bookService: BookService

    @AfterEach
    fun cleanDataBase() {
        bookRepository.deleteAll()
    }

    @Test
    fun `should be able to fetch all books`() {
        val book = Book(ObjectId.get().toHexString(), "Harry Potter", "ABCDS", "", 98.0, 2)
        val anotherBook = Book(ObjectId.get().toHexString(), "JAVA", "ABCDS", "", 98.0, 2)
        val bookList = ArrayList<Book>()
        bookRepository.save(book)
        bookList.add(book)

        val returnedList = bookService.fetchAll(null, null)
        Assertions.assertEquals(bookList, returnedList)

    }

    @Test
    fun `should be able to save book`() {
        val book = Book(ObjectId.get().toHexString(), "Harry Potter", "ABCDS", "", 98.0, 2)

        val savedBook = bookService.save(book);

        Assertions.assertEquals(savedBook, book)

    }
}