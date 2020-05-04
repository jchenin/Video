package jin.chen.service;

import jin.chen.pojo.Bgm;
import jin.chen.pojo.Comments;
import jin.chen.pojo.UsersReport;
import jin.chen.pojo.Videos;
import jin.chen.pojo.vo.VideosVO;
import jin.chen.utils.PagedResult;

import java.util.List;

public interface VideoService {

    /**
    保存视频信息
     */
    public String SaveVideo(Videos videos);

    /**
     * 更新视频信息
     */
    public void UploadVideo(String videoId, String coverPath);

    /**
     * 查询所有数据，视频分页,page  查询的页数， pageSize 显示的记录数
     */
    public PagedResult queryAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);

    /**
     * 查询收藏视频
     * @param video
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult queryCollectionVideos(Videos video, Integer page, Integer pageSize);

    /**
     * 查询关注视频
     * @param video
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult queryFollowVideos(Videos video, Integer page, Integer pageSize);


    /**
     * 查询所有的热搜词
     */
    public List<String> queryAllHots();

    /**
     * 视频获赞数增加
     * @param userId  点赞用户id
     * @param videoId  视频id
     * @param publishId 视频发布者id
     */
    public void addVideoLikeCounts(String userId, String videoId, String publishId);

    /**
     * 视频获赞数减少
     * @param userId  点赞用户id
     * @param videoId  视频id
     * @param publishId 视频发布者id
     */
    public void reduceVideoLikeCounts(String userId, String videoId, String publishId);

    /**
     * 保存举报信息
     * @param usersReport
     */
    public void saveReport(UsersReport usersReport);

    /**
     * 保存留言信息
     * @param comments
     */
    public void  saveComments(Comments comments);

    /**
     *
     * @param page  当前页数
     * @param pageSize  当前显示的记录数
     * @param videoId 评论pojo
     * @return
     */
    public PagedResult queryComments(Integer page, Integer pageSize, String videoId);
}
