package jin.chen.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
public interface MyMapper<T> extends Mapper<T> , MySqlMapper<T> {
    //不能被扫描到
}

