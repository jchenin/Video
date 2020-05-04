package jin.chen.service;

import jin.chen.pojo.Bgm;
import jin.chen.pojo.Users;

import java.util.List;

public interface BgmService {

    /**
    查询背景音乐列表
     */
    public List<Bgm> queryListBgm();

    /**
     * 根据背景音乐id查询
     */
    public Bgm queryBgm(String bgmId);
}
