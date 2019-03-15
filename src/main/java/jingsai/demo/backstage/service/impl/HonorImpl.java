package jingsai.demo.backstage.service.impl;

import jingsai.demo.backstage.dao.HonorMapper;
import jingsai.demo.backstage.domain.Honor;
import jingsai.demo.backstage.domain.HonorExample;
import jingsai.demo.backstage.service.HonorService;
import jingsai.demo.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HonorImpl implements HonorService {
    @Autowired
    private HonorMapper honorMapper;

    @Override
    public PageBean getPageBean(Integer limit, Integer page){
        try {
            HonorExample example = new HonorExample();
            HonorExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteEqualTo(false);
            int count = (int) honorMapper.countByExample(example);
            if (count > 0) {
                PageBean pageBean = new PageBean(page, count, limit);
                example.setLimit(limit);
                example.setOffset(pageBean.getStart());
                List<Honor> honorList = honorMapper.selectByExample(example);
                pageBean.setList(honorList);
                return pageBean;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Honor selectById(Long pkId){
        try{
            Honor honor = honorMapper.selectByPrimaryKey(pkId);
            if (honor!=null){
                return honor;
            }else{
                return null;
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int honorDelete(Long honorId)throws RuntimeException{
        try {
            Honor honor = new Honor();
            honor.setPkId(honorId);
            honor.setIsDelete(true);
            return honorMapper.updateByPrimaryKeySelective(honor);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Long insert(Honor honor) {
        honor.setGmtCreate(new Date());
        honor.setGmtModified(new Date());
        honor.setIsDelete(false);
        if (honorMapper.insert(honor)==1){
            return honor.getPkId();
        }else{
            return 0L;
        }
    }
}
