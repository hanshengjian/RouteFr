package com.route.fr;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.type.TypeMirror;

/**
 * Created by hansj on 2017/2/22.
 * 构建方法需要的元素
 */

public class MethodModel {
    private String methodName;
    private String className; //要跳转的类名
    private Map<String,TypeMirror> params; //key是变量的name,value是变量的类型
    private int requestCode = -1; //请求码
    private String createClass; //要创建的类名

    public MethodModel() {
        params = new LinkedHashMap<>();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, TypeMirror> getParams() {
        return params;
    }

    public void setParams(Map<String, TypeMirror> params) {
        this.params = params;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getCreateClass() {
        return createClass;
    }

    public void setCreateClass(String createClass) {
        this.createClass = createClass;
    }
}
