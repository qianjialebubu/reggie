package com.bjpowernode.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.bjpowernode.reggie.common.BaseContext;
import com.bjpowernode.reggie.common.R;
import com.bjpowernode.reggie.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author qjl
 * @create 2022-09-22 13:43
 */
//检测是否登录(暂时拦截所有的请求)
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
//    路径匹配器
    public static final AntPathMatcher PATH_MATCHER= new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        强转类型

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
//        配置不处理的请求
        log.info("拦截到：{}",requestURI);
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                "/front/page"

        };
//        判断是否需要处理
        boolean check = check(urls, requestURI);
        if (check) {
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request, response);
            return;
        }
//        需要处理,判断是否登录，已经登录直接放行
        if (request.getSession().getAttribute("employee")!=null) {
            log.info("用户已经登录，用户id是{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            Long empId = (Long) request.getSession().getAttribute("employee");
            System.out.println("________________________{empId=}______________"+empId);
            BaseContext.setCurrentId(empId);
            return;
        }

//        判断移动端用户的登录状态
        if (request.getSession().getAttribute("user")!=null) {
            log.info("用户已经登录，用户id是{}",request.getSession().getAttribute("user"));
            filterChain.doFilter(request, response);
            Long userId = (Long) request.getSession().getAttribute("user");
//            System.out.println("________________________{userid=}______________"+userid);
            BaseContext.setCurrentId(userId);
            return;
        }
//        未登录，进行页面的跳转
//        通过输出流的方式向客户端页面进行响应
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        log.info("{}未登录，即将进行页面的跳转",requestURI);
        return;
////        输出日志
//        log.info("拦截请求{}",request.getRequestURI());
////        拦截放行
//        filterChain.doFilter(request,response);
    }

    /**
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI){
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;

    }
}
