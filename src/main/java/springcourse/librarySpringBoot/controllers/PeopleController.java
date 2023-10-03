package springcourse.librarySpringBoot.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springcourse.librarySpringBoot.dao.PersonDao;
import springcourse.librarySpringBoot.models.Book;
import springcourse.librarySpringBoot.models.Person;
import springcourse.librarySpringBoot.services.BookService;
import springcourse.librarySpringBoot.services.PeopleService;
import springcourse.librarySpringBoot.util.PersonValidator;

import java.util.List;

@Controller
public class PeopleController {
    private final PersonDao personDao;
    private final PeopleService peopleService;
    private final BookService bookService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDao personDao, PeopleService peopleService,
                            BookService bookService,
                            PersonValidator personValidator) {
        this.personDao = personDao;
        this.peopleService = peopleService;
        this.bookService = bookService;
        this.personValidator = personValidator;
    }

    @GetMapping("/people")
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }
    @GetMapping("people/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }
    @PostMapping("/people")
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/new";
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("people/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }
    @PatchMapping("people/{id}/edit")
    public String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        //System.out.println(person+" - "+id);
        person.setPerson_id(id);
        //System.out.println(person);
        Person testPerson = peopleService.getPersonByFullName(person.getFullName());
        if(testPerson==null){
            if(bindingResult.hasErrors()) return "people/edit";
            peopleService.update(id,person);
            return "redirect:/people";
        }

        if(testPerson.getPerson_id()!=id)
            personValidator.validate(person,bindingResult);
        if(bindingResult.hasErrors()) return "people/edit";
        peopleService.update(id,person);
        return "redirect:/people";
    }

    @DeleteMapping("people/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }

    @GetMapping("people/{id}/find")
    public String findBooks(@PathVariable("id") int id,Model model){
        Person person = peopleService.findOne(id);
        List<Book> bookList=bookService.findByOwner(person);
        bookService.checkingDelay(bookList);
        model.addAttribute("book", bookList);
        model.addAttribute("person",person);
        return "people/find";
    }
}
