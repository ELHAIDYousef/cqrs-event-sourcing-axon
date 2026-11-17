package ma.enset.accountservice.commands.dto;

import java.time.Instant;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountEventResponseDTO(String type, long sequenceNumber, Instant timestamp, Object payload) {
}
