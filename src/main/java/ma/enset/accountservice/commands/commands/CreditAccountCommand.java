package ma.enset.accountservice.commands.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 *
 * @author Yousef ELHAID
 */
public record CreditAccountCommand(@TargetAggregateIdentifier String id, double amount, String currency) {
}
