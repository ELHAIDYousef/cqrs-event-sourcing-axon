package ma.enset.accountservice.commands.dto;

/**
 *
 * @author Yousef ELHAID
 */
public record DebitAccountRequestDTO(String accountId, double amount, String currency) {
}
