package jin.chen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jin.chen.pojo.Users;
import jin.chen.pojo.vo.UserLikeVideoVO;
import jin.chen.pojo.vo.UsersVO;
import jin.chen.service.UserService;
import jin.chen.utils.MD5Utils;
import jin.chen.utils.VideoJSONResult;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import  java.io.File;
import java.util.Date;
import java.util.UUID;

@RestController
@Api(value = "用户相关业务接口", tags = {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends CommonController{
    @Autowired
    private UserService userService;

    @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
                                    dataType = "String", paramType = "query")
    @ApiOperation(value = "用户头像上传", notes = "用户头像上传接口")
    @PostMapping("/uploadFace")
    public VideoJSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files){
        if(org.apache.commons.lang3.StringUtils.isBlank(userId)){
            return VideoJSONResult.errorMsg("userId不能为空");
        }
        //确定命名空间
//        String finalSpace = "E:/videos-dev";
        String finalSpace = "C:/videos-dev";
        //保存数据库的相对路径
        String UploadDBPath = "/" + userId + "/face";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {
                String fileName = files[0].getOriginalFilename();
                if (org.apache.commons.lang3.StringUtils.isNotBlank(fileName)) {
                    //文件保存的绝对路径
                    String finalFilePath = finalSpace + UploadDBPath + "/" + fileName;
                    UploadDBPath += ("/" + fileName);
                    File finalFile = new File(finalFilePath);
                    if (finalFile.getParentFile() != null || !finalFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        finalFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(finalFile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            }else{
                return VideoJSONResult.errorMsg("上传出错");
            }
        }catch (Exception e){
            e.printStackTrace();
            return VideoJSONResult.errorMsg("上传出错");
        }finally {
            if(fileOutputStream != null){
                try{
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(inputStream != null){
                try{
                    inputStream.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(UploadDBPath);
        userService.updateUserInfo(user);
        return VideoJSONResult.ok(UploadDBPath);
    }

    @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
            dataType = "String", paramType = "query")
    @ApiOperation(value = "查询用户信息", notes = "查询用户信息接口")
    @PostMapping("/queryUserInfo")
    public VideoJSONResult queryUserInfo(String userId, String fansId) throws Exception{
        if(org.apache.commons.lang3.StringUtils.isBlank(userId)){
            return VideoJSONResult.errorMsg("userId不能为空");
        }
        Users user = userService.queryUserInfo(userId);
        if(user == null){
            return VideoJSONResult.errorMsg("Id错误，查询不出数据");
        }
        UsersVO userVO = new UsersVO();
        boolean isFollow = userService.queryIsFollow(userId, fansId);
        userVO.setFollow(isFollow);
        BeanUtils.copyProperties(user, userVO);
        return VideoJSONResult.ok(userVO);
    }
    //新建一个方法只供前端游客查看视频时，查询视频发布者的信息
    @PostMapping("/queryUserInfoForVisitor")
    public VideoJSONResult queryUserInfoForVisitor(String userId) throws Exception{
        if(org.apache.commons.lang3.StringUtils.isBlank(userId)){
            return VideoJSONResult.errorMsg("userId不能为空");
        }
        Users user = userService.queryUserInfo(userId);
        if(user == null){
            return VideoJSONResult.errorMsg("Id错误，查询不出数据");
        }
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(user, userVO);
        return VideoJSONResult.ok(userVO);
    }

    @PostMapping("/queryUserLikeVideo")
    public VideoJSONResult queryUserLikeVideo(String userId, String videoId, String publisherId) throws Exception{
        if(org.apache.commons.lang3.StringUtils.isBlank(publisherId)){
            return VideoJSONResult.errorMsg("publisherId不能为空");
        }
        //查询视频发布者信息
        Users user = userService.queryUserInfo(publisherId);
        if(user == null){
            return VideoJSONResult.errorMsg("Id错误，查询不出数据");
        }
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(user, userVO);
        //service查询当前用户和当前视频有无关联关系
        boolean isLikeVideo = userService.isLikeVideo(userId, videoId);
        //新建vo类，将上面两个结果返回前端
        UserLikeVideoVO userLikeVideoVO = new UserLikeVideoVO();
        userLikeVideoVO.setLikeVideo(isLikeVideo);
        userLikeVideoVO.setUsersVO(userVO);
        return VideoJSONResult.ok(userLikeVideoVO);
    }

    @PostMapping("/becomeYourFans")
    public VideoJSONResult becomeYourFans(String publisherId, String fansId) throws Exception{
        if(org.apache.commons.lang3.StringUtils.isBlank(publisherId) || org.apache.commons.lang3.StringUtils.isBlank(fansId)){
            return VideoJSONResult.errorMsg("Id不能为空");
        }
        userService.becomeYourFans(publisherId, fansId);
        return VideoJSONResult.ok("关注成功");
    }

    @PostMapping("/becomeNotYourFans")
    public VideoJSONResult becomeNotYourFans(String publisherId, String fansId) throws Exception{
        if(org.apache.commons.lang3.StringUtils.isBlank(publisherId) || org.apache.commons.lang3.StringUtils.isBlank(fansId)){
            return VideoJSONResult.errorMsg("Id不能为空");
        }
        userService.becomeNotYourFans(publisherId, fansId);
        return VideoJSONResult.ok("取消关注成功");
    }
}
