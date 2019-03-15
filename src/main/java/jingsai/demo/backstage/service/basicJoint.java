package jingsai.demo.backstage.service;

import jingsai.demo.backstage.domain.BasicJoint;

public interface basicJoint {
    int deleteByPrimaryKey(Integer id);

    int insert(BasicJoint record);

    BasicJoint selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(BasicJoint record);


}
