package jin.chen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jin.chen.pojo.Bgm;
import jin.chen.pojo.Users;
import jin.chen.pojo.vo.UsersVO;
import jin.chen.service.BgmService;
import jin.chen.service.UserService;
import jin.chen.utils.VideoJSONResult;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@Api(value = "背景音乐相关接口", tags = {"背景音乐相关的controller"})
@RequestMapping("/bgm")
public class BgmController extends CommonController{
    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "查询背景音乐列表", notes = "查询背景音乐列表接口")
    @PostMapping("/queryBgmList")
    public VideoJSONResult queryBgmList() throws Exception{
        List<Bgm> listBgm =  bgmService.queryListBgm();
        return VideoJSONResult.ok(listBgm);
    }


}
