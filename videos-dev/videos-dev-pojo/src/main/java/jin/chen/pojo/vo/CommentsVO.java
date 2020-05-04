package jin.chen.pojo.vo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class CommentsVO {

    private String id;

    private String videoId;

    private String fromUserId;

    private Date ts;

    private String comment;

    private String faceImage;

    private String nickname;

    private String toNickname;

    private String timeAgo;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return video_id
     */
    public String getVideoId() {
        return videoId;
    }

    /**
     * @param videoId
     */
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    /**
     * @return from_user_id
     */
    public String getFromUserId() {
        return fromUserId;
    }

    /**
     * @param fromUserId
     */
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    /**
     * @return ts
     */
    public Date getTs() {
        return ts;
    }

    /**
     * @param ts
     */
    public void setTs(Date ts) {
        this.ts = ts;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getToNickname() {
        return toNickname;
    }

    public void setToNickname(String toNickname) {
        this.toNickname = toNickname;
    }
}
