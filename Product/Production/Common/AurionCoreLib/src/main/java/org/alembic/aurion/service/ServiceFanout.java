/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.service;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.alembic.aurion.nhinclib.NullChecker;
import org.alembic.aurion.util.ReflectionHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author jhoppesc
 */
public class ServiceFanout implements Runnable {
    private Log log = LogFactory.getLog(ServiceFanout.class);
    private Object proxyObject = null;
    private Class proxyClass = null;
    private String methodName = null;
    private Object msg = null;
    private Object assertion = null;
    private Object targetSystem = null;
    private Object response = null;

    public ServiceFanout (Object proxyObject, Class proxyClass, String methodName, Object msg, Object assertion, Object targetSystem) {
        this.proxyObject = proxyObject;
        this.proxyClass = proxyClass;
        this.methodName = methodName;
        this.msg = msg;
        this.assertion = assertion;
        this.targetSystem = targetSystem;
    }

    public void run () {
        log.info("Entering run method of the fanout thread");

        // Check input paramaters
        String errorMsg = null;
        
        if (proxyObject == null) {
            errorMsg = "proxyObject parameter was null";
        }
        else if (proxyClass == null) {
            errorMsg = "proxyClass parameter was null";
        }
        else if (NullChecker.isNullish(methodName)) {
            errorMsg = "methodName parameter was null";
        }
        else if (msg == null) {
            errorMsg = "msg parameter was null";
        }
        else if (assertion == null) {
            errorMsg = "assertion parameter was null";
        }
        else if (targetSystem == null) {
            errorMsg = "targetSystem parameter was null";
        }

        // If one of the input parameters was invalid then throw an IllegalArgumentException
        if (NullChecker.isNotNullish(errorMsg)) {
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        // Obtain the method to call the NHIN Component Proxy for this service
        Method proxyMethod = ReflectionHelper.getMethod(proxyClass, methodName);

        // If the method was found then execute the method
        if (proxyMethod != null) {
            try {
                response = proxyMethod.invoke(proxyObject, msg, assertion, targetSystem);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ServiceFanout.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ServiceFanout.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ServiceFanout.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            log.error("The method " + methodName + "was not defined in the class " + proxyClass.getName());
        }

        log.info("Exiting run method of the fanout thread");
    }

    public Object getResponse() {
        return this.response;
    }

    public Object getMsg() {
        return this.msg;
    }
}
