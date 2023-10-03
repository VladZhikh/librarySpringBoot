package springcourse.librarySpringBoot.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springcourse.librarySpringBoot.models.Book;
import springcourse.librarySpringBoot.models.Person;
import springcourse.librarySpringBoot.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PeopleService peopleService;

    public BookService(BookRepository bookRepository, PeopleService peopleService) {
        this.bookRepository = bookRepository;
        this.peopleService = peopleService;
    }
    public List<Book> findAll(){
        return bookRepository.findAll(Sort.by("bookYear"));
    }
    public Book findOne(int id){
       Optional<Book> findBook = bookRepository.findById(id);
       return findBook.orElse(null);
    }
    public List<Book> findWithPagination(Integer page, Integer booksPerPage) {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage,
                    Sort.by("bookYear"))).getContent();
    }
    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }
    @Transactional
    public void update(int id, Book updatedBook){
        Book bookToBeUpdated = bookRepository.findById(id).get();
        updatedBook.setBookId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        updatedBook.setTakeAt(bookToBeUpdated.getTakenAt());
        bookRepository.save(updatedBook);
    }
    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }

    public List<Book> findByOwner(Person owner){
        return bookRepository.findByOwner(owner);
    }
    @Transactional
    public void selectReader(Integer idReader, Book selectedBook){
        Person person = peopleService.findOne(idReader);
        selectedBook.setOwner(person);
        selectedBook.setTakeAt(new Date());
        bookRepository.save(selectedBook);
    }
    @Transactional
    public void cancelReader(Book selectedBook){
        selectedBook.setOwner(null);
        selectedBook.setTakeAt(null);
        bookRepository.save(selectedBook);
    }
    public List<Book> findByTitle(String title){
        return bookRepository.findByTitleStartingWith(title);
    }

    public void checkingDelay(List<Book> bookList){
        for (Book book : bookList){
            //book.setDelay(false);
            update(book.getBookId(),book);
            long millis =book.getTakenAt().getTime();
            long millisCurr =System.currentTimeMillis();
            if((millisCurr-millis)>864000000) book.setDelay(true);
            //System.out.println(book.isDelay());
        }
    }
}
