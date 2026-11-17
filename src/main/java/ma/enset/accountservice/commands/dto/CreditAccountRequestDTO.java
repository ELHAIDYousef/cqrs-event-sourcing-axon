package ma.enset.accountservice.commands.dto;

/**
 *
 * @author Yousef ELHAID
 */
public record CreditAccountRequestDTO(String accountId, double amount, String currency) {
}
