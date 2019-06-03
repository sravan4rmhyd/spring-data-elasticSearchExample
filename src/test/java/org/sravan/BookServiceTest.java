package org.sravan;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;
import org.sravan.model.Book;
import org.sravan.service.BookService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringDataElasticsearchExampleApplication.class)
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Before
    public void before() {
        esTemplate.deleteIndex(Book.class);
        esTemplate.createIndex(Book.class);
        esTemplate.putMapping(Book.class);
        esTemplate.refresh(Book.class);
    }

   @Test
    public void testSave() {

        Book book = new Book("1001", "Elasticsearch Basics", "Sravan Kumar", "03-Jun-2019");
        Book testBook = bookService.save(book);

        assertNotNull(testBook.getId());
        assertEquals(testBook.getTitle(), book.getTitle());
        assertEquals(testBook.getAuthor(), book.getAuthor());
        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());

    }

    @Test
    public void testFindOne() {

        Book book = new Book("1001", "Elasticsearch Basics", "Sravan Kumar", "03-Jun-2019");
        bookService.save(book);

        Book testBook = bookService.findOne(book.getId());

        assertNotNull(testBook.getId());
        assertEquals(testBook.getTitle(), book.getTitle());
        assertEquals(testBook.getAuthor(), book.getAuthor());
        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());

    }

   @Test
    public void testFindByTitle() {

        Book book = new Book("1001", "Elasticsearch Basics", "Sravan Kumar", "03-Jun-2019");
        bookService.save(book);

        List<Book> byTitle = bookService.findByTitle(book.getTitle());
        assertThat(byTitle.size(), is(1));
    }

    @Test
    public void testFindByAuthor() {

        List<Book> bookList = new ArrayList<>();

        bookList.add(new Book("1001", "Elasticsearch Basics", "Sravan Kumar", "03-Jun-2019"));
        bookList.add(new Book("1002", "Apache Lucene Basics", "Sravan Kumar", "13-MAR-2017"));
        bookList.add(new Book("1003", "Apache Solr Basics", "Sravan Kumar", "21-MAR-2017"));
        bookList.add(new Book("1007", "Spring Data + ElasticSearch", "Sravan Kumar", "01-APR-2017"));
        bookList.add(new Book("1008", "Spring Boot + MongoDB", "Amruth", "25-FEB-2017"));

        for (Book book : bookList) {
            bookService.save(book);
        }

        Page<Book> byAuthor = bookService.findByAuthor("Sravan Kumar", PageRequest.of(0, 10));
        assertThat(byAuthor.getTotalElements(), is(4L));

        Page<Book> byAuthor2 = bookService.findByAuthor("Amruth", PageRequest.of(0, 10));
        assertThat(byAuthor2.getTotalElements(), is(1L));

    }
    

    @Test
    public void testHightLightFindByAuthor() {
    	 Book book = new Book("1001", "Elasticsearch Basics", "Sravan Kumar", "03-Jun-2019");
         bookService.save(book);
    	QueryBuilder query = QueryBuilders.matchQuery("author", "Sravan Kumar"); 
    	SearchQuery searchQuery = new NativeSearchQueryBuilder()
    	                           .withQuery(query)
    	                           .withHighlightFields(new Field("author").preTags("<mark>")
    	                                   .postTags("</mark>")).build();
    	List<Book> books = esTemplate.queryForList(searchQuery, Book.class);
    	assertTrue(books.size()>0);
    }

   @Test
    public void testDelete() {

        Book book = new Book("1001", "Elasticsearch Basics", "Sravan Kumar", "03-Jun-2019");
        bookService.save(book);
        bookService.delete(book);
        Book testBook = bookService.findOne(book.getId());
        assertNull(testBook);
    }

}