package ma.enset.accountservice.query.handlers;

import java.util.List;
import ma.enset.accountservice.query.dto.AccountOperationResponseDTO;
import ma.enset.accountservice.query.dto.AccountResponseDTO;
import ma.enset.accountservice.query.dto.AccountStatementResponseDTO;
import ma.enset.accountservice.query.entities.Account;
import ma.enset.accountservice.query.entities.AccountOperation;
import ma.enset.accountservice.query.queries.GetAccountStatementQuery;
import ma.enset.accountservice.query.queries.GetAllAccountsQuery;
import ma.enset.accountservice.query.repositories.AccountOperationRepository;
import ma.enset.accountservice.query.repositories.AccountRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Yousef ELHAID
 */
@Component
public class AccountQueryHandler {

    private final AccountRepository accountRepository;
    private final AccountOperationRepository accountOperationRepository;

    public AccountQueryHandler(AccountRepository accountRepository,
                               AccountOperationRepository accountOperationRepository) {
        this.accountRepository = accountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }

    @QueryHandler
    public List<AccountResponseDTO> on(GetAllAccountsQuery query) {
        return accountRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query) {
        Account account = accountRepository.findById(query.accountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + query.accountId()));
        List<AccountOperationResponseDTO> operations = accountOperationRepository
                .findByAccountIdOrderByDateAsc(query.accountId()).stream()
                .map(this::toDto)
                .toList();
        return new AccountStatementResponseDTO(toDto(account), operations);
    }

    private AccountResponseDTO toDto(Account account) {
        return new AccountResponseDTO(account.getId(), account.getCreatedAt(), account.getBalance(),
                account.getCurrency(), account.getStatus());
    }

    private AccountOperationResponseDTO toDto(AccountOperation operation) {
        return new AccountOperationResponseDTO(operation.getId(), operation.getDate(), operation.getAmount(),
                operation.getType(), operation.getCurrency(), operation.getAccount().getId());
    }
}
