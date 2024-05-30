package org.example.simpledms.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : kangtaegyung
 * @fileName : ExControllerAdvice
 * @since : 24. 5. 27.
 * description : 공통된 예외 처리 함수
 *  @ResponseStatus(HttpStatus.상태코드) : 상태코드가 발생하면
 *  @ExceptionHandler(Exception.class) : Exception 예외 클래스에 대해 처리한다.
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionAdvice {

//  컨트롤러에서 어떤 에러가 발생하더라도 이 함수가 실행됨
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> internalServerErrorException(Exception e) {
        log.debug("벡엔드 에러: " + e.getMessage());

        return new ResponseEntity<>("벡엔드 에러: " + e.getMessage()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}