package com.thoughtworks.onboarding.bookInventory.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.thoughtworks.onboarding.bookInventory.service.GoogleBookDetails
import com.thoughtworks.onboarding.bookInventory.service.GoogleSalesDetails

@JsonIgnoreProperties(ignoreUnknown = true)
class BookDto(
    var authors: List<String>? = null,
    var title: String? = null,
    var description: String? = null,
    var price: Any? = null
) {


    companion object {
        fun from(volumeInfo: GoogleBookDetails, salesInfo: GoogleSalesDetails): BookDto {
            return BookDto(
                title = volumeInfo.title,
                authors = volumeInfo.authors,
                description = volumeInfo.description,
                price = salesInfo.listPrice?.get("amount")
            )
        }
    }
}