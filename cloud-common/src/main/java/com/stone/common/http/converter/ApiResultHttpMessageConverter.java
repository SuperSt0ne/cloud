package com.stone.common.http.converter;

import com.alibaba.fastjson2.JSON;
import com.stone.common.web.ApiResult;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ApiResultHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    public ApiResultHttpMessageConverter() {
        super(MediaType.ALL);
        super.setDefaultCharset(StandardCharsets.UTF_8);
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return false;
    }

    /**
     * Writes the actual body. Invoked from {@link #write}.
     *
     * @param t             the object to write to the output message
     * @param type          the type of object to write (may be {@code null})
     * @param outputMessage the HTTP output message to write to
     * @throws IOException                     in case of I/O errors
     * @throws HttpMessageNotWritableException in case of conversion errors
     */
    @Override
    protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        MediaType contentType = outputMessage.getHeaders().getContentType();
        Charset charset = getCharsetToUse(contentType);

        ApiResult<Object> apiResult = new ApiResult<>();
        apiResult.setData(t);

        outputMessage.getBody().write(JSON.toJSONString(apiResult).getBytes(charset));
        outputMessage.getBody().flush();
    }

    private Charset getCharsetToUse(@Nullable MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            return contentType.getCharset();
        }
        return getDefaultCharset();
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        throw new UnsupportedOperationException();
    }

}
