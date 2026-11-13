package com.kh.logdemo.common.filter;

import static net.logstash.logback.argument.StructuredArguments.value;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 요청마다 접근 로그 한 줄을 남기는 필터입니다.
 *
 * 1) 요청이 들어오면 traceId를 만들어 MDC에 넣습니다. (이 요청 안의 모든 로그에 자동으로 붙습니다)
 * 2) 요청 처리가 끝나면 메서드, 경로, 상태코드, 처리 시간을 로그로 남깁니다.
 *
 * value("필드명", 값)으로 넘긴 값은 메시지에도 들어가고, JSON의 별도 필드로도 들어갑니다.
 * 그래서 Kibana에서 http_status, duration_ms 같은 필드로 집계할 수 있습니다.
 */
@Component
public class AccessLogFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger("access");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        MDC.put("traceId", traceId);
        response.setHeader("X-Trace-Id", traceId);

        long start = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("{} {} {} {}ms",
                    value("http_method", request.getMethod()),
                    value("http_path", request.getRequestURI()),
                    value("http_status", response.getStatus()),
                    value("duration_ms", duration));
            MDC.remove("traceId");
        }
    }
}
