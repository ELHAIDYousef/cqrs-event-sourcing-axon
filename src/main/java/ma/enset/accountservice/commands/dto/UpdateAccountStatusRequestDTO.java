package ma.enset.accountservice.commands.dto;

import ma.enset.accountservice.enums.AccountStatus;

/**
 *
 * @author Yousef ELHAID
 */
public record UpdateAccountStatusRequestDTO(String accountId, AccountStatus status) {
}
