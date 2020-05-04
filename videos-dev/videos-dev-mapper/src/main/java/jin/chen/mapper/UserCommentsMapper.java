package jin.chen.mapper;

import jin.chen.pojo.Comments;
import jin.chen.pojo.vo.CommentsVO;
import jin.chen.utils.MyMapper;

import java.util.List;

public interface UserCommentsMapper extends MyMapper<Comments>  {
    /**
     * 根据视频id查找该视频对应的所有评论信息
     * @param videoId
     * @return
     */
    public List<CommentsVO> queryAllComments(String videoId);
}
