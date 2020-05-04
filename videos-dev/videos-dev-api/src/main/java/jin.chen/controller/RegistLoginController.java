package jin.chen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jin.chen.pojo.Users;
import jin.chen.pojo.vo.UsersVO;
import jin.chen.service.UserService;
import jin.chen.utils.MD5Utils;
import jin.chen.utils.VideoJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@Api(value = "用户注册登录接口", tags = {"用户注册登录的controller"})
public class RegistLoginController extends CommonController{
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册", notes = "用户注册的接口")
    @PostMapping("/regist")
    public VideoJSONResult regist(@RequestBody Users user){
        String userName = user.getUsername();
        String password = user.getPassword();
        //非空判断账号密码是否为空
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            return VideoJSONResult.errorMsg("账号、密码不能为空");
        }
        //查询不存在重复账号则保存数据
        boolean userNameExist = userService.queryUseExist(user.getUsername());
        if(!userNameExist){
            user.setNickname(user.getUsername());
            try {
                user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            }catch (Exception e){
                e.printStackTrace();
            }
            user.setFansCounts(0);
            user.setFollowCounts(0);
            user.setReceiveLikeCounts(0);
            user.setTs(new Date());
            userService.saveUser(user);
        }else {
            return VideoJSONResult.errorMsg("用户已存在，请换一个");
        }
        //返回前端时，密码设置为空，安全性
        user.setPassword("");
        UsersVO userVO = commonRedis(user);
        return VideoJSONResult.ok(userVO);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @PostMapping("/login")
    public VideoJSONResult login(@RequestBody Users user){
        String userName = user.getUsername();
        String password = user.getPassword();
        String queryPassword = "";
        //非空判断账号密码是否为空
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            return VideoJSONResult.errorMsg("账号、密码不能为空");
        }
        try{
            //查询时的密码也必须加密，因为保存时密码是加密的
             queryPassword = MD5Utils.getMD5Str(password);
        }catch (Exception e){
            e.printStackTrace();
        }
        //查询，非空则返回用户数据
        Users result = userService.queryUserSuccess(userName, queryPassword);
        if (result != null){
            //非空判断账号密码是否为空
            result.setPassword("");
            UsersVO userVO = commonRedis(result);
            return VideoJSONResult.ok(userVO);
        }else {
            return VideoJSONResult.errorMsg("账号或密码不正确");
        }
    }
    @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
                                    dataType = "String", paramType = "query")
    @ApiOperation(value = "用户注销", notes = "用户注销接口")
    @PostMapping("/logout")
    public VideoJSONResult logout(String userId){
        redisOperator.del(USER_REDIS_SESSION  + ":" + userId);
        return VideoJSONResult.ok();
    }

    //redis存放token
    public  UsersVO commonRedis( Users user){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisOperator.set(USER_REDIS_SESSION + ":" + user.getId(), token, 1000 * 60 * 60 * 24);
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setToken(token);
        return userVO;
    }
}
