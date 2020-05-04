package jin.chen.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jin.chen.mapper.*;
import jin.chen.pojo.*;
import jin.chen.pojo.vo.CommentsVO;
import jin.chen.pojo.vo.VideosVO;
import jin.chen.service.VideoService;
import jin.chen.utils.PagedResult;
import jin.chen.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class VideoServiceImp implements VideoService {

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private VideosUserMapper videosUserMapper;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersReportMapper usersReportMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private UserCommentsMapper userCommentsMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String SaveVideo(Videos videos) {
        videosMapper.insertSelective(videos);
        return videos.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void UploadVideo(String videoId, String coverPath) {
        Videos video = new Videos();
        video.setId(videoId);
        video.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(video);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult queryAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {
        String content = video.getVideoDesc();
        //1  表示要保存热搜词
        if(isSaveRecord != null && isSaveRecord == 1){
            SearchRecords searchRecords = new SearchRecords();
            searchRecords.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            searchRecords.setContent(content);
            searchRecordsMapper.insertSelective(searchRecords);
        }
        //page  查询的页数， pageSize 显示的记录数
        PageHelper.startPage(page, pageSize);
        String userId = video.getUserId();
        List<VideosVO> listVideos =  videosUserMapper.queryAllVideos(content, userId);
        //PageInfo类相当于包装了下listVideos,设置了一些总页数，记录数等
        PageInfo<VideosVO> pageInfo = new PageInfo<>(listVideos);
        PagedResult pagedResult = new PagedResult();
        //当前页数
        pagedResult.setCurrentPage(page);
        //总页数
        pagedResult.setTotalPages(pageInfo.getPages());
        //总记录数
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(listVideos);
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryCollectionVideos(Videos video, Integer page, Integer pageSize) {
        String userId = video.getUserId();
        //开始分页，page 页数，pageSize该页显示的数量
        PageHelper.startPage(page, pageSize);
        List<VideosVO> listVideos  = videosUserMapper.queryCollectionVideos(userId);
        PageInfo<VideosVO> pageInfo = new PageInfo<>(listVideos);
        PagedResult pagedResult = new PagedResult();
        //当前页数
        pagedResult.setCurrentPage(page);
        //总页数
        pagedResult.setTotalPages(pageInfo.getPages());
        //总记录数
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(listVideos);
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryFollowVideos(Videos video, Integer page, Integer pageSize) {
        String userId = video.getUserId();
        //开始分页，page 页数，pageSize该页显示的数量
        PageHelper.startPage(page, pageSize);
        List<VideosVO> listVideos  = videosUserMapper.queryFollowVideos(userId);
        PageInfo<VideosVO> pageInfo = new PageInfo<>(listVideos);
        PagedResult pagedResult = new PagedResult();
        //当前页数
        pagedResult.setCurrentPage(page);
        //总页数
        pagedResult.setTotalPages(pageInfo.getPages());
        //总记录数
        pagedResult.setRecords(pageInfo.getTotal());
        pagedResult.setRows(listVideos);
        return pagedResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> queryAllHots() {
        return searchRecordsMapper.queryAllHots();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addVideoLikeCounts(String userId, String videoId, String publishId) {
        //1、先创建关联表的数据
        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        usersLikeVideos.setId(id);
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setVideoId(videoId);
        usersLikeVideosMapper.insertSelective(usersLikeVideos);
        //2、处理数据
        videosUserMapper.addVideoLikeCounts(videoId);
        usersMapper.addLikeCounts(publishId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void reduceVideoLikeCounts(String userId, String videoId, String publishId) {
        //删除关联表数据
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);
        usersLikeVideosMapper.deleteByExample(example);
        //处理数据
        videosUserMapper.reduceVideoLikeCounts(videoId);
        usersMapper.reduceLikeCounts(publishId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveReport(UsersReport usersReport) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        usersReport.setId(id);
        usersReport.setTs(new Date());
        usersReportMapper.insert(usersReport);
    }

    @Override
    public void saveComments(Comments comments) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        comments.setId(id);
        comments.setTs(new Date());
        commentsMapper.insert(comments);
    }

    @Override
    public PagedResult queryComments(Integer page, Integer pageSize, String videoId) {
        List<CommentsVO> list = userCommentsMapper.queryAllComments(videoId);
        for (CommentsVO c : list){
            String timeAgo = TimeUtils.format(c.getTs());
            c.setTimeAgo(timeAgo);
        }
        //开始分页
        PageHelper.startPage(page, pageSize);
        //包装查询到的数据
        PageInfo<CommentsVO> pageInfo = new PageInfo<>(list);
        PagedResult pagedResult = new PagedResult();
        //当前页数
        pagedResult.setCurrentPage(page);
        //总页数
        pagedResult.setTotalPages(pageInfo.getPages());
        //总记录数
        pagedResult.setRecords(pageInfo.getTotal());
        //数据list
        pagedResult.setRows(list);
        return pagedResult;
    }
}
