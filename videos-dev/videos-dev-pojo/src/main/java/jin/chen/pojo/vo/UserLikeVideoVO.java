package jin.chen.pojo.vo;

import javax.persistence.Id;
import java.util.Date;

public class UserLikeVideoVO {
    //UsersVO信息
   public UsersVO usersVO;
   //是否喜欢该视频，即有没有关联关系
   public boolean isLikeVideo;

    public UsersVO getUsersVO() {
        return usersVO;
    }

    public boolean isLikeVideo() {
        return isLikeVideo;
    }

    public void setUsersVO(UsersVO usersVO) {
        this.usersVO = usersVO;
    }

    public void setLikeVideo(boolean likeVideo) {
        isLikeVideo = likeVideo;
    }
}