package com.hao.lib.net;

/**
 * post 表单参数提交
 *
 * @paramName 参数字段名
 * @paramContent 参数内容
 */
public class RequestFormBodyParam {
    String paramName;
    String paramContent;

    public RequestFormBodyParam(String paramName, String paramContent) {
        this.paramName = paramName;
        this.paramContent = paramContent;
    }

    @Override
    public String toString() {
        return "paramName='" + paramName + '\'' +
                ", paramContent='" + paramContent + '\'';
    }
}
