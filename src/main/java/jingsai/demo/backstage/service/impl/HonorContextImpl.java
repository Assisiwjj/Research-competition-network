package jingsai.demo.backstage.service.impl;

import jingsai.demo.backstage.dao.HonorContextMapper;
import jingsai.demo.backstage.domain.HonorContext;
import jingsai.demo.backstage.domain.HonorContextExample;
import jingsai.demo.backstage.service.HonorContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HonorContextImpl implements HonorContextService {
    @Autowired
    private HonorContextMapper honorContextMapper;

    @Override
    public int insert(HonorContext honorContext){
        honorContext.setGmtCreate(new Date());
        honorContext.setGmtModified(new Date());
        return honorContextMapper.insert(honorContext);
    }

    @Override
    public List<HonorContext> selectByHonorId(Long honorId) throws RuntimeException{
        try {
            HonorContextExample example = new HonorContextExample();
            HonorContextExample.Criteria criteria = example.createCriteria();
            criteria.andHonorIdEqualTo(honorId);
            return honorContextMapper.selectByExample(example);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
