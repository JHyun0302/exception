package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /**
     * IllegalArgumentException 발생시 illegalExHandler() 실행
     *
     * @RestController 이므로 그대로 화면에 찍힘!
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //200 -> 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.info("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage()); //정상 흐름으로 반환(200 ok)
    }

    /**
     * 애노테이션 방법과 달리 ResponseEntity 사용하면 HTTP 응답 코드를 프로그래밍해서 동적으로 변경 가능
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.info("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * illegalExHandler(), userExHandler()가 처리하지 못한 나머지 에러들 처리 (ex. RuntimeException)
     */

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500으로 응답
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.info("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }

        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
