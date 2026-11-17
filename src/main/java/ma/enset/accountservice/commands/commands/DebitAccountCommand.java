package ma.enset.accountservice.commands.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Yousef ELHAID
 */
public record DebitAccountCommand(@TargetAggregateIdentifier String id, double amount, String currency) {
}
