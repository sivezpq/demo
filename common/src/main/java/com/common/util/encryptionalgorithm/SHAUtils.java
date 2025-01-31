package com.common.util.encryptionalgorithm;
/**
 * @标题         : 一丰项目
 * @描述         : SHA1验签工具类
 * @所在公司     : 南京四维智联科技有限公司
 * @所在部门     : 项目交付部
 * @创建人       : 赵佳丽 zhaojl2@autoai.com
 * @创建时间     : 2022/6/29  16:17
 * @修改人       : 赵佳丽 zhaojl2@autoai.com
 * @修改时间     : 2022/6/29  16:17
 * @修改分支信息 : 
 * @版本         : 1.0
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class SHAUtils {

    private static final Logger logger = LoggerFactory.getLogger(SHAUtils.class);

    public static final String KEY_SHA = "SHA";

    /**
     * 
     * @描述      : SHA1验签工具类
     * @param content
     * @结果      : 
     * @异常      : 
     * @创建者     : 赵佳丽 zhaojl2@autoai.com
     * @创建时间   : 2022/6/30 10:26
     */
    public static String SHAEncrypt(final String content) {
        try {
            MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
            byte[] sha_byte = sha.digest(content.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (byte b : sha_byte) {
                //将其中的每个字节转成十六进制字符串：byte类型的数据最高位是符号位，通过和0xff进行与操作，转换为int类型的正整数。
                String toHexString = Integer.toHexString(b & 0xff);
                hexValue.append(toHexString.length() == 1 ? "0" + toHexString : toHexString);
            }
            return hexValue.toString();
        } catch (Exception e) {
            logger.error("SHA加密失败，msg：{}",e.getMessage(),e);
        }
        return "";
    }

    public static void main(String[] args) {
        String data = "Hello Dylan";
        String encryptedData = SHAUtils.SHAEncrypt(data);
        System.out.println("Encrypted data: " + encryptedData);
    }
}
