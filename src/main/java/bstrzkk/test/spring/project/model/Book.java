package bstrzkk.test.spring.project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }
}
