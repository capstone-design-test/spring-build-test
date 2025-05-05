package hello.springmvc.basic.request;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {
    // **메세지 바디**를 직접 조회하는 기능은 **요청 파라미터**를 조회하는 @RequestParam, @ModelAttribute 와는 전혀 관계없다.

    @PostMapping("/request-body-string-v1")
    // 참고로 Get Method 에도 Body 에 데이터를 넣을 수 있다. (최근 스펙, 실무에선 지양)
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");
    }

    @PostMapping("/request-body-string-v2")
    // 참고로 Get Method 에도 Body 에 데이터를 넣을 수 있다. (최근 스펙, 실무에선 지양)
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }

    // HttpMessageConverter
    @PostMapping("/request-body-string-v3")
    // 참고로 Get Method 에도 Body 에 데이터를 넣을 수 있다. (최근 스펙, 실무에선 지양)
//    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
    public HttpEntity<String> requestBodyStringV3(RequestEntity<String> httpEntity) throws IOException {
        // HttpEntity -> 메시지 바디 정보 직접 반환, 헤더 정보 포함 가능, view 조회 X
        // HttpEntity 를 상속받은 RequestEntity, ResponseEntity 도 존재한다.

        String messageBody = httpEntity.getBody();
        HttpHeaders headers = httpEntity.getHeaders();
        log.info("messageBody={}", messageBody);

//        return new HttpEntity<String>("ok");
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody,
                                      @RequestHeader(value = "mycustom", required = false) String header
    ) throws IOException {

        log.info("messageBody={}", messageBody);
        log.info("header={}", header);
        return "ok";
    }
}
