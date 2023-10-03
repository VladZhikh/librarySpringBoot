package springcourse.librarySpringBoot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springcourse.librarySpringBoot.dao.BookDao;
import springcourse.librarySpringBoot.dao.PersonDao;
import springcourse.librarySpringBoot.models.Book;
import springcourse.librarySpringBoot.models.Person;
import springcourse.librarySpringBoot.services.BookService;
import springcourse.librarySpringBoot.services.PeopleService;

//import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Controller
public class BookController {
    private final BookDao bookDao;
    private final PersonDao personDao;
    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookDao bookDao, PersonDao personDao, BookService bookService, PeopleService peopleService) {
        this.bookDao = bookDao;
        this.personDao = personDao;
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping("/book")
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage) {
        if (page == null || booksPerPage == null)
            model.addAttribute("book", bookService.findAll());
        else model.addAttribute("book",bookService.findWithPagination(page,booksPerPage));
        return "book/index";
    }

    @GetMapping("book/new")
    public String newBook(@ModelAttribute("book") Book book) {
        //System.out.println(book.getTitle() + " ," + book.getAuthor());
        return "book/new";
    }
    @PostMapping("book/new")
    public String create(@ModelAttribute("book")  @Valid Book book,
                         BindingResult bindingResult) throws SQLException {
        if(bindingResult.hasErrors()) return "book/new";
        bookService.save(book);
        return "redirect:/book";
    }

    @GetMapping("book/{id}")
    public String show(@PathVariable("id") int id, Model model){
        Book book= bookService.findOne(id);
        Person person=book.getOwner();

        if (person==null) person = new Person("",0);
        model.addAttribute("book",book);
        model.addAttribute("person",person);
        model.addAttribute("people", peopleService.findAll());
        return "book/show";
    }

    @GetMapping("book/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "book/edit";
    }

    @PatchMapping("book/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,BindingResult bindingResult,
                         @PathVariable("id") int id) {
        book.setBookId(id);
        System.out.println(book.getBookYear());
        if(bindingResult.hasErrors()) return "book/edit";
        bookService.update(id,book);
        return "redirect:/book";
    }

    @DeleteMapping("book/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/book";
    }

    @PatchMapping("book/select/{id}")
    public String selectPerson(@ModelAttribute("person") Person person, @PathVariable int id){
        Book book = bookService.findOne(id);
        bookService.selectReader(person.getPerson_id(), book);
        return "redirect:/book";
    }

    @PatchMapping("book/cancel/{id}")
    public String cancel(@PathVariable int id){
        Book book = bookService.findOne(id);
        bookService.cancelReader(book);
        return "redirect:/book";
    }

    @GetMapping("book/search")
    public String searchBooks(Model model,
                              @RequestParam(value = "title", required = false) String title){
        List<Book> bookList= bookService.findByTitle(title);
        model.addAttribute("books", bookList);
        return "book/search";
    }
}
