package com.hao.lib.net;

import okhttp3.MediaType;

public class RequestMultipartBodyParam {
    /**
     * http://www.iana.org/assignments/media-types/media-types.xhtml
     * mediaType 详细种类及相关解释 目前常用的参数为
     * json : application/json
     * xml : application/xml
     * png : image/png
     * jpg : image/jpeg
     * gif : imge/gif
     */
    MediaType mediaType;

    /**
     * 与mediaType匹配的内容
     * json : application/json
     * xml : application/xml
     * png : image/png  文件
     * jpg : image/jpeg 文件
     * gif : imge/gif   文件
     */
    Object mediaContent;

    /**
     * 请求头的名称
     */
    String headerName;

    /**
     * 请求头的内容
     */
    String headerContent;

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderContent() {
        return headerContent;
    }

    public void setHeaderContent(String headerContent) {
        this.headerContent = headerContent;
    }


    @Override
    public String toString() {
        return
                "mediaType=" + mediaType +
                        ", mediaContent=" + mediaContent +
                        ", headerName='" + headerName + '\'' +
                        ", headerContent='" + headerContent + '\'';
    }
}
