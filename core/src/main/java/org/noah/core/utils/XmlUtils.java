package org.noah.core.utils;

public class XmlUtils {
    public static String xml2json(String xml) {
        cn.hutool.json.JSONObject xmlJSONObj = cn.hutool.json.XML.toJSONObject(xml);
        return xmlJSONObj.toString();
    }
}
