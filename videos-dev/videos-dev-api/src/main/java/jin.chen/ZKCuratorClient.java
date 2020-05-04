package jin.chen;

import jin.chen.mapper.BgmMapper;
import jin.chen.pojo.Bgm;
import jin.chen.service.BgmService;
import jin.chen.utils.JSONUtils;
import org.apache.commons.io.FileUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class ZKCuratorClient {

    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private BgmService bgmService;

    @Autowired
    private ResourceConfig resourceConfig;
    //客户端
    private CuratorFramework client = null;
    final static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);
    //zookeeper所在地址
//    private static final String ZOOKEEPER_PATH  = "49.234.207.183:2181";

    //初始化
    public void init(){
        if(client != null){
            return;
        }
        //创建重连策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 6);

        //创建客户端
        client = CuratorFrameworkFactory.builder().sessionTimeoutMs(6000).connectionTimeoutMs(6000)
                    .connectString(resourceConfig.getZookeeperServer()).retryPolicy(retryPolicy).namespace("admin").build();

        //启动客户端
        client.start();
        try {
//            String result = new String(client.getData().forPath("/bgm/22daa5367b924639bd8bcd998dd8e4ad"));
//            log.info("获取到的结果 ：" + result);
            addChilePathListen("/bgm");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    创建一次监听，后续会一直监听
     */
    public void  addChilePathListen(String childNode) throws Exception{
        //监听缓存
        final PathChildrenCache cache = new PathChildrenCache(client, childNode, true);
        //启动
        cache.start();
        //创建监听
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                if(pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
//                    log.info("获取到的监听" );
                    //获取文件在zk服务器的路径,类似于这种  /admin/bgm/test1
                    String path = pathChildrenCacheEvent.getData().getPath();
                    //操作类型，1  增加   2 删除
                    String operatorObj = new String(pathChildrenCacheEvent.getData().getData(), "UTF-8");
                    Map<String, String> map = JSONUtils.jsonToPojo(operatorObj, Map.class);
                    String filePath = URLDecoder.decode(map.get("path"), "UTF-8");
                    String type = map.get("operatorType");
                    //分割路径，最后一个就是bgmId
//                    String [] arr = path.split("/");
//                    String bgmId = arr[arr.length - 1];
//                    //获取到Bgm对象,根据主键
//                   Bgm bgm =  bgmService.queryBgm(bgmId);
//                   //获取bgm在数据库中的存储路径,数据库中存储路径是\bgm\23
//                    String filePath = bgm.getPath();

                    //设置保存到本地的bgm路径
//                    String finalPath = "E:\\videos-dev" + filePath;
                    String finalPath = resourceConfig.getFileSpace()+ filePath;
                    //定义下载bgm路径
//                    String[] arrPath = filePath.split("\\\\");            //windows环境是\\
                    String[] arrPath = filePath.split("/");            //linux环境是/
                    String tmpPath = "";
                    for(int i = 0; i < arrPath.length; i++){
                        if(arrPath[i] != null && !"".equals(arrPath[i])){
                            tmpPath += "/";
                            //防止中文乱码
                            tmpPath += URLEncoder.encode(arrPath[i], "UTF-8");
                        }
                    }
//                    String downloadUrl = "http://192.168.1.105:8080/mvc-bgm" + tmpPath;
                    String downloadUrl = (resourceConfig.getBgmServer() + tmpPath).replaceAll("\\+", "%20");
                    if(type.equals("1")) {
                        //下载bgm到本地
                        URL url = new URL(downloadUrl);
                        File file = new File(finalPath);
                        FileUtils.copyURLToFile(url, file);
                        client.delete().forPath(path);
                    }else if(type.equals("2")){
                        File file = new File(finalPath);
                        FileUtils.forceDelete(file);
                        client.delete().forPath(path);
                    }
                }
            }
        });
    }
}
