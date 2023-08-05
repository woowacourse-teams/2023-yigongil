package com.yigongil.backend.ui.querycounter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.web.context.request.RequestContextHolder;

public class PreparedStatementProxyHandler implements InvocationHandler {

    private final Object preparedStatement;
    private final ApiQueryCounter apiQueryCounter;

    public PreparedStatementProxyHandler(Object preparedStatement, ApiQueryCounter apiQueryCounter) {
        this.preparedStatement = preparedStatement;
        this.apiQueryCounter = apiQueryCounter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (isExecuteQuery(method) && isInRequestScope()) {
            apiQueryCounter.increaseCount();
        }
        return method.invoke(preparedStatement, args);
    }

    private boolean isExecuteQuery(Method method) {
        String methodName = method.getName();
        return methodName.equals("executeQuery") || methodName.equals("execute") || methodName.equals("executeUpdate");
    }

    private boolean isInRequestScope() {
        return null != RequestContextHolder.getRequestAttributes();
    }
}
