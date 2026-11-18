package ma.enset.accountservice.query.dto;

import java.util.List;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountStatementResponseDTO(AccountResponseDTO account,
                                          List<AccountOperationResponseDTO> operations) {
}
