package rhjava.erpnext.demo.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);
        // Autoriser l'accès à la page login et aux fichiers statiques
        if (uri.equals("/") || uri.equals("/login") || uri.equals("/logout") ||
            uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images")) {
            return true;
        }
        // Vérifie si la session contient erpSession
        if (session != null && session.getAttribute("erpSession") != null) {
            return true;
        }
        // Sinon, redirige vers la page de connexion
        response.sendRedirect("/");
        return false;
    }
}

