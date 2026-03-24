package uz.asadbek.subcourse.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class SpaRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();

        boolean isApi = uri.startsWith("/v1/api") || uri.startsWith("/v3")|| uri.startsWith("/api");
        boolean isAsset = uri.startsWith("/assets");
        boolean isStaticFile = uri.contains(".");
        boolean isIndex = uri.equals("/") || uri.equals("/index.html");

        if (request.getMethod().equals("GET")
            && !isApi
            && !isAsset
            && !isStaticFile
            && !isIndex) {

            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }

        chain.doFilter(req, res);
    }

}
