package ma.enset.accountservice.query.dto;

import java.time.Instant;
import ma.enset.accountservice.enums.AccountStatus;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountResponseDTO(String id, Instant createdAt, double balance, String currency,
                                 AccountStatus status) {
}
