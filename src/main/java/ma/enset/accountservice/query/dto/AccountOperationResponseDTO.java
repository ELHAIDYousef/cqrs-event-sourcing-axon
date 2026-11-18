package ma.enset.accountservice.query.dto;

import java.time.Instant;
import ma.enset.accountservice.enums.OperationType;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountOperationResponseDTO(Long id, Instant date, double amount, OperationType type,
                                          String currency, String accountId) {
}
