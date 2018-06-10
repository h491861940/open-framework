
package com.open.framework.core.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestBodyParamMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver implements WebArgumentResolver {
    private static ObjectMapper mapper = new ObjectMapper();
    private final ThreadLocal<JsonNode> threadJsonNode = new ThreadLocal();
    public RequestBodyParamMethodArgumentResolver() {
        super((ConfigurableBeanFactory)null);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
    }

    private JsonNode readBodyToJson(HttpServletRequest servletRequest) throws JsonProcessingException, IOException {
        InputStream input = servletRequest.getInputStream();
        String uri = servletRequest.getServletPath();
        String s = IOUtils.toString(input, "UTF-8");

        if (!"".equals(s) && s != null) {
            JsonNode rootNode = mapper.readTree(s);
            this.threadJsonNode.set(rootNode);
            System.out.println("地址是: " + servletRequest.getRequestURI() + "& 参数:" + s);
            return rootNode;
        } else {
            return (JsonNode)this.threadJsonNode.get();
        }
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBodyParam.class);
    }

    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        RequestBodyParam annotation = (RequestBodyParam)parameter.getParameterAnnotation(RequestBodyParam.class);
        return new RequestBodyParamMethodArgumentResolver.RequestBodyParamNamedValueInfo(annotation);
    }

    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
        Type type = parameter.getGenericParameterType();
        HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
        JsonNode rootNode = null;

        try {
            rootNode = this.readBodyToJson(servletRequest);
        } catch (Exception var9) {
            System.out.println("传入参数Json解析失败:" + var9);
        }

        if (rootNode == null) {
            return null;
        } else {
                rootNode = rootNode.findPath(name);
                if (rootNode.isMissingNode()) {
                    System.out.println("没有找到参数 '" + name + "' 对应的传入变量!");
                    return null;
                }
            JavaType javaType = this.getJavaType(type);
            return mapper.readValue(rootNode.traverse(), javaType);
        }
    }

    protected JavaType getJavaType(Type type) {
        return TypeFactory.defaultInstance().constructType(type);
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }


    public Object resolveArgument(MethodParameter parameter, NativeWebRequest request) throws Exception {
        return !this.supportsParameter(parameter) ? WebArgumentResolver.UNRESOLVED : this.resolveArgument(parameter, (ModelAndViewContainer)null, request, (WebDataBinderFactory)null);
    }

    private class RequestBodyParamNamedValueInfo extends NamedValueInfo {
        private RequestBodyParamNamedValueInfo(RequestBodyParam annotation, RequestBodyParamNamedValueInfo
                requestBodyParamNamedValueInfo) {
            super("", false, (String)null);
        }

        private RequestBodyParamNamedValueInfo(RequestBodyParam annotation) {
            super(annotation.value(), annotation.required(), (String)null);
        }
    }
}
