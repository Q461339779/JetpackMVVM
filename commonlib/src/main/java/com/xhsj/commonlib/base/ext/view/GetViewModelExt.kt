package com.xhsj.commonlib.base.ext.view

import java.lang.reflect.ParameterizedType


/**
 * 获取当前类绑定的泛型ViewModel-clazz
 */
fun <VM> getVmClazz(obj:Any):VM{
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}