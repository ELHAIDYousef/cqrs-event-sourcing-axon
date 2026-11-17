package ma.enset.accountservice.commands.aggregates;

import lombok.extern.slf4j.Slf4j;
import ma.enset.accountservice.commands.commands.AddAccountCommand;
import ma.enset.accountservice.commands.commands.CreditAccountCommand;
import ma.enset.accountservice.commands.commands.DebitAccountCommand;
import ma.enset.accountservice.enums.AccountStatus;
import ma.enset.accountservice.events.AccountActivatedEvent;
import ma.enset.accountservice.events.AccountCreatedEvent;
import ma.enset.accountservice.events.AccountCreditedEvent;
import ma.enset.accountservice.events.AccountDebitedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

/**
 *
 * @author Yousef ELHAID
 */
@Slf4j
@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
    }

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        log.info("AddAccountCommand received for account {}", command.id());
        if (command.initialBalance() <= 0) {
            throw new IllegalArgumentException("Initial balance must be positive");
        }
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.id(), command.initialBalance(), command.currency(), AccountStatus.CREATED));
        AggregateLifecycle.apply(new AccountActivatedEvent(command.id(), AccountStatus.ACTIVATED));
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        log.info("CreditAccountCommand received for account {}", command.id());
        if (status != AccountStatus.ACTIVATED) {
            throw new IllegalStateException("Account is not activated");
        }
        if (command.amount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        AggregateLifecycle.apply(new AccountCreditedEvent(command.id(), command.amount(), command.currency()));
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        log.info("DebitAccountCommand received for account {}", command.id());
        if (status != AccountStatus.ACTIVATED) {
            throw new IllegalStateException("Account is not activated");
        }
        if (command.amount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (balance < command.amount()) {
            throw new IllegalStateException("Balance insufficient");
        }
        AggregateLifecycle.apply(new AccountDebitedEvent(command.id(), command.amount(), command.currency()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.accountId();
        this.balance = event.initialBalance();
        this.currency = event.currency();
        this.status = event.status();
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        this.status = event.status();
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance += event.amount();
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance -= event.amount();
    }
}
