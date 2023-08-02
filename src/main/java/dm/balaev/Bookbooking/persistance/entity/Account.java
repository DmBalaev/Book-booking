package dm.balaev.Bookbooking.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@Builder
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "account_sequence",
            strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @OneToMany(mappedBy = "account", cascade = CascadeType.PERSIST)
    private Collection<Borrowing> borrowings;
}
