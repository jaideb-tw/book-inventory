package com.thoughtworks.onboarding.bookInventory.responce

import com.thoughtworks.onboarding.bookInventory.model.Book

class BookResponse(var statusCode: Int, var message: String, var book: Book?) {


}