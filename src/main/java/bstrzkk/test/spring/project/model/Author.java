package bstrzkk.test.spring.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @JsonManagedReference
    @OneToMany(cascade = ALL, mappedBy = "author")
    private List<Book> books;

    public Author(String name, String surname, List<Book> books) {
        this.name = name;
        this.surname = surname;
    }
}
