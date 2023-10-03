package springcourse.librarySpringBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springcourse.librarySpringBoot.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person,Integer> {
    Person findByFullName(String fullName);
}
