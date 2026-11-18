package ma.enset.accountservice.query.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.enset.accountservice.enums.AccountStatus;

/**
 *
 * @author Yousef ELHAID
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    private String id;

    private Instant createdAt;

    private double balance;

    private String currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
}
