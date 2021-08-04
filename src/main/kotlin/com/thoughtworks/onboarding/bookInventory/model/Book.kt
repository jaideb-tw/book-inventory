package com.thoughtworks.onboarding.bookInventory.model


import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="Book")
data class Book(
    @Id
    var id: String?,
    val title: String,
    val author: String?,
    val description: String?,
    val price: Double,
    val quantity: Int?
) {
}
