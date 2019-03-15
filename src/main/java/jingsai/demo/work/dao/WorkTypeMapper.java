package jingsai.demo.work.dao;

import java.util.List;
import jingsai.demo.work.domain.WorkType;
import jingsai.demo.work.domain.WorkTypeExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface WorkTypeMapper {
    long countByExample(WorkTypeExample example);

    int deleteByExample(WorkTypeExample example);

    int deleteByPrimaryKey(Long pkId);

    int insert(WorkType record);

    int insertSelective(WorkType record);

    List<WorkType> selectByExample(WorkTypeExample example);

    WorkType selectByPrimaryKey(Long pkId);

    int updateByExampleSelective(@Param("record") WorkType record, @Param("example") WorkTypeExample example);

    int updateByExample(@Param("record") WorkType record, @Param("example") WorkTypeExample example);

    int updateByPrimaryKeySelective(WorkType record);

    int updateByPrimaryKey(WorkType record);
}