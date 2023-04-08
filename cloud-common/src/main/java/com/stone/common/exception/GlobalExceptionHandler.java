package com.stone.common.exception;

import com.alibaba.fastjson.JSON;
import com.stone.common.constant.ApiConstant;
import com.stone.common.ding.DingTalkUtil;
import com.stone.common.web.ApiResult;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 自定义异常处理类
 */
@Log4j2
@Data
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    private String webhook;

    public GlobalExceptionHandler(String webhook) {
        this.webhook = webhook;
    }

    public GlobalExceptionHandler() {
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ApiResult<Object> result = new ApiResult<>();
        Throwable rootCause = ex;
        boolean doSend = StringUtils.isNotBlank(webhook);
        if (ex instanceof BizException) {
            result.setMessage(ex.getMessage());
            result.setErrorCode(ApiConstant.Code.REQUEST_BIZ_EXCEPTION);
            if (doSend) {
                DingTalkUtil.send(webhook, getContent(request, ExceptionUtil.getStackTrace(rootCause, 16)));
            }
        } else {
            if (ex instanceof NestedServletException && ((NestedServletException) ex).getRootCause() instanceof Error) {
                rootCause = ((NestedServletException) ex).getRootCause();
                if (rootCause instanceof OutOfMemoryError) {
                    // Trigger heap dump generation
                    throw (OutOfMemoryError) rootCause;
                }
            }
            String stackTrace = ExceptionUtil.getStackTrace(rootCause, 16);
            String content = getContent(request, stackTrace);
            result.setMessage(ApiConstant.Message.MESSAGE_SERVICE_ERROR);
            result.setErrorCode(ApiConstant.Code.REQUEST_SERVICE_ERROR);
            result.setStackTrance(stackTrace);
            if (doSend) {
                DingTalkUtil.send(webhook, content);
            }
            log.error(content);
        }
        responseJson(result, response);
        // The empty ModelAndView indicate that the exception has been resolved successfully
        // but that no view should be rendered,
        // see: org.springframework.web.servlet.DispatcherServlet.processHandlerException
        return new ModelAndView();
    }

    public static void responseJson(Object obj, HttpServletResponse response) {
        String jsonString = JSON.toJSONString(obj);
        writeJsonStringToResponse(jsonString, response);
    }

    public static void writeJsonStringToResponse(String jsonString, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        /*
          设置编码应在 getWriter 方法调用之前，参考：org.apache.catalina.connector.Response#setCharacterEncoding
         */
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getContent(HttpServletRequest request, String stackTrace) {
        return "错误信息:\n" +
                "RequestURI:" + request.getRequestURI() + "\n" +
//                "_uid:" + request.getAttribute(HttpConstant.PARAM_KEY_USER_ID) + "\n" +
                "请求参数:" + JSON.toJSONString(request.getParameterMap()) + "\n" +
                "堆栈信息:\n" + stackTrace + "\n";
//                "@" + request.getAttribute(HttpConstant.HEADER_KEY_DEVELOPER);
    }


}