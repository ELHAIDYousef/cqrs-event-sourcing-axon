package ma.enset.accountservice.query.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import ma.enset.accountservice.query.dto.AccountOperationResponseDTO;
import ma.enset.accountservice.query.dto.AccountResponseDTO;
import ma.enset.accountservice.query.dto.AccountStatementResponseDTO;
import ma.enset.accountservice.query.queries.GetAccountStatementQuery;
import ma.enset.accountservice.query.queries.GetAllAccountsQuery;
import ma.enset.accountservice.query.queries.WatchEventQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 *
 * @author Yousef ELHAID
 */
@RestController
@RequestMapping("/query/accounts")
public class AccountQueryController {

    private final QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public CompletableFuture<List<AccountResponseDTO>> getAllAccounts() {
        return queryGateway.query(new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(AccountResponseDTO.class));
    }

    @GetMapping("/accountStatement/{accountId}")
    public CompletableFuture<AccountStatementResponseDTO> getAccountStatement(@PathVariable String accountId) {
        return queryGateway.query(new GetAccountStatementQuery(accountId),
                ResponseTypes.instanceOf(AccountStatementResponseDTO.class));
    }

    @GetMapping(value = "/watch/{accountId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountOperationResponseDTO> watch(@PathVariable String accountId) {
        SubscriptionQueryResult<AccountOperationResponseDTO, AccountOperationResponseDTO> result =
                queryGateway.subscriptionQuery(new WatchEventQuery(accountId),
                        ResponseTypes.instanceOf(AccountOperationResponseDTO.class),
                        ResponseTypes.instanceOf(AccountOperationResponseDTO.class));
        return result.initialResult().concatWith(result.updates());
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
