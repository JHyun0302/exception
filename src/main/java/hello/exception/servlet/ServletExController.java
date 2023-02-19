package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * response.sendError(HTTP 상태 코드, 오류 메시지);  -> http 오류 코드 & 오류 메시지 설정 가능
 * <p>
 * "WebServerCustomizer" - @Component 주석처리: 스프링이 제공하는 BasicErrorController 사용가능!!
 * <p>
 * BasicErrorController는 경로에 맞게 오류 페이지만 넣어주고 mapping 시키면 됨!
 */
@Slf4j
@Controller
public class ServletExController {
    @GetMapping("/error-ex")
    public void errorEx() { //1.controller에서 예외 발생하면 쭉~ 가서 WAS(WebServerCustomizer)까지 전달됨
        throw new RuntimeException("예외 발생!");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류!"); //.sendEror(HTTP 상태 코드, 오류 메시지)
    }

    @GetMapping("/error-400")
    public void error400(HttpServletResponse response) throws IOException {
        response.sendError(400, "400 오류!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500, "500 오류!");
    }
}
