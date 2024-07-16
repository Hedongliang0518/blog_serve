package com.hdl.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.hdl.domain.ResponseResult;
import com.hdl.enums.AppHttpCodeEnum;
import com.hdl.exception.SystemException;
import com.hdl.service.UploadService;
import com.hdl.utils.PathUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
public class OssUploadService implements UploadService {
    /**
     * 上传接口
     * @param img
     * @return
     * @throws IOException
     */
    @Override
    public ResponseResult uploadImg(MultipartFile img) throws IOException {
        //判断文件类型
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //对原始文件名进行判断
        if(!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //如果判断通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
//        String url = uploadOss(filePath,img.getInputStream());
        String url = uploadOss(img, filePath);

        return ResponseResult.okResult(url);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;
    private String ossPath;

    /**
     * 上传七牛云
     * @param imgFile
     * @param filePath
     * @return
     */
    private String uploadOss(MultipartFile imgFile, String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return ossPath+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }

//    private String uploadFile(String filePath, InputStream inputStream) {
//        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//        String endpoint = "sgpi0ovir.hd-bkt.clouddn.com";

//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
//        // 创建PutObjectRequest对象。
//        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filePath, inputStream);
//
//        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
//        // ObjectMetadata metadata = new ObjectMetadata();
//        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
//        // metadata.setObjectAcl(CannedAccessControlList.Private);
//        // putObjectRequest.setMetadata(metadata);
//
//        // 上传字符串。
//        PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//        return "https://"+bucket+"."+endpoint+"/"+filePath;
//    }
}
