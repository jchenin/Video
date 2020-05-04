package jin.chen.pojo;

import javax.persistence.*;

public class Bgm {
    @Id
    private String id;

    private String author;

    private String name;

    private String path;

    private String duration;

    private String isautoplay;

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
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return isautoplay
     */
    public String getIsautoplay() {
        return isautoplay;
    }

    /**
     * @param isautoplay
     */
    public void setIsautoplay(String isautoplay) {
        this.isautoplay = isautoplay;
    }
}