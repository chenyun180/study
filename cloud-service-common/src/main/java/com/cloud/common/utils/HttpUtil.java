package com.cloud.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.common.utils.common.DateUtil;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public final class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String getURL(HttpServletRequest request) {
        StringBuffer sb = request.getRequestURL();
        String queryString = request.getQueryString();
        if (queryString != null) {
            return sb.toString() + "?" + queryString;
        }
        return sb.toString();
    }


    /**
     * Get Integer parameter from request. If specified parameter name is not
     * found, the default value will be returned.
     */
    public static int getInt(HttpServletRequest request, String paramName, int defaultValue) {
        String s = request.getParameter(paramName);
        if (s == null || s.equals("")) {
            return defaultValue;
        }
        return Integer.parseInt(s);
    }


    /**
     * Get Integer parameter from request. If specified parameter name is not
     * found, an Exception will be thrown.
     */
    public static int getInt(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Integer.parseInt(s);
    }


    public static long getLong(HttpServletRequest request, String paramName, long defaultValue) {
        String s = request.getParameter(paramName);
        if (s == null || s.equals("")) {
            return defaultValue;
        }
        return Long.parseLong(s);
    }


    public static long getLong(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Long.parseLong(s);
    }


    /**
     * Get String parameter from request. If specified parameter name is not
     * found, the default value will be returned.
     */
    public static String getString(HttpServletRequest request, String paramName, String defaultValue) {
        String s = request.getParameter(paramName);
        if (s == null || s.equals("")) {
            return defaultValue;
        }
        return s;
    }


    /**
     * Get String parameter from request. If specified parameter name is not
     * found or empty, an Exception will be thrown.
     */
    public static String getString(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        if (s == null || s.equals("")) {
            throw new NullPointerException("Null parameter: " + paramName);
        }
        return s;
    }


    /**
     * Get Boolean parameter from request. If specified parameter name is not
     * found, an Exception will be thrown.
     */
    public static boolean getBoolean(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Boolean.parseBoolean(s);
    }


    /**
     * Get Boolean parameter from request. If specified parameter name is not
     * found, the default value will be returned.
     */
    public static boolean getBoolean(HttpServletRequest request, String paramName, boolean defaultValue) {
        String s = request.getParameter(paramName);
        if (s == null || s.equals("")) {
            return defaultValue;
        }
        return Boolean.parseBoolean(s);
    }


    /**
     * Get float parameter from request. If specified parameter name is not
     * found, an Exception will be thrown.
     */
    public static float getFloat(HttpServletRequest request, String paramName) {
        String s = request.getParameter(paramName);
        return Float.parseFloat(s);
    }


    /**
     * Create a FormBean and bind data to it. Example: If found a parameter
     * named "age", the object's setAge() method will be invoked if this method
     * exists. If a setXxx() method exists but no corrsponding parameter, this
     * setXxx() method will never be invoked.<br/>
     * <b>NOTE:</b> only public setXxx() method can be invoked successfully.
     */
    public static Object createFormBean(HttpServletRequest request, Class<?> c) {
        Object bean;
        try {
            bean = c.newInstance();
        } catch (Exception e) {
            return new Object();
        }
        Method[] ms = c.getMethods();
        for (int i = 0; i < ms.length; i++) {
            String name = ms[i].getName();
            if (name.startsWith("set")) {
                Class<?>[] cc = ms[i].getParameterTypes();
                if (cc.length == 1) {
                    String type = cc[0].getName(); // parameter type
                    try {
                        // get property name:
                        String prop = Character.toLowerCase(name.charAt(3)) + name.substring(4);
                        // get parameter value:
                        String param = getString(request, prop);
                        if (param != null && !param.equals("")) {
                            if (type.equals("java.lang.String")) {
                                ms[i].invoke(bean, new Object[]{htmlEncode(param)});
                            } else if (type.equals("int") || type.equals("java.lang.Integer")) {
                                ms[i].invoke(bean, new Object[]{new Integer(param)});
                            } else if (type.equals("long") || type.equals("java.lang.Long")) {
                                ms[i].invoke(bean, new Object[]{new Long(param)});
                            } else if (type.equals("boolean") || type.equals("java.lang.Boolean")) {
                                ms[i].invoke(bean, new Object[]{Boolean.valueOf(param)});
                            } else if (type.equals("float") || type.equals("java.lang.Float")) {
                                ms[i].invoke(bean, new Object[]{new Float(param)});
                            } else if (type.equals("java.util.Date")) {
                                Date date = null;
                                if (param.indexOf(':') != (-1)) {
                                    date = DateUtil.parseDateTime1(param);
                                } else {
                                    date = DateUtil.parseDate2(param);
                                }
                                if (date != null) {
                                    ms[i].invoke(bean, new Object[]{date});
                                } else {
                                    System.err.println("WARNING: date is null: " + param);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("WARNING: Invoke method " + ms[i].getName() + " failed: " + e.getMessage());
                    }
                }
            }
        }
        return bean;
    }

    public static String htmlEncode(String text) {
        if (text == null || "".equals(text)) {
            return "";
        }
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace(" ", "&nbsp;");
        text = text.replace("\"", "&quot;");
        text = text.replace("\'", "&apos;");
        return text.replace("\n", "<br/>");
    }

    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    public static String getIPAddress(HttpServletRequest request) {
        // 获取X-Forwarded-For
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 获取Proxy-Client-IP
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // WL-Proxy-Client-IP
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 获取的IP实际上是代理服务器的地址，并不是客户端的IP地址
            ip = request.getRemoteAddr();
        }
        /*
         * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
         * X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
         * 用户真实IP为： 192.168.1.110
         */
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    /**
     * 探测过滤请求中是否包含当前URL
     *
     * @param request 请求体
     * @param url     URL集合
     * @return 返回包含状态, true包含、false不包含
     */
    public static boolean inContainURL(HttpServletRequest request, List<String> url) {
        boolean result = false;
        StringBuffer reqUrl = request.getRequestURL();
        logger.debug("checkLogin  url : " + reqUrl);
        if (url != null) {
            for (String ignoreUrl : url) {
                if (reqUrl.indexOf(ignoreUrl) > 1) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 探测过滤请求中是否包含当前URL
     *
     * @param request 请求体
     * @param url     参数为以';'分割的URL字符串
     * @return 返回状态, true包含、false不包含
     */
    public static boolean inContainURL(HttpServletRequest request, String url) {
        boolean result = false;
        if (url != null && !"".equals(url.trim())) {
            String[] urlArr = url.split(";");
            StringBuffer reqUrl = request.getRequestURL();
            if(reqUrl.indexOf("websocket") > 1){
                return true;
            }
            for (String element : urlArr) {
                if (reqUrl.indexOf(element) > 1) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取POST请求中Body参数
     *
     * @param request
     * @return 字符串
     */
    public static String getBodyParm(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedReaderToString(br);
    }

    /**
     * 获取POST请求中Body参数
     *
     * @param serverHttpRequest
     * @return 字符串
     */
    public static String getBodyParm(ServerHttpRequest serverHttpRequest) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(serverHttpRequest.getBody(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedReaderToString(br);
    }

    /**
     * 获取POST请求中Body参数
     *
     * @param inputStream
     * @return 字符串
     */
    public static String getBodyParm(InputStream inputStream) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedReaderToString(br);
    }

    private static String bufferedReaderToString(BufferedReader br) {
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // 获取注册用户地区分布
    public static JSONObject registerUserLocation(JSONArray array) {
        JSONObject result = new JSONObject();
        if (array == null || array.size() < 1) {
            return result;
        }
        JSONArray resultArray = new JSONArray();
        StringBuffer sbUrl = new StringBuffer("http://restapi.amap.com/v3/geocode/regeo");
        sbUrl.append("?key=92f17bdf774eb0eae9c835d852a7d87e");
        sbUrl.append("&batch=true");
        // 传入内容规则：经度在前，纬度在后，经纬度间以“,”分割，经纬度小数点后不要超过 6 位。
        // 如果需要解析多个经纬度的话，请用"|"进行间隔，并且将 batch 参数设置为 true，
        // 最多支持传入 20 对坐标点。每对点坐标之间用"|"分割。
        StringBuffer locationSb = new StringBuffer();
        locationSb.append("&location=");
        int num = 0;
        for (Object o : array) {
            num++;
            JSONObject jsonObject = (JSONObject) o;
            locationSb.append(jsonObject.getString("lng"));
            locationSb.append(",");
            locationSb.append(jsonObject.getString("lat"));
            if (num >= 20) {
                JSONObject executeResult = executeGet(sbUrl.toString() + locationSb.toString());
                resultArray.addAll(executeResult.getJSONArray("regeocodes"));
                num = 0;
                locationSb = new StringBuffer();
                locationSb.append("&location=");
            } else {
                locationSb.append("%7C");
            }
        }
        if (locationSb.length() > 13) {
            locationSb.deleteCharAt(locationSb.lastIndexOf("C"));
            locationSb.deleteCharAt(locationSb.lastIndexOf("7"));
            locationSb.deleteCharAt(locationSb.lastIndexOf("%"));
            JSONObject executeResult = executeGet(sbUrl.toString() + locationSb.toString());
            resultArray.addAll(executeResult.getJSONArray("regeocodes"));
        }

        for (Object o : resultArray) {
            JSONObject jsonObject = (JSONObject) o;
            JSONObject addressComponent = jsonObject.getJSONObject("addressComponent");
            String key = getLocationKey(addressComponent);
            int intValue = result.getIntValue(key);
            intValue++;
            result.put(key, intValue);
        }
        return result;
    }
    // 获取注册用户地区分布
    public static String registerUserLocation(JSONObject jsonObject) {
        StringBuffer sbUrl = new StringBuffer("http://restapi.amap.com/v3/geocode/regeo");
        sbUrl.append("?key=92f17bdf774eb0eae9c835d852a7d87e");
        sbUrl.append("&location=");
        sbUrl.append(jsonObject.getString("lng"));
        sbUrl.append(",");
        sbUrl.append(jsonObject.getString("lat"));
        JSONObject regeocode = executeGet(sbUrl.toString()).getJSONObject("regeocode").getJSONObject("addressComponent");
        return getLocationKey(regeocode);
    }

    private static String getLocationKey(JSONObject regeocode) {
        StringBuffer sbKey = new StringBuffer();
        if (!regeocode.getString("province").equals("[]")) {
            sbKey.append(regeocode.getString("province"));
        }
        if (!regeocode.getString("city").equals("[]")) {
            sbKey.append(regeocode.getString("city"));
        }
        if (!regeocode.getString("district").equals("[]")) {
            sbKey.append(regeocode.getString("district"));
        }
        return sbKey.toString();
    }

    public static JSONObject executeGet(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            logger.info("executing request " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return JSONObject.parseObject(EntityUtils.toString(entity));
                }
            } finally {
                response.close();
            }
        } catch (
                ClientProtocolException e) {
            e.printStackTrace();
        } catch (
                ParseException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }


//    public static void main(String[] args) {
//        String arrayStr = "[ \n" +
//                "{ \n" +
//                "\"lng\":113.264500,\n" +
//                "\"lat\":23.128800\n" +
//                "},\n" +
//                "{ \n" +
//                "\"lng\":116.560687,\n" +
//                "\"lat\":33.226636\n" +
//                "},\n" +
//                "{ \n" +
//                "\"lng\":115.755594,\n" +
//                "\"lat\":33.828110\n" +
//                "},\n" +
//                "{ \n" +
//                "\"lng\":116.176119,\n" +
//                "\"lat\":33.131133\n" +
//                "},\n" +
//                "{ \n" +
//                "\"lng\":113.264500,\n" +
//                "\"lat\":23.128800\n" +
//                "},\n" +
//                "{ \n" +
//                "\"lng\":116.206853,\n" +
//                "\"lat\":33.147933\n" +
//                "}]";
//        JSONArray array = JSONArray.parseArray(arrayStr);
//        // 打印地区分布
//        // System.out.println(registerUserLocation(array).toJSONString());
//
//
//        // 查询某个地区的经纬度。（为了找出准确用户）
//        List<String> userLocationList = new ArrayList<>();
//        for (Object o : array) {
//            JSONObject json = (JSONObject) o;
//            String userLocation = registerUserLocation(json);
//            if(userLocation.equals("广东省广州市越秀区")
//            ){
//                userLocationList.add(userLocation+":"+json.toJSONString());
//            }
//        }
//        for (String s : userLocationList) {
//            System.out.println(s);
//        }
//    }
}
