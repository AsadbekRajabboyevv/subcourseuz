package uz.asadbek.subcourse.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpaRedirectFilter implements Filter {

    private static final String[] LANGS = {"uz", "ru", "en", "uz-Cyrl"};

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();

        boolean isApi = uri.startsWith("/v1/api") || uri.startsWith("/v3") || uri.startsWith("/api");
        boolean isAsset = uri.startsWith("/assets");
        boolean isStaticFile = uri.contains(".");

        if ("GET".equals(request.getMethod())
            && !isApi
            && !isAsset
            && !isStaticFile) {

            String lang = extractLang(uri);

            if (lang == null) {
                response.sendRedirect("/uz");
                return;
            }

            request.getRequestDispatcher("/" + lang + "/index.html")
                .forward(request, response);
            return;
        }

        chain.doFilter(req, res);
    }

    private String extractLang(String uri) {
        if (uri == null || uri.length() < 3) return null;

        String[] parts = uri.split("/");
        if (parts.length > 1) {
            String first = parts[1];
            for (String lang : LANGS) {
                if (lang.equalsIgnoreCase(first)) {
                    return lang;
                }
            }
        }
        return null;
    }
}
