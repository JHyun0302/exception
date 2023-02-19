package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ExceptionHandler: API 방식 예외 처리
 * @RestControllerAdvice: 정상 코드와 예외 처리 코드 분리
 */
@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api") //package:api 에 적용(V2, V3 모두 적용됨)
public class ExControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST) //200 -> 400
    @ExceptionHandler(IllegalArgumentException.class)
    //controller에서 IllegalArgumentException 발생시 아래 로직 실행(@RestController라서 json으로 반환)
    public ErrorResult illegalExHandle(IllegalArgumentException e) { //ill~ & 그 자식들 에러 잡음
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());//정상 흐름으로 반환!(200)
    }

    @ExceptionHandler //(UserException.class) 아래(userExHandle(UserException e)) 같은 경우 생략가능
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {//User~ & 그 자식들 에러 잡음
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) { //위에 Ill~ & User~에서 못 잡은 에러들 여기서 잡음(공통 처리)
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
