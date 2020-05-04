package jin.chen.controller;

import io.swagger.annotations.*;
import jin.chen.enums.VideoStatusEnum;
import jin.chen.mapper.CommentsMapper;
import jin.chen.mapper.UsersMapper;
import jin.chen.mapper.VideosMapper;
import jin.chen.pojo.*;
import jin.chen.service.BgmService;
import jin.chen.service.VideoService;
import jin.chen.utils.FFMpeg;
import jin.chen.utils.PagedResult;
import jin.chen.utils.VideoJSONResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Api(value = "视频相关接口", tags = "视频相关接口的controller")
@RequestMapping("/video")
public class VideoController extends CommonController{

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UsersMapper usersMapper;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id",  required = true, dataType = "String",
                    paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id",  required = false, dataType = "String",
                    paramType = "form"),
            @ApiImplicitParam(name = "videoTime", value = "视频时长",  required = true, dataType = "double",
                    paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度",  required = true, dataType = "int",
                    paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度",  required = true, dataType = "int",
                    paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述",  required = true, dataType = "String",
                    paramType = "form"),
    })
    @ApiOperation(value = "视频上传", notes = "视频上传的接口")
    @PostMapping(value = "/uploadVideo", headers = "content-type=multipart/form-data")
    public VideoJSONResult uploadVideo(String userId, String bgmId, double videoTime,
                                                                int videoHeight, int videoWidth, String desc,
                                       @ApiParam(value = "短视频", required = true) MultipartFile file){
        if(org.apache.commons.lang3.StringUtils.isBlank(userId)){
            return VideoJSONResult.errorMsg("userId不能为空");
        }
        //确定命名空间
//        String finalSpace = "E:/videos-dev";
        //保存数据库的相对路径
        String UploadDBPath = "/" + userId + "/video";
        String CoverDBPath = "/" + userId + "/cover";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        String finalVideoFilePath = "";
        String finalVideoCoverPath = "";
        try {
            if (file != null ) {
                String fileName = file.getOriginalFilename();
                //视频名字是video.mp4分割出前面的，\\用于转义.
                //jpg格式比png格式的图片内存小
                String coverFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";
                if (org.apache.commons.lang3.StringUtils.isNotBlank(fileName)) {
                    //文件保存的绝对路径
                    finalVideoFilePath = FINAL_SPACE + UploadDBPath + "/" + fileName;
                    finalVideoCoverPath = FINAL_SPACE + CoverDBPath + "/" + coverFileName;
                    UploadDBPath += ("/" + fileName);
                    CoverDBPath += ("/" + coverFileName);
                    File finalVideoFile = new File(finalVideoFilePath);
                    File finalVideoCoverFile = new File(finalVideoCoverPath);
                    if (finalVideoFile.getParentFile() != null || !finalVideoFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        finalVideoFile.getParentFile().mkdirs();
                    }
                    if (finalVideoCoverFile.getParentFile() != null || !finalVideoCoverFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        finalVideoCoverFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(finalVideoFile);
                    inputStream = file.getInputStream();
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
        FFMpeg ffMpeg = new FFMpeg(FFMPEGEXE);
        String composeVideoSuffixId = null;
        //bgmId不为空则要合并音视频
        if (StringUtils.isNotBlank(bgmId)){
            Bgm bgm = bgmService.queryBgm(bgmId);
            String musicPath = FINAL_SPACE + bgm.getPath();
            String videoPath = finalVideoFilePath;
            composeVideoSuffixId = UUID.randomUUID().toString().replaceAll("-", "") + ".mp4";
             String composeVideoName = videoPath + composeVideoSuffixId;
            ffMpeg.pruduct(videoPath,musicPath, videoTime, composeVideoName);
        }
        //保存视频信息到数据库
        Videos video = new Videos();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        video.setId(id);
        video.setUserId(userId);
        video.setAudioId(bgmId);
        video.setVideoDesc(desc);
        video.setVideoSeconds(new Double(videoTime).floatValue());
        video.setVideoWidth(videoWidth);
        video.setVideoHeight(videoHeight);
        video.setStatus(VideoStatusEnum.Success.getValue());
        ffMpeg.pruductCover(finalVideoFilePath, finalVideoCoverPath);
        video.setCoverPath(CoverDBPath);
        if(composeVideoSuffixId != null){
            video.setVideoPath(UploadDBPath + composeVideoSuffixId);
        }else{
            video.setVideoPath(UploadDBPath);
        }
        video.setTs(new Date());
        videoService.SaveVideo(video);
        return VideoJSONResult.ok();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id",  required = true, dataType = "String",
                    paramType = "form"),
            @ApiImplicitParam(name = "videoId", value = "视频主键id",  required = true, dataType = "String",
                    paramType = "form")
    })
    @ApiOperation(value = "视频封面上传", notes = "视频封面上传的接口")
    @PostMapping(value = "/uploadVideoCover", headers = "content-type=multipart/form-data")
    public VideoJSONResult uploadVideoCover(String userId, String videoId,
                                       @ApiParam(value = "短视频封面", required = true) MultipartFile file){
        if(org.apache.commons.lang3.StringUtils.isBlank(videoId)){
            return VideoJSONResult.errorMsg("videoId不能为空");
        }
        //确定命名空间
//        String finalSpace = "E:/videos-dev";
        //保存数据库的相对路径
        String UploadDBPath = "/" + userId + "/cover";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        String finalVideoFilePath = "";
        try {
            if (file != null ) {
                String fileName = file.getOriginalFilename();
                if (org.apache.commons.lang3.StringUtils.isNotBlank(fileName)) {
                    //文件保存的绝对路径
                    finalVideoFilePath = FINAL_SPACE + UploadDBPath + "/" + fileName;
                    UploadDBPath += ("/" + fileName);
                    File finalVideoFile = new File(finalVideoFilePath);
                    if (finalVideoFile.getParentFile() != null || !finalVideoFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        finalVideoFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(finalVideoFile);
                    inputStream = file.getInputStream();
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
        videoService.UploadVideo(videoId, UploadDBPath);
        return VideoJSONResult.ok();
    }

    /*
    @ApiOperation(value = "分页视频展现", notes = "分页视频展现的接口")
    这个接口swagger可以去掉，不然获取的也只是一个视频数组信息，没什么意义
    isSaveRecord     1      --   表示要保存
                               0     --    表示不需要保存，或者为空也不要保存
     */
    @PostMapping(value = "/showAllVideos")
    public VideoJSONResult showAllVideos(@RequestBody Videos video, Integer isSaveRecord, Integer page){
        if(page == null || "".equals(page)){
            page = 1;
        }
        if(isSaveRecord == null || "".equals(isSaveRecord)){
            isSaveRecord = 0;
        }
        PagedResult pagedResult = videoService.queryAllVideos(video, isSaveRecord, page, PAGE_SIZE);
        return VideoJSONResult.ok(pagedResult);
    }

    /**
     * 查询个人收藏视频
     * @param video
     * @param page
     * @return
     */
    @PostMapping(value = "/showAllCollectionVideos")
    public VideoJSONResult showAllCollectionVideos(@RequestBody Videos video, Integer page){
        if(page == null || "".equals(page)){
            page = 1;
        }
        PagedResult pagedResult = videoService.queryCollectionVideos(video, page, PAGE_SIZE);
        return VideoJSONResult.ok(pagedResult);
    }

    @PostMapping(value = "/showAllFollowVideos")
    public VideoJSONResult showAllFollowVideos(@RequestBody Videos video,Integer page){
        if(page == null || "".equals(page)){
            page = 1;
        }
        PagedResult pagedResult = videoService.queryFollowVideos(video, page, PAGE_SIZE);
        return VideoJSONResult.ok(pagedResult);
    }

    @PostMapping(value = "/showHot")
    public VideoJSONResult showHot(){
        List<String> listHots =  videoService.queryAllHots();
        return VideoJSONResult.ok(listHots);
    }

    @PostMapping(value = "/addVideoLikeCount")
    public VideoJSONResult addVideoLikeCount(String userId, String videoId, String publisherId){
        videoService.addVideoLikeCounts(userId, videoId, publisherId);
        return VideoJSONResult.ok();
    }

    @PostMapping(value = "/reduceVideoLikeCount")
    public VideoJSONResult reduceVideoLikeCount(String userId, String videoId, String publisherId){
        videoService.reduceVideoLikeCounts(userId, videoId, publisherId);
        return VideoJSONResult.ok();
    }

    @PostMapping(value = "/report")
    public VideoJSONResult report(@RequestBody UsersReport usersReport){
        videoService.saveReport(usersReport);
        return VideoJSONResult.ok("举报成功");
    }

    @PostMapping(value = "/saveComment")
    public VideoJSONResult saveComment(@RequestBody Comments comments,
                                       String fatherCommentId, String toUserId){
        if((fatherCommentId != null && fatherCommentId != "") && (toUserId != null && toUserId != "")){
            comments.setFatherCommentId(fatherCommentId);
            comments.setToUserId(toUserId);
        }
        videoService.saveComments(comments);
        return VideoJSONResult.ok();
    }

    @PostMapping(value = "queryComments")
    public VideoJSONResult queryComments(Integer page, Integer pageSize, String videoId){
        //page  查询的页数， pageSize 显示的记录数
        if(page == null){
            page = 1;
        }
        if(pageSize == null) {
            pageSize = 6;
        }
        if(videoId == null || videoId == ""){
            return VideoJSONResult.errorMsg("未知错误，视频Id不能为空");
        }
        PagedResult pagedResult = videoService.queryComments(page, pageSize, videoId);

        return VideoJSONResult.ok(pagedResult);
    }
}
