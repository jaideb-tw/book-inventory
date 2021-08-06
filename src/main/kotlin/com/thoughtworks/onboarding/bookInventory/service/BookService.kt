package com.thoughtworks.onboarding.bookInventory.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.model.BookDto
import com.thoughtworks.onboarding.bookInventory.repository.BookRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository) {

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

    private fun updateQuantityAndPrice(bookFound: Optional<Book>, book: Book) {
        bookFound.get().price = book.price
        bookFound.get().quantity = book.quantity
    }

    fun delete(id: String): Book? {
        val bookToBeDeleted = bookRepository.findById(id)
        if (bookToBeDeleted.isPresent)
            bookRepository.deleteById(id)
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



