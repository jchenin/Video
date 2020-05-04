package jin.chen;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="jin.chen")
@PropertySource("classpath:properties")
public class ResourceConfig {
    private String zookeeperServer;
    private String bgmServer;
    private String fileSpace;

    public String getZookeeperServer() {
        return zookeeperServer;
    }

    public String getBgmServer() {
        return bgmServer;
    }

    public String getFileSpace() {
        return fileSpace;
    }

    public void setZookeeperServer(String zookeeperServer) {
        this.zookeeperServer = zookeeperServer;
    }

    public void setBgmServer(String bgmServer) {
        this.bgmServer = bgmServer;
    }

    public void setFileSpace(String fileSpace) {
        this.fileSpace = fileSpace;
    }
}
