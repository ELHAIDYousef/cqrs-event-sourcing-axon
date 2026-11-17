package ma.enset.accountservice.commands.commands;

import ma.enset.accountservice.enums.AccountStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Yousef ELHAID
 */
public record UpdateAccountStatusCommand(@TargetAggregateIdentifier String id, AccountStatus status) {
}
