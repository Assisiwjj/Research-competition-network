package jingsai.demo.backstage.service;

import jingsai.demo.backstage.domain.HonorContext;

import java.util.List;

public interface HonorContextService {
    int insert(HonorContext honorContext);

    List<HonorContext> selectByHonorId(Long honorId);
}
