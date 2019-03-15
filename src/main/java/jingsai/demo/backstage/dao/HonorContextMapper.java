package jingsai.demo.backstage.dao;

import java.util.List;
import jingsai.demo.backstage.domain.HonorContext;
import jingsai.demo.backstage.domain.HonorContextExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface HonorContextMapper {
    long countByExample(HonorContextExample example);

    int deleteByExample(HonorContextExample example);

    int deleteByPrimaryKey(Long pkId);

    int insert(HonorContext record);

    int insertSelective(HonorContext record);

    List<HonorContext> selectByExample(HonorContextExample example);

    HonorContext selectByPrimaryKey(Long pkId);

    int updateByExampleSelective(@Param("record") HonorContext record, @Param("example") HonorContextExample example);

    int updateByExample(@Param("record") HonorContext record, @Param("example") HonorContextExample example);

    int updateByPrimaryKeySelective(HonorContext record);

    int updateByPrimaryKey(HonorContext record);
}