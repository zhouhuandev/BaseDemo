package com.hzsoft.lib.base.utils

import android.text.TextUtils
import java.lang.Exception
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

object ReflectUtils {
    private val sMethods: MutableMap<String, Method?> = ConcurrentHashMap()

    @JvmStatic
    fun getDefaultValue(type: Class<*>): Any? {
        if (type == Boolean::class.javaPrimitiveType || type == Boolean::class.java) {
            return false
        }
        if (type == Void.TYPE) {
            return null
        }
        return if (type.isPrimitive || Number::class.java.isAssignableFrom(type)) {
            0
        } else null
    }

    @JvmStatic
    @Throws(Exception::class)
    fun invokeMethod(any: Any, methodName: String): Any? {
        val clazz: Class<*> = any.javaClass
        return invokeMethod(clazz, any, methodName, null, null)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun invokeMethod(className: String, methodName: String): Any? {
        val clazz = Class.forName(className)
        return invokeMethod(clazz, null, methodName, null, null)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun invokeMethod(clazz: Class<*>, methodName: String): Any? {
        return invokeMethod(clazz, null, methodName, null, null)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun invokeMethod(
        clazz: Class<*>,
        methodName: String,
        cls: Array<Class<*>>,
        args: Array<Any?>
    ): Any? {
        return invokeMethod(clazz, null, methodName, cls, args)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun invokeMethod(
        className: String, any: Any?, methodName: String, cls: Array<Class<*>>?,
        args: Array<Any?>?
    ): Any? {
        return invokeMethod(Class.forName(className), any, methodName, cls, args)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun invokeStaticMethod(
        clazz: Class<*>, methodName: String, cls: Array<Class<*>>?,
        args: Array<Any?>?
    ): Any? {
        return invokeMethod(clazz, null, methodName, cls, args)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun invokeStaticMethod(
        className: String, methodName: String, cls: Array<Class<*>>?,
        args: Array<Any?>?
    ): Any? {
        return invokeMethod(Class.forName(className), null, methodName, cls, args)
    }

    @JvmStatic
    @Throws(Exception::class)
    fun invokeMethod(
        clazz: Class<*>,
        any: Any?,
        methodName: String,
        cls: Array<Class<*>>?,
        args: Array<Any?>?
    ): Any? {
        val method = getMethod(clazz, methodName, cls)
        return if (null == args) {
            method!!.invoke(any)
        } else {
            method!!.invoke(any, *args)
        }
    }

    @JvmStatic
    @Throws(NoSuchMethodException::class)
    fun getMethod(
        clazz: Class<*>,
        methodName: String,
        cls: Array<Class<*>>?
    ): Method? {
        var key = clazz.name + "." + methodName
        if (null != cls && cls.isNotEmpty()) {
            key = "$key("
            for (c in cls) {
                key = key + c.name + ","
            }
            key = key.substring(0, key.length - 1) + ")"
        }
        var method: Method? = null
        try {
            if (sMethods.containsKey(key)) {
                method = sMethods[key]
            }
        } catch (e: Throwable) { //NOPMD
        }
        if (null == method) {
            method = if (null == cls) {
                clazz.getDeclaredMethod(methodName)
            } else {
                clazz.getDeclaredMethod(methodName, *cls)
            }
            method.isAccessible = true
            sMethods[key] = method
        }
        return method
    }

    @JvmStatic
    @Throws(
        ClassNotFoundException::class,
        NoSuchFieldException::class,
        IllegalAccessException::class
    )
    fun getField(classOrName: Any, fieldName: String, targetObject: Any): Any? {
        val clazz: Class<*>? = if (classOrName is Class<*>) {
            classOrName
        } else {
            val className = classOrName.toString()
            Class.forName(className)
        }
        val field = clazz?.getDeclaredField(fieldName)
        field?.isAccessible = true
        return field?.get(targetObject)
    }

    @JvmStatic
    fun findMethod(clazz: Class<*>, methodName: String, argsType: Array<String>?): Method? {
        val methods = clazz.methods
        for (method in methods) {
            if (method.name != methodName) {
                continue
            }
            val argsTypeLen = argsType?.size ?: 0
            if (method.parameterTypes.size != argsTypeLen) {
                continue
            }
            val parameterTypes = method.parameterTypes
            if (equalsParamTypes(parameterTypes, argsType)) {
                return method
            }
        }
        return null
    }

    private fun equalsParamTypes(
        parameterTypes: Array<Class<*>>,
        paramTypes: Array<String>?
    ): Boolean {
        for (i in parameterTypes.indices) {
            val paramType = paramTypes?.get(i)
            if (!TextUtils.equals(parameterTypes[i].name, paramType)) {
                return false
            }
        }
        return true
    }
}