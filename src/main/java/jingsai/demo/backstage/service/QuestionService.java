package jingsai.demo.backstage.service;

import jingsai.demo.backstage.domain.Question;

import java.util.List;


public interface QuestionService {
    List<Question> selectAll();
}
