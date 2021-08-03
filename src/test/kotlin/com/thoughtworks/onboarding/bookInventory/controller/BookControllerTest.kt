package com.thoughtworks.onboarding.bookInventory.controller

import com.thoughtworks.onboarding.bookInventory.service.BookService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
        Mockito.verify(bookService,times(1)).fetchAll()
    }
}