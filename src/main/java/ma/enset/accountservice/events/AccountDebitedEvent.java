package ma.enset.accountservice.events;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountDebitedEvent(String accountId, double amount, String currency) {
}
