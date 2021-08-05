package com.thoughtworks.onboarding.bookInventory.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.onboarding.bookInventory.model.Book
import com.thoughtworks.onboarding.bookInventory.service.BookService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
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
        verify(bookService, times(1)).fetchAll(null, null)
    }

    @Test
    fun `should be able to save all books`() {
        val book = Book(ObjectId.get().toHexString(), "Harry Potter", "ABCDS", "", 98.0, 2)
        mockMvc.perform(
            post("/books/save")
                .content(ObjectMapper().writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON)
        )
        verify(bookService, times(1)).save(book)
    }

    @Test
    fun `should be able to update price and quantity of book`() {
        val book = Book("4", null, null, null, 89.0, 8)
        `when`(bookService.update("4", book)).thenReturn(book)
        mockMvc.perform(
            put("/books/update/{id}", "4").content(ObjectMapper().writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
        verify(bookService, times(1)).update("4", book)

    }

    @Test
    fun `should be able to delete` (){
        val book = Book("4", null, null, null, 89.0, 8)
        `when`(bookService.delete("4")).thenReturn(book)
        mockMvc.perform(
            delete("/books/delete/{id}", "4").content(ObjectMapper().writeValueAsString(book))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
    }
}