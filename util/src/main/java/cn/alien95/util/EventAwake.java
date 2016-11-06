package cn.alien95.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linlongxin on 2016/1/19.
 * similar EventBus，notify operation when message change
 */
public class EventAwake {

    private static EventAwake instance;

    private Map<Object, Map<Method, Object[]>> hasParamMap;

    private Map<Object, Method> noParamMap;

    private EventAwake() {
        hasParamMap = new HashMap<>();
        noParamMap = new HashMap<>();
    }

    public static EventAwake getInstance() {
        if (instance == null) {
            synchronized (EventAwake.class) {
                instance = new EventAwake();
            }
        }
        return instance;
    }

    /**
     * register a method to execute when some time
     *
     * @param object a object for method
     * @param method
     */
    public void registerEvent(Object object, Method method) {
        noParamMap.put(object, method);
    }

    public void registerEvent(Object object, Method method, Object[] args) {
        Map<Method, Object[]> methodMap = new HashMap<>();
        methodMap.put(method, args);
        hasParamMap.put(object, methodMap);
    }

    /**
     * executeEvent() method will invoke through invoke sendMessage() method
     */
    private void executeEvent() {
        if (!noParamMap.isEmpty()) {
            for (Object object : noParamMap.keySet()) {
                try {
                    noParamMap.get(object).invoke(object, (Object[]) null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!hasParamMap.isEmpty()) {
            for (Object object : hasParamMap.keySet()) {
                try {
                    for (Method m : hasParamMap.get(object).keySet()) {
                        m.invoke(object, hasParamMap.get(object).get(m));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * invoke this method when you want to update data
     */
    public void notifyChange() {
        executeEvent();
    }

    /**
     * unRegister when you no longer user the object
     *
     * @param object
     */
    public void unRegisterEvent(Object object) {
        if (noParamMap.containsKey(object)) {
            noParamMap.remove(object);
        }
        if(hasParamMap.containsKey(object)){
            hasParamMap.remove(object);
        }
    }

}
