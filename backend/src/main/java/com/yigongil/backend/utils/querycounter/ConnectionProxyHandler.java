package com.yigongil.backend.utils.querycounter;

import java.lang.reflect.Method;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.annotation.Profile;

@Profile(value = {"local", "test"})
public class ConnectionProxyHandler implements InvocationHandler {

    private final Object connection;
    private final ApiQueryCounter apiQueryCounter;

    public ConnectionProxyHandler(Object connection, ApiQueryCounter apiQueryCounter) {
        this.connection = connection;
        this.apiQueryCounter = apiQueryCounter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invokeResult = method.invoke(connection, args);
        if (method.getName().equals("prepareStatement")) {
            return Proxy.newProxyInstance(
                    invokeResult.getClass().getClassLoader(),
                    invokeResult.getClass().getInterfaces(),
                    new PreparedStatementProxyHandler(invokeResult, apiQueryCounter)
            );
        }
        return invokeResult;
    }
}
