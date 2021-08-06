package com.thoughtworks.onboarding.bookInventory.model

class BookEvent(
    var id: String? = null,
    var title: String? = null,
    var evenType: EventType? = null
) {
    companion object {
        fun from(book: Book, evenType: EventType?): BookEvent {
            return BookEvent(book.id, book.title, evenType)


        }
    }
}
