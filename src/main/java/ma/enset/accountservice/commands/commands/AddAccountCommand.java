package ma.enset.accountservice.commands.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Yousef ELHAID
 */
public record AddAccountCommand(@TargetAggregateIdentifier String id, double initialBalance, String currency) {
}
