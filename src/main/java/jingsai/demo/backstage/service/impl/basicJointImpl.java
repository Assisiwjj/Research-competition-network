package jingsai.demo.backstage.service.impl;

import jingsai.demo.backstage.dao.BasicJointMapper;
import jingsai.demo.backstage.domain.BasicJoint;
import jingsai.demo.backstage.service.basicJoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class basicJointImpl implements basicJoint {
    @Autowired
    private BasicJointMapper basicJointMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return basicJointMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(BasicJoint record) {
        return basicJointMapper.insert(record);
    }

    @Override
    public BasicJoint selectByPrimaryKey(Integer id) {
        BasicJoint basicJoint = basicJointMapper.selectByPrimaryKey(id);

        return basicJoint;
    }

    @Override
    public int updateByPrimaryKey(BasicJoint record) {
        return basicJointMapper.updateByPrimaryKey(record);
    }

}
