package WebStoreGroup.WebStore.Exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public class ExceptionResponse {
    private String message;
    private List<String> messages;
    private String detail;
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;


    public ExceptionResponse(List<String> messages, String detail,HttpStatus httpStatus, LocalDateTime timestamp) {
        this.messages = messages;
        this.detail=detail;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public ExceptionResponse(String message, List<String> messages, HttpStatus httpStatus, LocalDateTime timestamp) {
        this.message = message;
        this.messages = messages;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public ExceptionResponse(String message, HttpStatus httpStatus, LocalDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    //    public ExceptionResponse(String message, Throwable detail, HttpStatus httpStatus, ZonedDateTime timestamp) {
//        this.message = message;
//        this.detail = detail;
//        this.httpStatus = httpStatus;
//        this.timestamp = timestamp;
//    }
}
