package ma.enset.accountservice.events;

import ma.enset.accountservice.enums.AccountStatus;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountStatusUpdatedEvent(String accountId, AccountStatus status) {
}
