package it.nerr.buff163updater.util;

import java.time.LocalDateTime;

public record DataPoint<T>(T data, LocalDateTime timestamp) {

}
