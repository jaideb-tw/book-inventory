package com.thoughtworks.onboarding.bookInventory.model


data class Book(
    val title: String,
    val author: String?,
    val description: String?,
    val price: Double,
    val quantity: Int?
) {

    constructor(
    ) : this("", "", "", 0.0, null) {

    }


}
