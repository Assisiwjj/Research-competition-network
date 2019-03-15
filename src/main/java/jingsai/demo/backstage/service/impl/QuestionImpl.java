package jingsai.demo.backstage.service.impl;

import jingsai.demo.backstage.dao.QuestionMapper;
import jingsai.demo.backstage.domain.Question;
import jingsai.demo.backstage.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Question> selectAll(){
        return questionMapper.selectAll();
    }
}
