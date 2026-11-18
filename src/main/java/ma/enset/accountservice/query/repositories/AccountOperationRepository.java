package ma.enset.accountservice.query.repositories;

import java.util.List;
import ma.enset.accountservice.query.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Yousef ELHAID
 */
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

    List<AccountOperation> findByAccountIdOrderByDateAsc(String accountId);
}
