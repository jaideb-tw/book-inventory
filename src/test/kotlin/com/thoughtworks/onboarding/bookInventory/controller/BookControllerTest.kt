package com.thoughtworks.onboarding.bookInventory.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.service.BookService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController::class)
internal class BookControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var bookService: BookService

    @Test
    fun `should be able to fetch all books`() {
        mockMvc.perform(
            get("/books/fetchAll")
        ).andExpect(status().isOk)
        verify(bookService, times(1)).fetchAll()
    }

    @Test
    fun `should be able to save all books`() {
        val book = Book(null, "Harry Potter", "ABCDS", "", 98.0, 2)
        mockMvc.perform(
            post("/books/save")
                .content(ObjectMapper().writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
        verify(bookService, times(1)).save(book)
    }
}