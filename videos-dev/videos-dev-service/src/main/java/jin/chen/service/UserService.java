package jin.chen.service;

import jin.chen.pojo.Users;

public interface UserService {

    /**
    查询用户是否存在
     */
    public boolean queryUseExist(String userName);

    /**
    保存用户
     */
    public void saveUser(Users user);

    /**
    登录查询用户密码是否正确
     */
    public Users queryUserSuccess(String userName, String password);

    /**
    更改用户信息
     */
    public void updateUserInfo(Users user);

    /**
     * 查询用户信息
     */
    public Users queryUserInfo(String userId);

    /**
     * 查询当前用户是否喜欢点开的视频，是否有关联关系
     * @param userId  当前登录用户id
     * @param videoId  当前点开的视频id
     * @return
     */
    public boolean isLikeVideo(String userId, String videoId);

    /**
     * 点击关注，成为某人粉丝
     * @param publisherId  视频发布者id
     * @param fansId         粉丝id
     */
    public  void  becomeYourFans(String publisherId, String fansId);

    /**
     * 取消关注
     * @param publisherId  视频发布者id
     * @param fansId         粉丝id
     */
    public  void  becomeNotYourFans(String publisherId, String fansId);

    public  boolean queryIsFollow(String userId, String fansId);
}
