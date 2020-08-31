package com.mtons.mblog.web.controller.qny;

import com.mtons.mblog.base.lang.Consts;
import com.mtons.mblog.base.utils.FileKit;
import com.mtons.mblog.base.utils.QnyUtils;
import com.mtons.mblog.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Ueditor 文件上传
 *
 * @author langhsu
 */
@Controller
@RequestMapping("/qny")
public class QnyController extends BaseController
{
    public static HashMap<String, String> errorInfo = new HashMap<>();

    static {
        errorInfo.put("SUCCESS", "SUCCESS"); //默认成功
        errorInfo.put("NOFILE", "未包含文件上传域");
        errorInfo.put("TYPE", "不允许的文件格式");
        errorInfo.put("SIZE", "文件大小超出限制，最大支持100Mb");
        errorInfo.put("ENTYPE", "请求类型ENTYPE错误");
        errorInfo.put("REQUEST", "上传请求异常");
        errorInfo.put("IO", "IO异常");
        errorInfo.put("DIR", "目录创建失败");
        errorInfo.put("UNKNOWN", "未知错误");
    }

    @PostMapping("/upload")
    @ResponseBody
    public com.mtons.mblog.web.controller.site.posts.UploadController.UploadResult upload(@RequestParam(value = "file", required = false) MultipartFile file,
        HttpServletRequest request) throws IOException
    {
        com.mtons.mblog.web.controller.site.posts.UploadController.UploadResult
            result = new com.mtons.mblog.web.controller.site.posts.UploadController.UploadResult();
        String crop = request.getParameter("crop");
        int size = ServletRequestUtils.getIntParameter(request, "size", siteOptions.getIntegerValue(Consts.STORAGE_MAX_WIDTH));

        // 检查空
        if (null == file || file.isEmpty()) {
            return result.error(errorInfo.get("NOFILE"));
        }

        String fileName = file.getOriginalFilename();

        // 检查类型
        if (!FileKit.checkFileType(fileName)) {
            return result.error(errorInfo.get("TYPE"));
        }

        // 检查大小
        String limitSize = siteOptions.getValue(Consts.STORAGE_LIMIT_SIZE);
        if (StringUtils.isBlank(limitSize)) {
            limitSize = "100";
        }
        if (file.getSize() > (Long.parseLong(limitSize) * 1024 * 1024)) {
            return result.error(errorInfo.get("SIZE"));
        }

        // 保存图片
        try {
            String path = QnyUtils.uploadFile(file);

            result.ok(errorInfo.get("SUCCESS"));
            result.setName(fileName);
            result.setPath(path);
            result.setSize(file.getSize());

        } catch (Exception e) {
            result.error(errorInfo.get("UNKNOWN"));
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping("/down")
    @ResponseBody
    public void down(@RequestParam String sourceUrl, @RequestParam String fileName,HttpServletResponse response) throws Exception
    {
        QnyUtils.download(sourceUrl,fileName,response);
    }
}
