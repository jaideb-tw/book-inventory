package com.thoughtworks.onboarding.bookInventory.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.repository.BookRepository
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

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

    fun search(title: String?): BookDto? {
        val result = webClient.get()
            .uri { it.queryParam("q", title).build() }
            .retrieve()
            .bodyToMono(Any::class.java)
            .map {
                val writeValueAsString = ObjectMapper().writeValueAsString(it)
                ObjectMapper().readValue(writeValueAsString, GoogleBooks::class.java)
            }
            .block()
        return convertToBook(result)


    }

    private fun convertToBook(result: GoogleBooks?): BookDto {
        val cast = result?.items as ArrayList<LinkedHashMap<String, Any>>
        val volumeInfo = cast[0]["volumeInfo"]

        val actualVolumeInfo = ObjectMapper().readValue(
            ObjectMapper().writeValueAsString(volumeInfo),
            VolumeInfo::class.java)

        val actualSaleInfo = ObjectMapper().readValue(
            ObjectMapper().writeValueAsString(
                cast[0]["saleInfo"]), SaleInfo::class.java)

        val amount = actualSaleInfo.listPrice?.get("amount")
        return setBookDto(actualVolumeInfo, amount)
    }

    private fun setBookDto(actualVolumeInfo: VolumeInfo, amount: Any?): BookDto {
        val bookDto = BookDto()
        bookDto.authors = actualVolumeInfo.authors
        bookDto.description = actualVolumeInfo.description
        bookDto.price = amount
        bookDto.title = actualVolumeInfo.title
        return bookDto
    }


}

@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleBooks {
    var items: Any? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class SaleInfo {
    var listPrice: LinkedHashMap<String, Any>? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class VolumeInfo {
    var title: String? = null
    var authors: List<String>? = null
    var description: String? = null
}

class BookDto {
    var authors: List<String>? = null
    var title: String? = null
    var description: String? = null
    var price: Any? = null

}