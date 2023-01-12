package hello.exception;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import hello.exception.resolver.MyHandlerExceptionResolver;
import hello.exception.resolver.UserHandlerExceptionResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.List;

//@Component
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     *  스프링 부트 - 오류페이지 처리(WebServerCustomizer의 @Component 주석처리)
     *  BasicErrorController라는 스프링 컨트롤러가 resources/templates/error의 파일부터 .html 뒤져서 출력(우선순위)
     */

    /**
     * 스프링 인터셉터(LogInterceptor) 등록
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico",
                        "/error", "/error-page/**"); //오류 페이지 경로
    }

    /**
     * HandlerExceptionResolver 등록
     */

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new MyHandlerExceptionResolver());
        resolvers.add(new UserHandlerExceptionResolver());
    }

    /**
     * 서블릿 필터(LogFilter) 등록
     */
//    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        //이 필터는 Request(클라이언트 정상요청), Error(오류 페이지 출력을 위한 내부요청) 2가지 경우에 호출이 됩니다!
        return filterRegistrationBean;

    }
}
