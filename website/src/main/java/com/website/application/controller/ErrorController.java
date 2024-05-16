//package com.website.application.controller;
//
//import com.website.framework.common.GlobalApplicationException;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
//import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.PrintWriter;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//public class ErrorController extends AbstractErrorController{
//    Log log = LogFactory.getLog(ErrorController.class);
//
//    public ErrorController(){
//        super(new DefaultErrorAttributes());
//    }
//
//    @RequestMapping("/error")
//    public ModelAndView getErrorPath(HttpServletRequest request, HttpServletResponse response) {
//        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request,
//                isIncludeStackTrace(request, MediaType.TEXT_HTML)));
//        //获取异常
//        Throwable cause = getCause(request);
//        int status = (Integer) model.get("status");
//        //错误信息
//        String message = (String) model.get("message");
//        //前台友好提示信息
//        String errorMessage = getErrorMessage(cause);
//        //后台打印日志方便查看
//        log.info(status+","+message, cause);
//        response.setStatus(status);
//        if(isJsonRequest(request)){
//            Map error = new HashMap();
//            error.put("success", false);
//            error.put("errorMessage", errorMessage);
//            error.put("message", message);
//            writeJson(response, error);
//            return null;
//        } else {
//            ModelAndView view = new ModelAndView("errorpage");
//            view.addAllObjects(model);
//            view.addObject("errorMessage", errorMessage);
//            view.addObject("status",status);
//            view.addObject("cause",cause);
//            return view;
//        }
//
//    }
//
//    protected  Throwable getCause(HttpServletRequest request) {
//        Throwable error = (Throwable) request.getAttribute("javax.servlet.error.exception");
//        if(error != null){
//            //MVC有可能会封装异常成ServletException，需要调用getCause获取真正的异常
//            while(error instanceof ServletException && error.getCause() != null){
//                error = ((ServletException)error).getCause();
//            }
//        }
//        return error;
//    }
//
//    protected String getErrorMessage(Throwable ex) {
//        if(ex instanceof GlobalApplicationException){
//            return ((GlobalApplicationException)ex).getMessage();
//        }
//        return "服务器错误，请联系管理员";
//    }
//
//    protected boolean isJsonRequest(HttpServletRequest request){
//        return false;
//    }
//
//    protected void writeJson(HttpServletResponse response, Object data) {
//        try{
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("text/html;charset=utf-8");
//            response.setHeader("Cache-Control", "no-cache");
//            PrintWriter out = response.getWriter(); //输出中文，这一句一定要放到response.setContentType("text/html;charset=utf-8"),  response.setHeader("Cache-Control", "no-cache")后面，否则中文返回到页面是乱码
//            out.print(data);
//            out.flush();
//            out.close();
//        } catch (Exception ex) {
//
//        }
//    }
//
//    @Override
//    public String getErrorPath() {
//        return null;
//    }
//}
