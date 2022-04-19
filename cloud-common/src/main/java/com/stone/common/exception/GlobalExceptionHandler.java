package com.stone.common.exception;

import com.alibaba.fastjson.JSON;
import com.stone.common.result.ApiResult;
import com.stone.common.result.ResultCode;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 自定义异常处理类
 */
@Log4j2
@Data
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ApiResult<Object> result = new ApiResult<>();
        result.setErrorMsg(ex.getMessage());
        if (ex instanceof GlobalException) {
            result.setCode(ResultCode.CODE_GLOBAL_EXCEPTION);
        } else {
            result.setCode(ResultCode.CODE_UNEXPECTED_EXCEPTION);
        }
        try (PrintWriter writer = response.getWriter()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            writer.println(JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("com.stone.common.exception.GlobalExceptionHandler.resolveException:responseJson error:" + result);
        }
        return null;
    }
}
