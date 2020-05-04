package jin.chen.imp;

import jin.chen.mapper.BgmMapper;
import jin.chen.mapper.UsersMapper;
import jin.chen.pojo.Bgm;
import jin.chen.pojo.Users;
import jin.chen.service.BgmService;
import jin.chen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

@Service
public class BgmServiceImp implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryListBgm() {
        return bgmMapper.selectAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm queryBgm(String bgmId) {
        Example example = new Example(Bgm.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", bgmId);
        return bgmMapper.selectOneByExample(example);
    }
}
