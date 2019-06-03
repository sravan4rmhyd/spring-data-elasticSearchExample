package org.sravan.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.sravan.model.Book;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
	Page<Book> findByAuthor(String author, Pageable pageable);

	List<Book> findByTitle(String title);
}
