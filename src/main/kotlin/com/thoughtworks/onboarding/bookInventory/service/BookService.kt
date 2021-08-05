package com.thoughtworks.onboarding.bookInventory.service

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.onboarding.bookInventory.configuration.WebClientConfiguration
import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.repository.BookRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class BookService {

    @Autowired
    lateinit var bookRepository: BookRepository


    @Autowired
    lateinit var webClient: WebClient

    fun fetchAll(title: String?, author: String?): List<Book>? {
        return when {
            title != null -> bookRepository.findByTitleIgnoreCase(title)
            author != null -> bookRepository.findByAuthorIgnoreCase(author)
            else -> bookRepository.findAll().toList()
        }
    }

    fun save(book: Book): Book? {
        book.id = ObjectId.get().toHexString()
        return bookRepository.save(book)
    }

    fun update(id: String, book: Book): Book? {
        var updatedBook: Book? = null
        val bookFound = bookRepository.findById(id)
        when {
            bookFound.isPresent -> {
                updateQuantityAndPrice(bookFound, book)
                updatedBook = bookRepository.save(bookFound.get())
            }
        }
        return updatedBook
    }

    private fun updateQuantityAndPrice(
        bookFound: Optional<Book>,
        book: Book,
    ) {
        bookFound.get().price = book.price
        bookFound.get().quantity = book.quantity
    }

    fun delete(id: String): Book? {
        val bookToBeDeleted = bookRepository.findById(id)
        if (bookToBeDeleted.isPresent)
            bookRepository.deleteById(id)
        return bookToBeDeleted.orElse(null)
    }

    fun search(title: String?): Any? {
        var result= webClient.get()
            .uri { it.queryParam("q", title).build() }
            .retrieve()
            .bodyToMono(Any::class.java)
            .map { val writeValueAsString = ObjectMapper().writeValueAsString(it)
               ObjectMapper().readValue(writeValueAsString,GoogleBooks::class.java)
            }
            .block()


        return result
    }


}

@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleBooks {
    lateinit var items: Any
}
