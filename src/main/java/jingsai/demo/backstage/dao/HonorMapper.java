package jingsai.demo.backstage.dao;

import java.util.List;
import java.util.Map;

import jingsai.demo.backstage.domain.Honor;
import jingsai.demo.backstage.domain.HonorExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface HonorMapper {
    long countByExample(HonorExample example);

    int deleteByExample(HonorExample example);

    int deleteByPrimaryKey(Long pkId);

    int insert(Honor record);

    int insertSelective(Honor record);

    List<Honor> selectByExample(HonorExample example);

    Honor selectByPrimaryKey(Long pkId);

    int updateByExampleSelective(@Param("record") Honor record, @Param("example") HonorExample example);

    int updateByExample(@Param("record") Honor record, @Param("example") HonorExample example);

    int updateByPrimaryKeySelective(Honor record);

    int updateByPrimaryKey(Honor record);

    List<Map> selectAllPkId();
}