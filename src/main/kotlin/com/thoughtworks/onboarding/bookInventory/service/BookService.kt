package com.thoughtworks.onboarding.bookInventory.service

import com.thoughtworks.onboarding.bookInventory.model.Book
import org.springframework.stereotype.Service

@Service
class BookService {

    companion object{
        fun getBooks(): List<Book>{
            var bookList = ArrayList<Book>()

            bookList.add(Book("C++","","",90.0,8))
            bookList.add(Book("","","",90.0,8))
            bookList.add(Book("","","",90.0,8))
            return bookList
        }

    }

    fun fetchAll(): List<Book>? {
        return getBooks()
    }
}