package org.cloris.houses.web.interceptor;

import org.cloris.houses.common.constants.CommonConstants;
import org.cloris.houses.common.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用于权限鉴定的拦截器。
 *
 * @author Jackson Fang
 * Date:   2018/11/8
 * Time:   15:14
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqURI = request.getRequestURI();
//        System.out.println("----request uri: " + reqURI); // 打印拦截器中 request 的 uri。
        // 静态资源请求，无需处理直接返回 true。
        if (reqURI.startsWith("/static") || reqURI.startsWith("/error")) {
            return true;
        }
        // 后台请求
        HttpSession session = request.getSession(true);
        User currentUser = (User) session.getAttribute(CommonConstants.USER_ATTRIBUTE);
        if (currentUser != null) {
            UserContext.setUser(currentUser);
        } else { // currentUser is null.
            response.sendRedirect("/accounts/signIn");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
