package ma.enset.accountservice.events;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountCreditedEvent(String accountId, double amount, String currency) {
}
