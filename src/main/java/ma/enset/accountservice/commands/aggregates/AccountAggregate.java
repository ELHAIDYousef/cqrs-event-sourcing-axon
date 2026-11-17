package ma.enset.accountservice.commands.aggregates;

import lombok.extern.slf4j.Slf4j;
import ma.enset.accountservice.commands.commands.AddAccountCommand;
import ma.enset.accountservice.enums.AccountStatus;
import ma.enset.accountservice.events.AccountActivatedEvent;
import ma.enset.accountservice.events.AccountCreatedEvent;
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
}
