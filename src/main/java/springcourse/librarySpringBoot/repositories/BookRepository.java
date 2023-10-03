package springcourse.librarySpringBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springcourse.librarySpringBoot.models.Book;
import springcourse.librarySpringBoot.models.Person;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByOwner(Person owner);
    List<Book> findByTitleStartingWith(String title);
}
