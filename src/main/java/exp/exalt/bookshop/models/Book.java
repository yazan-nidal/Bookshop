package exp.exalt.bookshop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Book")
public class Book {
    @NotNull
    @Column(unique=true)
    private long isbn;
    @NotNull
    private String name;
    @NotNull
    @JsonManagedReference
    @ManyToOne
    private Author author;
    @JsonManagedReference
    @ManyToOne
    private Customer customer;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
}
