package com.hz.demo.core;

import com.hz.demo.model.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * session 超时跳转类
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();
        if (requestURI.endsWith(".html")) {
            return true;
        }
        sys_user userName = (sys_user) session.getAttribute("User");
        if (requestURI.contains("upload")) {//访问文件
            return true;
        }
        if (requestURI.contains("PreviewCertificatePdf/getCertificate")) {//二维码访问证书
            return true;
        }
        if (userName == null || requestURI.endsWith(".jsp")) {
            //不符合条件的，跳转到登录界面
            String CONTENT_TYPE = "text/html; charset=GBK";
            response.setContentType(CONTENT_TYPE);
            java.io.PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<script>");
            out.println("window.open ('" + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/User/baseLogin.do','_top')");
            out.println("</script>");
            out.println("</html>");
            out.close();
            return false;
        }
        return true;
    }
}
