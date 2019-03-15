package jingsai.demo.backstage.service.impl;

import jingsai.demo.backstage.dao.AdministratorMapper;
import jingsai.demo.backstage.domain.Administrator;
import jingsai.demo.backstage.domain.AdministratorExample;
import jingsai.demo.backstage.service.AdministratorService;
import jingsai.demo.team.domain.Team;
import jingsai.demo.team.domain.TeamExample;
import jingsai.demo.utils.SHA2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorImpl implements AdministratorService {
    @Autowired
    private AdministratorMapper administratorMapper;
    @Override
    public Boolean login(Administrator administrator) throws RuntimeException {
        try {
            administrator.setPassword(SHA2.SHA256(administrator.getPassword()));
            AdministratorExample example = new AdministratorExample();
            AdministratorExample.Criteria criteria = example.createCriteria();
            criteria.andNameEqualTo(administrator.getName());
            criteria.andPasswordEqualTo(administrator.getPassword());
            List<Administrator> list = administratorMapper.selectByExample(example);
            if (list != null && list.size() > 0) {
                return true;
            }
            else {
                return false;
                }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
