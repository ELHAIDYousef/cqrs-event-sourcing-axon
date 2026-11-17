package ma.enset.accountservice.events;

import ma.enset.accountservice.enums.AccountStatus;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountActivatedEvent(String accountId, AccountStatus status) {
}
