package jin.chen.mapper;

import jin.chen.pojo.SearchRecords;
import jin.chen.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    public List<String> queryAllHots();
}