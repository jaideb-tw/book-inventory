package com.thoughtworks.onboarding.bookInventory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
class BookInventoryApplication

fun main(args: Array<String>) {
	runApplication<BookInventoryApplication>(*args)
}
