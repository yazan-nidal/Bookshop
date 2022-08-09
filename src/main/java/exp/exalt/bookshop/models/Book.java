package exp.exalt.bookshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Book")
public class Book implements Serializable {
    @NotNull
    @Column(unique=true)
    private long isbn;
    @NotNull
    private String name;
    @NotNull
    @JsonBackReference("author-book")
    @ManyToOne(cascade = { })
    private Author author;
    @JsonBackReference("customer-book")
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE})
    private Customer customer;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
}
