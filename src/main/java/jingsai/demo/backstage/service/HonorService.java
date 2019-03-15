package jingsai.demo.backstage.service;

import jingsai.demo.backstage.domain.Honor;
import jingsai.demo.utils.PageBean;

public interface HonorService {
    PageBean getPageBean(Integer limit, Integer page);

    Honor selectById(Long pkId);

    int honorDelete(Long honorId);

    Long insert(Honor honor);
}
