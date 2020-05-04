package jin.chen.imp;

import jin.chen.mapper.UsersFansMapper;
import jin.chen.mapper.UsersLikeVideosMapper;
import jin.chen.mapper.UsersMapper;
import jin.chen.pojo.Users;
import jin.chen.pojo.UsersFans;
import jin.chen.pojo.UsersLikeVideos;
import jin.chen.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;
@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersFansMapper usersFansMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUseExist(String userName) {
        Users user = new Users();
        user.setUsername(userName);
        Users result = usersMapper.selectOne(user);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        user.setId(id);
        usersMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserSuccess(String userName, String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", userName);
        criteria.andEqualTo("password", password);
        Users result = usersMapper.selectOneByExample(example);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(Users user) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", user.getId());
        usersMapper.updateByExampleSelective(user,example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", userId);
        Users user = usersMapper.selectOneByExample(example);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean isLikeVideo(String userId, String videoId) {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)){
            return  false;
        }
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);
        List<UsersLikeVideos> result =  usersLikeVideosMapper.selectByExample(example);
        if(result != null && result.size() > 0){
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void becomeYourFans(String publisherId, String fansId) {
        //建立表关系
        UsersFans usersFans = new UsersFans();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        usersFans.setId(id);
        usersFans.setUserId(publisherId);
        usersFans.setFansId(fansId);
        usersFansMapper.insertSelective(usersFans);
        usersMapper.addFansCount(publisherId);
        usersMapper.addFollowCount(fansId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void becomeNotYourFans(String publisherId, String fansId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", publisherId);
        criteria.andEqualTo("fansId", fansId);
        usersFansMapper.deleteByExample(example);
        usersMapper.reduceFansCount(publisherId);
        usersMapper.reduceFollowCount(fansId);
    }

    @Override
    public boolean queryIsFollow(String userId, String fansId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("fansId", fansId);
        List<UsersFans> list = usersFansMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return true;
        }
        return false;
    }
}
