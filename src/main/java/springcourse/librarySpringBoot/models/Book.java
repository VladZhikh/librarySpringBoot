package springcourse.librarySpringBoot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


import java.util.Date;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name="book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @ManyToOne
    @JoinColumn(name = "person_id",referencedColumnName = "person_id")
    private Person owner;

    @Column(name = "book_name")
    @NotEmpty(message = "Title shouldn't be empty")
    @Size(min = 2, max = 40, message = "Title size should be min-2 max-40 characters")
    private String title;
    @Column(name = "author")
    @NotEmpty(message = "Author should not be empty")
    @Size(min = 2, max = 30, message = "Author size should be min-2 max-30 characters")
    private String author;

    @Column(name = "book_year")
    //@Not(message = "Нельзя вводить пустые значения")
    @Min(value = 1800, message = "Рік повинен бути менше ніж 1800")
    @Max(value = 2022, message = "Рік не повинен бути більше 2022")
    private int bookYear;

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @Transient
    private boolean delay;

    public Book() {
    }

    public Book(String title, String author, int bookYear) {
        this.title = title;
        this.author = author;
        this.bookYear = bookYear;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBookYear() {
        return bookYear;
    }

    public void setBookYear(int bookYear) {
        this.bookYear = bookYear;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakeAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isDelay() {
        return delay;
    }

    public void setDelay(boolean delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "Book{" +
                "owner="+owner+
                ", title='" + title + '\'' +
                '}';
    }
}
