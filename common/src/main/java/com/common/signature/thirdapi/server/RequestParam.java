package com.common.signature.thirdapi.server;

import com.common.util.StringUtils;
import com.google.gson.Gson;
import org.apache.catalina.filters.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.NettyDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestParam {
    static Logger logger = LoggerFactory.getLogger(RequestParam.class);

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_DELETE = "DELETE";

    /**
     * 获取请求参数
     *
     * @param request request
     * @return 按升序排序过QueryString格式的请求参数
     */
    public static String getQueryParams(ServerHttpRequest request) {
        String method = request.getMethodValue();
        // 升序：Comparator.naturalOrder()，降序：Comparator.reverseOrder()
        TreeMap<String, Object> resultMap = new TreeMap<>(Comparator.naturalOrder());
        Map<String, Object> headerMap = getHeaderSpecifyParam(request);
        if (headerMap.containsKey("x-sign")) {
            headerMap.remove("x-sign");
        }
        resultMap.putAll(headerMap);
        // 获取请求参数
        if (METHOD_GET.equals(method)) {
            Map<String, String> paramMap = getRequestParam(request);
            resultMap.putAll(paramMap);
        } else if (METHOD_POST.equals(method)) {
            resultMap = dealPost(request, resultMap);
        } else if (METHOD_DELETE.equals(method)) {
            resultMap = dealPost(request, resultMap);
        } else {
            // 其他请求方式暂不处理
            return null;
        }
        return sortTreeMapToQueryString(resultMap);
    }

    /**
     * 获取Header指定请求参数(安全验签用)
     *
     * @param request request
     * @return
     */
    private static Map<String, Object> getHeaderSpecifyParam(ServerHttpRequest request) {
        Map<String, Object> map = new HashMap<>(5, 1);
        HttpHeaders headers = request.getHeaders();
        // apikey
        String apikey = headers.getFirst("x-apikey");
        if (StringUtils.isNotBlank(apikey)) {
            map.put("x-apikey", apikey);
        }
        // 协议版本号
        String v = headers.getFirst("x-v");
        if (StringUtils.isNotBlank(v)) {
            map.put("x-v", v);
        }
        // 设备唯一标识
        String cid = headers.getFirst("x-cid");
        if (StringUtils.isNotBlank(cid)) {
            map.put("x-cid", cid);
        }

        // 签名值
        String sign = headers.getFirst("x-sign");
        if (StringUtils.isNotBlank(sign)) {
            map.put("x-sign", sign);
        }

        // 时间戳
        String timestamp = headers.getFirst("x-ts");
        if (StringUtils.isNotBlank(timestamp)) {
            map.put("x-ts", timestamp);
        }
        return map;
    }

    /**
     * 去掉字符串头尾指定字符
     *
     * @param source  需要处理的字符串
     * @param firstElement 指定字符
     * @param lastElement 指定字符
     * @return
     */
    private static String trimFirstAndLastChar(String source, char firstElement, char lastElement) {
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do {
            //判断指定字符是否出现在该字符串的第一位  是--返回下标1   否--返回下标0
            int beginIndex = source.indexOf(firstElement) == 0 ? 1 : 0;
            //判断指定字符是否出现在该字符串的最后一位  是--返回出现的位置   否--返回字符长度
            int endIndex = source.lastIndexOf(lastElement) + 1 == source.length() ? source.lastIndexOf(lastElement) : source.length();
            //开始截取字符串
            source = source.substring(beginIndex, endIndex);
            //判断新字符串首位是否还存在指定字符
            beginIndexFlag = (source.indexOf(firstElement) == 0);
            //判断新字符串最后一位是否还存在指定字符
            endIndexFlag = (source.lastIndexOf(lastElement) + 1 == source.length());
        }
        while (beginIndexFlag || endIndexFlag);//条件通过再次截取
        return source;
    }

    /**
     * 获取body请求参数
     */
    private static TreeMap dealPost(ServerHttpRequest request, TreeMap<String, Object> resultMap) {
        String bodyStr = getBodyContent(request);
        bodyStr = StringUtils.isNotBlank(bodyStr)? bodyStr.toLowerCase(Locale.ROOT):"";
        // contains时间复杂度O(n)，推荐使用
        MediaType contentType = request.getHeaders().getContentType();
        if(contentType != null&&StringUtils.isNotEmpty(bodyStr)){
            if (contentType.toString().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
//                bodyStr = decode(bodyStr.replaceAll("\\s*",""));
                resultMap.putAll(form2Map(bodyStr));
            }else if(contentType.toString().contains(MediaType.APPLICATION_JSON_VALUE)){
                resultMap.put("body",bodyStr);
            }else if (contentType.toString().contains(MediaType.APPLICATION_XML_VALUE)){
                resultMap.put("body",bodyStr);
                return resultMap;
            }else{
                resultMap.putAll(getRequestParam(request));
            }
        }else {
            if (contentType != null && !contentType.toString().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE) &&
                    !contentType.toString().contains(MediaType.APPLICATION_JSON_VALUE) &&
                    !contentType.toString().contains(MediaType.APPLICATION_XML_VALUE)
            ) {
                resultMap.putAll(getRequestParam(request));
            }
        }
        return resultMap;
    }

    /**
     * 获取请求体内容
     *
     * @param request
     * @return
     */
    private static String getBodyContent(ServerHttpRequest request) {
        // Object attribute = exchange.getAttribute("cachedRequestBody);
        // if (attribute == null || !(attribute instanceof NettyDataBuffer)) {
        //     return null;
        // }
        //
        // NettyDataBuffer nettyDataBuffer = (NettyDataBuffer) attribute;
        // CharBuffer charBuffer = StandardCharsets.UTF_8.decode(nettyDataBuffer.asByteBuffer());
        // return charBuffer.toString();

        //TODO
        return "body";
    }

    private static HashMap<String,Object> form2Map(String formData){
        String[] params;
        HashMap<String,Object> map = new HashMap<>();

        //params按照&将每个键值对分割成字符串数组
        params = formData.split("&");

        //增强for遍历
        for(String s : params){
            String[] list = s.split("=");
            if(list.length>1){
                map.put(list[0],list[1]);
            }
        }
        return map;
    }

    /**
     * 获取param请求参数
     */
    private static Map<String, String> getRequestParam(ServerHttpRequest request) {
        Map<String, String> reqMap = new HashMap<>();
        Map<String, String> map;
        map = (Map) request.getQueryParams();

        // 处理map编码和转小写
        Iterator it = map.entrySet().iterator();
        String key;
        String value;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            key = entry.getKey().toString().toLowerCase(Locale.ROOT);
            if(entry.getValue() == null || "".equals(entry.getValue()) || entry.getValue().toString().length() == 2){
                reqMap.put(key, null);
            }else{
                value = entry.getValue().toString().toLowerCase(Locale.ROOT);
                value = trimFirstAndLastChar(value, '[', ']');
                reqMap.put(key, value);
            }

        }
        return reqMap;
    }

    private static String getRequestParam(String timestamp, String nonceStr, HttpServletRequest request) throws UnsupportedEncodingException {
        TreeMap<String, Object> params = new TreeMap<>(Comparator.naturalOrder());
        Enumeration<String> enumeration = request.getParameterNames();
        if (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getParameter(name);
            params.put(name, URLEncoder.encode(value, "UTF-8"));
        }
        String qs = String.format("%s×tamp=%s&nonceStr=%s&key=%s", sortTreeMapToQueryString(params), timestamp, nonceStr);
        return qs;
    }

    /**
     * 生成签名，根据字段名ascii码，从小大到大
     *
     * @param map
     * @return
     */
    private static String sortTreeMapToQueryString(TreeMap<String, Object> map) {

        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        String ret = "";
        while (iter.hasNext()) {
            String key = iter.next();
            ret += key;
            ret += "=";
            ret += map.get(key);
            ret += "&";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

    /**
     * 获取请求头参数map
     *
     * @param request request
     * @return Map<String, Object>
     */
    private static Map<String, Object> getReqHeaderMap(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        Set<String> set = headers.keySet();
        Map<String, Object> headersMap = new HashMap<>();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String v = headers.getFirst(key);
            headersMap.put(key, v);
        }
        return headersMap;
    }

    /**
     * 获取请求参数map（发送kafka）
     *
     * @param request request
     * @return Map<String, Object>
     */
    private static Map<String, Object> getReqParamMap(ServerHttpRequest request) {
        String method = request.getMethodValue();
        Map<String, Object> resultMap = new HashMap<>();
        Gson gson = new Gson();
        if (METHOD_GET.equals(method)) {
            Map<String, String> paramMap = getKafkaRequestParam(request);
            resultMap.putAll(paramMap);
        } else if (METHOD_POST.equals(method)) {
            String bodyStr = getKafkaRequestBody(request);
            resultMap.put("body",bodyStr);
        } else if (METHOD_DELETE.equals(method)) {
            String bodyStr = getKafkaRequestBody(request);
            resultMap.put("body",bodyStr);
        } else {
            // 其他请求方式暂不处理
            return null;
        }
        return resultMap;
    }

    /**
     * 获取param请求参数(发送kafka)
     */
    private static Map<String, String> getKafkaRequestParam(ServerHttpRequest request) {
        Map<String, String> reqMap = new HashMap<>();
        Map<String, String> map;
        map = (Map) request.getQueryParams();

        // 处理map编码和转小写
        Iterator it = map.entrySet().iterator();
        String key;
        String value;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            key = entry.getKey().toString();
            value = entry.getValue().toString();
            value = trimFirstAndLastChar(value, '[', ']');
            reqMap.put(key, value);
        }
        return reqMap;
    }

    /**
     * 获取body请求参数（发送kafka）
     */
    private static String getKafkaRequestBody(ServerHttpRequest request) {
        String bodyStr = getBodyContent(request);
        Map<String, Object> bodyMap;
        Gson gson = new Gson();
        // contains时间复杂度O(n)，推荐使用
        MediaType contentType = request.getHeaders().getContentType();
        if(contentType != null&&StringUtils.isNotEmpty(bodyStr)){
            if (contentType.toString().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                logger.debug("contentType:{},bodyStr:{}",MediaType.APPLICATION_JSON_VALUE,bodyStr);
                bodyMap = form2Map(bodyStr);
                bodyStr = gson.toJson(bodyMap);
            }else if(contentType.toString().contains(MediaType.APPLICATION_JSON_VALUE)){
                logger.debug("contentType:{},bodyStr:\"{}\"",MediaType.APPLICATION_JSON_VALUE,bodyStr);
                return bodyStr;
            }else if (contentType.toString().contains(MediaType.APPLICATION_XML_VALUE)){
                logger.debug("contentType:{},bodyStr:{}",MediaType.APPLICATION_JSON_VALUE,bodyStr);
                return bodyStr;
            }else{
                return "";
            }
        }
        return bodyStr;
    }

}
