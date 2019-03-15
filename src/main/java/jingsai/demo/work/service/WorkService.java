package jingsai.demo.work.service;

import jingsai.demo.utils.PageBean;
import jingsai.demo.work.domain.Work;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkService {
    Long insert(Work work, String suffix,String data);

    String uploadImage(String suffix, String FileName, String data, Boolean insert);

    int updateImage(Long workId, String suffix,String data);

    String base64Data(Long workId);

    int updateFilePath(Long workId, String path);

    PageBean getPageBean(Integer limit, Integer page, String type, String name, String teamName);

    Work selectByWorkId(Long workId);

    int workDelete(Long workId);

    List<Work> selectByHits();

    List<Work> selectBykeyan();

    List<Work> selectByjingsai();

}
