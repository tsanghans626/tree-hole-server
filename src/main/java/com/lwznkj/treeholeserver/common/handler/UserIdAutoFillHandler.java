package com.lwznkj.treeholeserver.common.handler;

import com.tangzc.mpe.annotation.handler.AutoFillHandler;
import com.tangzc.mpe.annotation.handler.IAutoFillHandlerForBaseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;


@Component
public class UserIdAutoFillHandler implements IAutoFillHandlerForBaseEntity<Long> {
    /**
     * @param object 当前操作的数据对象
     * @param clazz  当前操作的数据对象的class
     * @param field  当前操作的数据对象上的字段
     * @return userId
     */

    @Override
    public Long getVal(Object object, Class<?> clazz, Field field) {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            // 无请求上下文（如异步任务、定时任务）
            if (requestAttributes == null) {
                return 0L;
            }

            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            String userId = request.getHeader("user-id");

            // 有请求上下文但没设置请求头
            if (userId == null || userId.isEmpty()) {
                return -1L;
            }

            return Long.parseLong(userId);
        } catch (Exception e) {
            // 任何异常情况返回 0L
            return -2L;
        }
    }
}
