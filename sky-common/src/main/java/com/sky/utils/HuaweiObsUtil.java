package com.sky.utils;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class HuaweiObsUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 上传文件到华为云 OBS
     *
     * @param bytes      文件内容
     * @param objectName 文件对象名（含路径）
     * @return 访问地址
     */
    public String upload(byte[] bytes, String objectName) {
        ObsClient obsClient = new ObsClient(accessKeyId, accessKeySecret, endpoint);

        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(bytes));
            obsClient.putObject(request);
        } catch (ObsException e) {
            log.error("OBS 上传失败：Code={}, Message={}, RequestId={}, HostId={}",
                    e.getErrorCode(), e.getErrorMessage(), e.getErrorRequestId(), e.getErrorHostId());
            throw new RuntimeException("上传文件到华为云 OBS 失败", e);
        } catch (Exception e) {
            log.error("OBS 上传异常", e);
            throw new RuntimeException("上传文件到华为云 OBS 异常", e);
        } finally {
            try {
                obsClient.close();
            } catch (Exception e) {
                log.warn("OBS 客户端关闭失败", e);
            }
        }

        String fileUrl = String.format("https://%s.%s/%s", bucketName, endpoint.replace("https://", ""), objectName);
        log.info("文件上传到:{}", fileUrl);
        return fileUrl;
    }
}