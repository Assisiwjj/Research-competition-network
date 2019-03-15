package jingsai.demo.backstage.web;

import jingsai.demo.backstage.dao.QuestionMapper;
import jingsai.demo.backstage.domain.Question;
import jingsai.demo.backstage.service.QuestionService;
import jingsai.demo.team.domain.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/question")
public class QuestionController extends BaseController{
    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @RequestMapping( method = RequestMethod.GET)
    public Map<String, Object> getAllQuestion() {
        msg.clear();
        try{
            List<Question> list = questionService.selectAll();
            msg.put("code", "1");
            msg.put("msg","成功");
            msg.put("data", list);
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Map<String, Object> updateQuestion( @RequestBody Question question){
        msg.clear();
        try{
            if (question.getContext()!=null){
                question.setPkId(1L);
                if (questionMapper.updateByPrimaryKeySelective(question)==1){
                    msg.put("code", "1");
                    msg.put("msg", "成功");
                }else {
                    msg.put("code", "0");
                    msg.put("msg", "失败");
                }
            }else {
                msg.put("code", "0");
                msg.put("msg", "传参不正确");
            }
        }catch (Exception e){
            msg.put("code","0");
            msg.put("msg",e.getMessage());
        }
        return msg;
    }
}
