package jin.chen.interceptor;

import com.alibaba.fastjson.JSONObject;
import jin.chen.utils.RedisOperator;
import jin.chen.utils.VideoJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * controller拦截器
 */
public class ControllerInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisOperator redisOperator;

    public static  final  String USER_REDIS_SESSION = "user-redis-session";
    /**
     *对controller之前的相关路径进行拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object o) throws Exception {
        //请求传入的参数，放在header里面
        String userId = request.getHeader("userId");
        String token = request.getHeader("token");
        if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(token)){
            String redisToken = redisOperator.get(USER_REDIS_SESSION + ":" + userId);
            //获取redisToken看看是否过期
            if(StringUtils.isNotBlank(redisToken) && StringUtils.isNotEmpty(redisToken)){
                //判断请求传入的token和根据userId获取的redisToken是否相等，判断是否多账号登录
                if(!token.equals(redisToken)){
                    ErrorResponce(response, VideoJSONResult.errorTokenMsg("其它设备已登录"));
                    return false;
                }
            }else{
                ErrorResponce(response, VideoJSONResult.errorTokenMsg("请登录"));
                return  false;
            }
        }else{
            ErrorResponce(response, VideoJSONResult.errorTokenMsg("请登录"));
            return false;
        }
        return true;
    }

    /**
     * 抛出拦截到的错误
     */
    public void ErrorResponce(HttpServletResponse response, VideoJSONResult msg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json");
        OutputStream out = null;
        try{
            JSONObject jsonObject = new JSONObject();
            out = response.getOutputStream();
            out.write(jsonObject.toJSONString(msg).getBytes("UTF-8"));
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(out != null){
                out.close();
            }
        }

    }
    /**
     *controller之后，渲染视图之前，进行拦截
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     *渲染视图之后，进行拦截
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
