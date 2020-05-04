package jin.chen.mapper;

import jin.chen.pojo.Users;
import jin.chen.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {

    /**
     * 用户收到的点赞数增加
     * @param userId  用户id
     */
    public void addLikeCounts(String userId);

    /**
     * 用户收到的点赞数减少
     * @param userId  用户id
     */
    public void reduceLikeCounts(String userId);

    /**
     * 增加粉丝数量
     * @param publisherId  视频发布者id
     */
    public  void addFansCount(String publisherId);

    /**
     * 减少粉丝数量
     * @param publisherId  视频发布者id
     */
    public  void reduceFansCount(String publisherId);

    /**
     * 增加关注数量
     * @param publisherId  视频发布者id
     */
    public  void addFollowCount(String publisherId);

    /**
     * 减少关注数量
     * @param publisherId  视频发布者id
     */
    public  void reduceFollowCount(String publisherId);
}