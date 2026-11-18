package ma.enset.accountservice.query.repositories;

import ma.enset.accountservice.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Yousef ELHAID
 */
public interface AccountRepository extends JpaRepository<Account, String> {
}
