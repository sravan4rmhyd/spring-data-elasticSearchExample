package org.sravan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.sravan.model.Book;
import org.sravan.service.BookService;

@SpringBootApplication
public class SpringDataElasticsearchExampleApplication  implements CommandLineRunner{


    @Autowired
    private BookService bookService;

    public static void main(String args[]) {
        SpringApplication.run(SpringDataElasticsearchExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        //printElasticSearchInfo();

        bookService.save(new Book("1001", "Elasticsearch Basics", "Sravan Kumar", "03-Jun-2019"));
        bookService.save(new Book("1002", "Apache Lucene Basics", "Sravan Kumar", "13-MAR-2017"));
        bookService.save(new Book("1003", "Apache Solr Basics", "Sravan Kumar", "21-MAR-2017"));

        //fuzzey search
        Page<Book> books = bookService.findByAuthor("Rambabu", PageRequest.of(0, 10));

        //List<Book> books = bookService.findByTitle("Elasticsearch Basics");

        books.forEach(x -> System.out.println(x));
    }

	/*
	 * //useful for debug, print elastic search details private void
	 * printElasticSearchInfo() {
	 * 
	 * System.out.println("--ElasticSearch--"); Client client = es.getClient();
	 * Map<String, Object> asMap = client.settings().getAsStructuredMap();
	 * 
	 * asMap.forEach((k, v) -> { System.out.println(k + " = " + v); });
	 * System.out.println("--ElasticSearch--"); }
	 */

}
