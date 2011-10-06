/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package org.alembic.aurion.util;

import java.lang.reflect.Method;

/**
 *
 * @author jhoppesc
 */
public class ReflectionHelper {

    /**
     * This method will return the reflection method object for the
     * given class and methodName.
     *
     * @param classObject  The class containing the method.
     * @param methodName The name of the method to find.
     * @return The Method object for that method.
     */
    public static Method getMethod(Class classObject, String methodName) {
        Method oReturnMethod = null;

        // Note that there is an assumption here for what we are working on
        // that names of methods are unique and there is no overloading
        // of method names.   We are looking only by method name.  Since
        // these are specifically for web services and component proxies -
        // we are fine because the method names are unique in both cases
        // within a single class.
        //---------------------------------------------------------------
        Method[] oaMethod = classObject.getDeclaredMethods();
        for (Method oMethod : oaMethod) {
            if (oMethod.getName().equals(methodName)) {
                oReturnMethod = oMethod;
            }
        }   // for (Method oMethod : oaMethod)

        return oReturnMethod;
    }

}
