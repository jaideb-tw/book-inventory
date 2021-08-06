package com.thoughtworks.onboarding.bookInventory.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.model.BookDto
import com.thoughtworks.onboarding.bookInventory.model.BookEvent
import com.thoughtworks.onboarding.bookInventory.model.EventType
import com.thoughtworks.onboarding.bookInventory.repository.BookRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository) {

    @Autowired
    lateinit var webClient: WebClient

    var topicName: String = "book-inventory"

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, Any>

    fun fetchAll(title: String?, author: String?): List<Book>? {
        return when {
            title != null -> bookRepository.findByTitleIgnoreCase(title)
            author != null -> bookRepository.findByAuthorIgnoreCase(author)
            else -> bookRepository.findAll().toList()
        }
    }

    fun save(book: Book): Book? {
        book.id = ObjectId.get().toHexString()
        val savedBook = bookRepository.save(book)
        publishEvent(book, EventType.ADD)
        return savedBook
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
        if (updatedBook != null) {
            publishEvent(updatedBook, EventType.UPDATE)
        }
        return updatedBook
    }

    fun delete(id: String): Book? {
        val bookToBeDeleted = bookRepository.findById(id)
        if (bookToBeDeleted.isPresent) {
            bookRepository.deleteById(id)
            publishEvent(bookToBeDeleted.get(), EventType.DELETE)
        }
        return bookToBeDeleted.orElse(null)
    }

    fun search(title: String?, count: Optional<Int>): List<BookDto>? {
        return when {
            count.isPresent && count.get() <= 0 -> null
            else -> webClient.get()
                .uri {
                    it
                        .queryParam("q", title)
                        .queryParamIfPresent("maxResults", count)
                        .build()
                }
                .retrieve()
                .bodyToMono(GoogleBookResponse::class.java)
                .map { it.items }
                .block()
                ?.map { BookDto.from(it.volumeInfo, it.saleInfo) }
        }
    }

    private fun updateQuantityAndPrice(bookFound: Optional<Book>, book: Book) {
        bookFound.get().price = book.price
        bookFound.get().quantity = book.quantity
    }

    private fun publishEvent(book: Book, eventType: EventType) {
        val bookEvent = BookEvent.from(book, eventType)
        kafkaTemplate.send(topicName, ObjectMapper().writeValueAsString(bookEvent))
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleBookResponse {
    lateinit var items: List<GoogleBooks>
}

@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleBooks(var volumeInfo: GoogleBookDetails, var saleInfo: GoogleSalesDetails)


@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleBookDetails(var title: String, var authors: List<String>, var description: String)

@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleSalesDetails(var listPrice: LinkedHashMap<String, Any>? = null)



