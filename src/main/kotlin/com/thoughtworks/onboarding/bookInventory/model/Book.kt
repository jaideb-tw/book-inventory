package com.thoughtworks.onboarding.bookInventory.model


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="Book")
data class Book(
    @Id
    var id: String?,
    var title: String?,
    var author: String?,
    var description: String?,
    var price: Double?,
    var quantity: Int?
)

