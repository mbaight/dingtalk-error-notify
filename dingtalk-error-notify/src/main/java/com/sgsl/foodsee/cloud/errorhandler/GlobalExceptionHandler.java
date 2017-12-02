package com.sgsl.foodsee.cloud.errorhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.ImmutableMap;
import com.sgsl.foodsee.cloud.dingtalk.DingTalkErrorNotifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuyo - 3/15
 * @author maoxianzhi 4/7
 * @author maoxianzhi 7/10
 * @author maoxianzhi 10/19
 * @version 1.0
 * @apiNote 加入钉钉通知
 */


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String USER_AGENT_HEADER_NAME = "User-Agent";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DingTalkErrorNotifier dingTalkErrorNotifier;

    public GlobalExceptionHandler(DingTalkErrorNotifier dingTalkErrorNotifier) {
        this.dingTalkErrorNotifier = dingTalkErrorNotifier;
        log.debug("load GlobalExceptionHandler ");
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity constraintViolationException(HttpServletRequest request, ConstraintViolationException exception) throws JsonProcessingException {
        List<Map<String, Object>> errors = exception.getConstraintViolations().stream()
                .map(error -> ImmutableMap.of(
                        "message", error.getMessage(),
                        "rejectedValue", error.getInvalidValue(),
                        "userAgent", USER_AGENT_HEADER_NAME
                        )
                ).collect(Collectors.toList());

        log.error(errors.toString(), exception);

        String requestURI = request.getRequestURI();
        dingTalkErrorNotifier.notify(NotifyData.builder()
                .message(objectMapper.writeValueAsString(errors))
                .exception(exception)
                .requestUrl(requestURI)
                .build());

        return ResponseEntity.badRequest().body(ImmutableMap.of(
                "requestUrl", requestURI,
                "errors", errors
        ));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidHandler(HttpServletRequest request, MethodArgumentNotValidException exception) throws JsonProcessingException {
        String requestURI = request.getRequestURI();
        List<Map<String, Object>> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(error -> ImmutableMap.of(
                        "paramName", error.getField(),
                        "message", error.getDefaultMessage(),
                        "rejectedValue", error.getRejectedValue() != null ? error.getRejectedValue() : "",
                        "userAgent", USER_AGENT_HEADER_NAME
                        )
                ).collect(Collectors.toList());

        log.error(errors.toString(), exception);

        dingTalkErrorNotifier.notify(NotifyData.builder()
                .message(objectMapper.writeValueAsString(errors))
                .exception(exception)
                .requestUrl(requestURI)
                .build());

        return ResponseEntity.badRequest().body(ImmutableMap.of(
                "requestUrl", requestURI,
                "errors", errors
        ));
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity missingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException exception) {
        String requestURI = request.getRequestURI();

        log.error("MissingServletRequestParameterException: url:{}, exception:{}, ParameterName:{},ParameterType:{}, userAgent:{}  ",
                requestURI,
                exception.getMessage(),
                exception.getParameterName(),
                exception.getParameterType(),
                request.getHeader(USER_AGENT_HEADER_NAME), exception);

        dingTalkErrorNotifier.notify(NotifyData.builder()
                .exception(exception)
                .requestUrl(requestURI)
                .build());

        return ResponseEntity.badRequest().body(ImmutableMap.of(
                "RequestURI", requestURI,
                "Message", exception.getMessage(),
                "ParameterName", exception.getParameterName(),
                "ParameterType", exception.getParameterType()
        ));
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity methodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException exception) {
        String requestURI = request.getRequestURI();


        log.error("MethodArgumentTypeMismatchException: url:{}, exception:{}, name:{},parameter:{}, RequiredType:{}, PropertyName:{}, Value:{}, userAgent:{}   ",
                requestURI,
                exception.getMessage(),
                exception.getName(),
                exception.getParameter(),
                exception.getRequiredType(),
                exception.getPropertyName(),
                exception.getValue(),
                request.getHeader(USER_AGENT_HEADER_NAME), exception);

        dingTalkErrorNotifier.notify(NotifyData.builder()
                .exception(exception)
                .requestUrl(requestURI)
                .build());

        return ResponseEntity.badRequest().body(ImmutableMap.builder()
                .put("RequestURI", requestURI)
                .put("Message", exception.getMessage())
                .put("Name", exception.getName())
                .put("Parameter", exception.getParameter())
                .put("RequiredType", exception.getRequiredType())
//                .put("PropertyName", exception.getPropertyName())
                .put("Value", exception.getValue())
                .build()
        );
    }

    @ExceptionHandler(value = ConversionFailedException.class)
    public ResponseEntity conversionFailedException(HttpServletRequest request, ConversionFailedException exception) {
        String requestURI = request.getRequestURI();

        log.error("ConversionFailedException: url:{}, Exception:{}, SourceType:{},TargetType:{}, value:{}, userAgent:{}   ",
                requestURI,
                exception.getMessage(),
                exception.getSourceType(),
                exception.getTargetType(),
                exception.getValue(),
                request.getHeader(USER_AGENT_HEADER_NAME), exception);

        dingTalkErrorNotifier.notify(NotifyData.builder()
                .message(exception.getValue().toString())
                .exception(exception)
                .requestUrl(requestURI)
                .build());

        return ResponseEntity.badRequest().body(ImmutableMap.builder()
                .put("RequestURI", requestURI)
                .put("Message", exception.getMessage())
                .put("SourceType", exception.getSourceType())
                .put("TargetType", exception.getTargetType())
                .put("Value", exception.getValue())
                .build()
        );
    }

    @ExceptionHandler(value = InvalidFormatException.class)
    public ResponseEntity invalidFormatException(HttpServletRequest request, InvalidFormatException exception) {
        String requestURI = request.getRequestURI();
        log.error("InvalidFormatException: url:{}, Exception:{}, PathReference:{},TargetType:{}, value:{}, userAgent:{}   ",
                requestURI,
                exception.getMessage(),
                exception.getPathReference(),
                exception.getTargetType(),
                exception.getValue(),
                request.getHeader(USER_AGENT_HEADER_NAME), exception);

        dingTalkErrorNotifier.notify(NotifyData.builder()
                .exception(exception)
                .requestUrl(requestURI)
                .build());

        return ResponseEntity.badRequest().body(ImmutableMap.builder()
                .put("RequestURI", requestURI)
                .put("Message", exception.getMessage())
                .put("PathReference", exception.getPathReference())
                .put("TargetType", exception.getTargetType())
                .put("Value", exception.getValue())
                .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException exception) {
        String requestURI = request.getRequestURI();
        log.error("{}: url:{}, Exception:HttpMessageNotReadableException,  userAgent:{}  ",
                requestURI,
                exception.getMessage(),
                request.getHeader(USER_AGENT_HEADER_NAME), exception);

        dingTalkErrorNotifier.notify(NotifyData.builder()
                .exception(exception)
                .requestUrl(requestURI)
                .build());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ImmutableMap.builder()
                .put("RequestURI", requestURI)
                .put("Message", exception.getMessage())
                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity generatorException(HttpServletRequest request, Exception exception) {
        String requestURI = request.getRequestURI();
        String message = exception.getMessage();
        if (StringUtils.isEmpty(message)){
            message = exception.getClass().getName();
        }

        log.error("{}: url:{}, Exception:{},  userAgent:{}  ",
                exception.getClass().getName(),
                requestURI,
                message,
                request.getHeader(USER_AGENT_HEADER_NAME), exception);

        dingTalkErrorNotifier.notify(NotifyData.builder()
                .exception(exception)
                .requestUrl(requestURI)
                .build());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ImmutableMap.builder()
                .put("RequestURI", requestURI)
                .put("Message", message)
                .build()
        );
    }

}
