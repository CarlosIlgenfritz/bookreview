package com.morotech.bookqualifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookQualifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookQualifierApplication.class, args);
	}

}
