package jin.chen.mapper;

import jin.chen.pojo.Videos;
import jin.chen.pojo.vo.VideosVO;
import jin.chen.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosUserMapper extends MyMapper<Videos> {
    /**
     *查询所有视频
     */
    public List<VideosVO> queryAllVideos(@Param("content") String content, @Param("userId") String userId);

    /**
     * 查询收藏视频
     * @param userId
     * @return
     */
    public List<VideosVO> queryCollectionVideos(@Param("userId") String userId);

    /**
     * 查询关注视频
     * @param userId
     * @return
     */
    public List<VideosVO> queryFollowVideos(@Param("userId") String userId);


    /**
     * 视频获赞数增加
     * @param videoId  视频id
     */
    public void addVideoLikeCounts(String videoId);

    /**
     * 视频获赞数减少
     * @param videoId  视频id
     */
    public void reduceVideoLikeCounts(String videoId);

}