package ma.enset.accountservice.commands.aggregates;

import ma.enset.accountservice.commands.commands.AddAccountCommand;
import ma.enset.accountservice.commands.commands.CreditAccountCommand;
import ma.enset.accountservice.commands.commands.DebitAccountCommand;
import ma.enset.accountservice.commands.commands.UpdateAccountStatusCommand;
import ma.enset.accountservice.enums.AccountStatus;
import ma.enset.accountservice.events.AccountActivatedEvent;
import ma.enset.accountservice.events.AccountCreatedEvent;
import ma.enset.accountservice.events.AccountCreditedEvent;
import ma.enset.accountservice.events.AccountDebitedEvent;
import ma.enset.accountservice.events.AccountStatusUpdatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Yousef ELHAID
 */
class AccountAggregateTest {

    private static final String ID = "acc-1";
    private static final String MAD = "MAD";

    private FixtureConfiguration<AccountAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(AccountAggregate.class);
    }

    private AccountCreatedEvent created() {
        return new AccountCreatedEvent(ID, 5000, MAD, AccountStatus.CREATED);
    }

    private AccountActivatedEvent activated() {
        return new AccountActivatedEvent(ID, AccountStatus.ACTIVATED);
    }

    @Test
    void shouldCreateAndActivateAccountWhenInitialBalanceIsPositive() {
        fixture.givenNoPriorActivity()
                .when(new AddAccountCommand(ID, 5000, MAD))
                .expectSuccessfulHandlerExecution()
                .expectEvents(created(), activated());
    }

    @Test
    void shouldRejectAccountCreationWhenInitialBalanceIsNegative() {
        fixture.givenNoPriorActivity()
                .when(new AddAccountCommand(ID, -100, MAD))
                .expectException(IllegalArgumentException.class)
                .expectExceptionMessage("Initial balance must be positive");
    }

    @Test
    void shouldCreditActivatedAccount() {
        fixture.given(created(), activated())
                .when(new CreditAccountCommand(ID, 1000, MAD))
                .expectEvents(new AccountCreditedEvent(ID, 1000, MAD));
    }

    @Test
    void shouldRejectCreditWhenAccountIsSuspended() {
        fixture.given(created(), activated(), new AccountStatusUpdatedEvent(ID, AccountStatus.SUSPENDED))
                .when(new CreditAccountCommand(ID, 1000, MAD))
                .expectException(IllegalStateException.class)
                .expectExceptionMessage("Account is not activated");
    }

    @Test
    void shouldRejectCreditWhenAmountIsNegative() {
        fixture.given(created(), activated())
                .when(new CreditAccountCommand(ID, -50, MAD))
                .expectException(IllegalArgumentException.class)
                .expectExceptionMessage("Amount must be positive");
    }

    @Test
    void shouldDebitActivatedAccountWhenBalanceIsSufficient() {
        fixture.given(created(), activated())
                .when(new DebitAccountCommand(ID, 2000, MAD))
                .expectEvents(new AccountDebitedEvent(ID, 2000, MAD));
    }

    @Test
    void shouldRejectDebitWhenBalanceIsInsufficient() {
        fixture.given(created(), activated())
                .when(new DebitAccountCommand(ID, 9000, MAD))
                .expectException(IllegalStateException.class)
                .expectExceptionMessage("Balance insufficient");
    }

    @Test
    void shouldUpdateAccountStatus() {
        fixture.given(created(), activated())
                .when(new UpdateAccountStatusCommand(ID, AccountStatus.SUSPENDED))
                .expectEvents(new AccountStatusUpdatedEvent(ID, AccountStatus.SUSPENDED));
    }

    @Test
    void shouldRejectStatusUpdateWhenStatusIsUnchanged() {
        fixture.given(created(), activated())
                .when(new UpdateAccountStatusCommand(ID, AccountStatus.ACTIVATED))
                .expectException(IllegalStateException.class)
                .expectExceptionMessage("Account is already ACTIVATED");
    }
}
