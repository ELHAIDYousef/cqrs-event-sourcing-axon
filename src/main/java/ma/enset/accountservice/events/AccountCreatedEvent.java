package ma.enset.accountservice.events;

import ma.enset.accountservice.enums.AccountStatus;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountCreatedEvent(String accountId, double initialBalance, String currency, AccountStatus status) {
}
