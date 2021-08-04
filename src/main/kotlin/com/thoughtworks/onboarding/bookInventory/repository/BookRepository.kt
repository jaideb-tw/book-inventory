package com.thoughtworks.onboarding.bookInventory.repository

import com.thoughtworks.onboarding.bookInventory.model.Book
import org.springframework.data.mongodb.repository.MongoRepository


interface BookRepository : MongoRepository<Book, String> {
}