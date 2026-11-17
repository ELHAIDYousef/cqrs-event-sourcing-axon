package ma.enset.accountservice.commands.controllers;

import ma.enset.accountservice.commands.commands.AddAccountCommand;
import ma.enset.accountservice.commands.commands.CreditAccountCommand;
import ma.enset.accountservice.commands.commands.DebitAccountCommand;
import ma.enset.accountservice.commands.commands.UpdateAccountStatusCommand;
import ma.enset.accountservice.commands.dto.AddAccountRequestDTO;
import ma.enset.accountservice.commands.dto.CreditAccountRequestDTO;
import ma.enset.accountservice.commands.dto.DebitAccountRequestDTO;
import ma.enset.accountservice.commands.dto.UpdateAccountStatusRequestDTO;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Yousef ELHAID
 */
@RestController
@RequestMapping("/commands/accounts")
public class AccountCommandController {

    private final CommandGateway commandGateway;

    public AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/add")
    public CompletableFuture<String> addAccount(@RequestBody AddAccountRequestDTO request) {
        return commandGateway.send(new AddAccountCommand(
                UUID.randomUUID().toString(),
                request.initialBalance(),
                request.currency()));
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request) {
        return commandGateway.send(new CreditAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()));
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request) {
        return commandGateway.send(new DebitAccountCommand(
                request.accountId(),
                request.amount(),
                request.currency()));
    }

    @PutMapping("/updateStatus")
    public CompletableFuture<String> updateStatus(@RequestBody UpdateAccountStatusRequestDTO request) {
        return commandGateway.send(new UpdateAccountStatusCommand(
                request.accountId(),
                request.status()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        Throwable root = exception;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return ResponseEntity.internalServerError().body(root.getMessage());
    }
}
