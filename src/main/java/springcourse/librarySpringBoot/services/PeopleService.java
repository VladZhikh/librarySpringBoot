package springcourse.librarySpringBoot.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springcourse.librarySpringBoot.models.Person;
import springcourse.librarySpringBoot.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;


    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }
    public List<Person> findAll() {
        return peopleRepository.findAll(Sort.by("birthYear"));
    }
    public Person findOne(int id){
        Optional<Person> findPerson=peopleRepository.findById(id);
        return findPerson.orElse(null);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }
    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setPerson_id(id);
//        Person testPerson = getPersonByFullName(updatedPerson.getFullName());
//        if(testPerson.getPerson_id()!=id) return;
        peopleRepository.save(updatedPerson);
    }
    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public Person getPersonByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }


}
