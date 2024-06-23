package top.frankxxj.homework.api.videobackend.vod;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Slf4j
public class AuthUrlDemo {
    // url是未带加密信息的原始播放url, key是在点播控制台配置的Key值
    // 加密算法A
    public static String createAuthInfoUrlByAlgorithmA(String url, String key) {
        try {
            checkParam(url, key);

            long timestamp = Instant.now().getEpochSecond();
            String randUid = UUID.randomUUID().toString().replaceAll("-", "");
            String uid = "0";
            String tmpRandKey = timestamp + "-" + randUid + "-" + uid;

            URL originUrl = new URL(url);
            String string2Md5 = originUrl.getPath() + "-" + tmpRandKey + "-" + key;
            String md5Hash = DigestUtils.md5DigestAsHex(string2Md5.getBytes(StandardCharsets.UTF_8));
            String authInfo = "auth_key=" + tmpRandKey + "-" + md5Hash;

            return StringUtils.hasLength(originUrl.getQuery()) ? url + "&" + authInfo : url + "?" + authInfo;
        } catch (Exception e) {
            log.error("error occured: {}", e.getMessage());
        }
        return null;
    }

    // 加密算法B
    public static String createAuthInfoUrlByAlgorithmB(String url, String key) {
        try {
            checkParam(url, key);

            URL originUrl = new URL(url);
            String filePath = originUrl.getPath();
            String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String str2Md5 = key + dateStr + filePath;
            String md5sum = DigestUtils.md5DigestAsHex(str2Md5.getBytes(StandardCharsets.UTF_8));

            return originUrl.getProtocol() + "://" + originUrl.getHost() + "/"
                    + dateStr + "/" + md5sum + originUrl.getFile();
        } catch (Exception e) {
            log.error("error occured: {}", e.getMessage());
        }
        return null;
    }

    // 加密算法C
    public static String createAuthInfoUrlByAlgorithmC(String url, String key) {
        try {
            checkParam(url, key);

            URL originUrl = new URL(url);
            String filePath = originUrl.getPath();
            String hexTime = Long.toHexString(Instant.now().getEpochSecond()).toUpperCase(Locale.ENGLISH);
            String str2Md5 = key + filePath + hexTime;
            String md5Hash = DigestUtils.md5DigestAsHex(str2Md5.getBytes(StandardCharsets.UTF_8));

            return originUrl.getProtocol() + "://" + originUrl.getHost() + "/" + md5Hash + "/" + hexTime + originUrl.getFile();
        } catch (Exception e) {
            log.error("error occured: {}", e.getMessage());
        }
        return null;
    }

    // 加密算法D
    public static String createAuthInfoUrlByAlgorithmD(String url, String key) {
        try {
            checkParam(url, key);

            URL originUrl = new URL(url);
            String urlPath = originUrl.getPath();
            String pathInUrl = urlPath.substring(0, urlPath.lastIndexOf("/") + 1);
            String data = encodeUrl(pathInUrl) + "$" + getUtcTime("yyyyMMddHHmmss");
            String encryptInfo = aesCbcEncrypt(data, key, true);
            String authInfoStr = "auth_info=" + URLEncoder.encode(encryptInfo, StandardCharsets.UTF_8);
            return urlPath + "?" + authInfoStr;
        } catch (Exception e) {
            log.error("error occured: {}", e.getMessage());
        }
        return null;
    }

    private static void checkParam(String url, String key) {
        if (!StringUtils.hasLength(url) || !StringUtils.hasLength(key)) {
            throw new IllegalArgumentException("url or key is illegal");
        }
    }

    private static String aesCbcEncrypt(String data, String key, boolean hasPoint) throws Exception {
        checkParam(data, key);

        byte[] realKey = get128BitKey(key);
        SecureRandom secureRand = new SecureRandom();
        byte[] ivBytes = new byte[16];
        secureRand.nextBytes(ivBytes);

        if (hasPoint) {
            return aesCbcEncrypt(data, ivBytes, realKey) + "." + bytesToHexString(ivBytes);
        } else {
            return aesCbcEncrypt(data, ivBytes, realKey) + bytesToHexString(ivBytes);
        }
    }

    private static String aesCbcEncrypt(String data, byte[] ivBytes, byte[] key) throws Exception {
        SecretKeySpec sk = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        if (ivBytes != null) {
            cipher.init(Cipher.ENCRYPT_MODE, sk, new IvParameterSpec(ivBytes));
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, sk);
        }

        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private static byte[] get128BitKey(String key) {
        byte[] result = null;

        if (key != null) {
            result = new byte[16];
            byte[] origin = key.getBytes();

            if (origin.length > 16) {
                System.arraycopy(origin, 0, result, 0, 16);
            } else {
                System.arraycopy(origin, 0, result, 0, origin.length);
            }
        }

        return result;
    }

    private static String encodeUrl(String str) {
        try {
            if (StringUtils.hasLength(str)) {
                StringBuilder encodeStr = new StringBuilder(32);
                String[] tmpArray = str.split("/");
                for (String s : tmpArray) {
                    encodeStr.append(URLEncoder.encode(s, StandardCharsets.UTF_8)).append("/");
                }
                return encodeStr.toString();
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Encode fail %s", e.getMessage()));
        }
        return str;
    }

    private static String getUtcTime(String dateTimePattern) {
        SimpleDateFormat foo = new SimpleDateFormat(dateTimePattern);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        return foo.format(new Date(cal.getTimeInMillis()));
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if ((src == null) || (src.length <= 0)) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
