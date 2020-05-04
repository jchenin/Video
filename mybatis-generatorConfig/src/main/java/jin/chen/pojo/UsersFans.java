package jin.chen.pojo;

import javax.persistence.*;

@Table(name = "users_fans")
public class UsersFans {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "fans_id")
    private String fansId;

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
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return fans_id
     */
    public String getFansId() {
        return fansId;
    }

    /**
     * @param fansId
     */
    public void setFansId(String fansId) {
        this.fansId = fansId;
    }
}