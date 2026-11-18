package ma.enset.accountservice.query.handlers;

import ma.enset.accountservice.enums.OperationType;
import ma.enset.accountservice.events.AccountActivatedEvent;
import ma.enset.accountservice.events.AccountCreatedEvent;
import ma.enset.accountservice.events.AccountCreditedEvent;
import ma.enset.accountservice.events.AccountDebitedEvent;
import ma.enset.accountservice.events.AccountStatusUpdatedEvent;
import ma.enset.accountservice.query.entities.Account;
import ma.enset.accountservice.query.dto.AccountOperationResponseDTO;
import ma.enset.accountservice.query.entities.AccountOperation;
import ma.enset.accountservice.query.queries.WatchEventQuery;
import ma.enset.accountservice.query.repositories.AccountOperationRepository;
import ma.enset.accountservice.query.repositories.AccountRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yousef ELHAID
 */
@Component
public class AccountEventHandler {

    private final AccountRepository accountRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public AccountEventHandler(AccountRepository accountRepository,
                               AccountOperationRepository accountOperationRepository,
                               QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    @Transactional
    public void on(AccountCreatedEvent event, EventMessage<AccountCreatedEvent> message) {
        Account account = Account.builder()
                .id(event.accountId())
                .balance(event.initialBalance())
                .currency(event.currency())
                .status(event.status())
                .createdAt(message.getTimestamp())
                .build();
        accountRepository.save(account);
    }

    @EventHandler
    @Transactional
    public void on(AccountActivatedEvent event) {
        Account account = findAccount(event.accountId());
        account.setStatus(event.status());
        accountRepository.save(account);
    }

    @EventHandler
    @Transactional
    public void on(AccountStatusUpdatedEvent event) {
        Account account = findAccount(event.accountId());
        account.setStatus(event.status());
        accountRepository.save(account);
    }

    @EventHandler
    @Transactional
    public void on(AccountCreditedEvent event, EventMessage<AccountCreditedEvent> message) {
        Account account = findAccount(event.accountId());
        AccountOperation operation = saveOperation(account, OperationType.CREDIT, event.amount(), event.currency(), message);
        emitOperation(event.accountId(), operation);
        account.setBalance(account.getBalance() + event.amount());
        accountRepository.save(account);
    }

    @EventHandler
    @Transactional
    public void on(AccountDebitedEvent event, EventMessage<AccountDebitedEvent> message) {
        Account account = findAccount(event.accountId());
        AccountOperation operation = saveOperation(account, OperationType.DEBIT, event.amount(), event.currency(), message);
        emitOperation(event.accountId(), operation);
        account.setBalance(account.getBalance() - event.amount());
        accountRepository.save(account);
    }

    private Account findAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException("Account not found in read model: " + accountId));
    }

    private AccountOperation saveOperation(Account account, OperationType type, double amount, String currency,
                               EventMessage<?> message) {
        AccountOperation operation = AccountOperation.builder()
                .date(message.getTimestamp())
                .amount(amount)
                .type(type)
                .currency(currency)
                .account(account)
                .build();
        return accountOperationRepository.save(operation);
    }

    private void emitOperation(String accountId, AccountOperation operation) {
        AccountOperationResponseDTO operationDto = new AccountOperationResponseDTO(operation.getId(),
                operation.getDate(), operation.getAmount(), operation.getType(), operation.getCurrency(), accountId);
        queryUpdateEmitter.emit(WatchEventQuery.class, query -> query.accountId().equals(accountId), operationDto);
    }
}
