package jin.chen.controller;

import jin.chen.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @Autowired
    public RedisOperator redisOperator;

    /**
     *     redis session命名
     */
    public static  final  String USER_REDIS_SESSION = "user-redis-session";

    /**
     * 文件保持命名空间
     */
//    public static final String FINAL_SPACE = "E:/videos-dev";
    public static final String FINAL_SPACE = "C:/videos-dev";

    /**
     * ffmpeg软件所在位置
     */
//    public static final String FFMPEGEXE = "E:\\毕设\\项目必须软件\\windows\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe";
    public static final String FFMPEGEXE = "C:\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe";
    /**
     * 分页视频展现每页视频数量
     */
    public static final Integer PAGE_SIZE = 6;
}
