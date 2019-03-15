package jingsai.demo.work.service.impl;

import jingsai.demo.team.domain.Team;
import jingsai.demo.utils.Base64;
import jingsai.demo.utils.ConstantsW;
import jingsai.demo.utils.PageBean;
import jingsai.demo.work.dao.WorkMapper;
import jingsai.demo.work.domain.Work;
import jingsai.demo.work.domain.WorkExample;
import jingsai.demo.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class WorkImpl implements WorkService {
    @Autowired
    private WorkMapper workMapper;

    @Override
    public Long insert(Work work, String suffix,String data){
        try {
            work.setGmtCreate(new Date());
            work.setGmtModified(new Date());
            work.setHits(0L);
            work.setIsDelete(false);
            work.setPicture(uploadImage(suffix, null, data, true));
            if(workMapper.insert(work)==1){
                return work.getPkId();
            }else {
                return 0L;
            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String uploadImage(String suffix, String FileName, String data, Boolean insert) throws RuntimeException {
        String fileName;
        try
        {
            //Base64解码
            byte[] b = Base64Utils.decodeFromString(data);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {
                    //调整异常数据
                    b[i]+=256;
                }
            }
            if(!insert){
                fileName = FileName;
                File oldFile = new File(ConstantsW.getCurrenPath()+fileName);
                if(oldFile.exists()){
                    oldFile.delete();
                }
                fileName =  FileName.split("\\.")[0] + suffix;
            }else{
                fileName = UUID.randomUUID().toString() + suffix;
            }
            String imgFilePath = ConstantsW.getCurrenPath()+fileName;
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return fileName;
        }
        catch (Exception e)
        {
            return null;
        }
    }


    @Override
    public int updateImage(Long workId, String suffix,String data) {
        Work work = workMapper.selectByPrimaryKey(workId);
        work.setPicture(uploadImage(suffix, work.getPicture(), data, false));
        return workMapper.updateByPrimaryKey(work);
    }

    @Override
    public String base64Data(Long workId){
        Work work = workMapper.selectByPrimaryKey(workId);
        if(work.getPicture()==null){
            return null;
        }
        InputStream inputStream = null;
        byte[] data = null;
        String imgFile = ConstantsW.getCurrenPath() + work.getPicture();
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        String Data = "data:image/png;base64," + encoder.encode(data);
        return Data;
    }


    @Override
    public int updateFilePath(Long workId, String path){
        try {
            Work work = workMapper.selectByPrimaryKey(workId);
            work.setFilePath(path);
            return workMapper.updateByPrimaryKey(work);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public PageBean getPageBean(Integer limit, Integer page, String type, String name, String teamName) throws RuntimeException{
        try {
            WorkExample example = new WorkExample();
            WorkExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteEqualTo(false);

            WorkExample.Criteria criteria2 = example.or();
            criteria2.andIsDeleteEqualTo(false);

            if (name!= null) {
                criteria.andNameLike("%" + name + "%");
                criteria2.andNameLike("%" + name + "%");
            }
            if (type!= null) {

                criteria.andTypeEqualTo(type);
                criteria2.andTypeEqualTo(type);
            }
            if (teamName!= null) {
                criteria.andTeamNameLike("%" + teamName + "%");
                criteria2.andTeamNameLike("%" + teamName + "%");
            }
            int count = (int) workMapper.countByExample(example);
            if(count>0) {
                PageBean pageBean = new PageBean(page, count, limit);
                example.setLimit(limit);
                example.setOffset(pageBean.getStart());
                List<Work> workList = (workMapper.selectByExample(example));
                for(Work work : workList){
                    if(work.getPicture()!=null){
                        work.setPicture(Base64.toBase64W(work.getPicture()));
                    }
                }
                pageBean.setList(workList);
                return pageBean;
            }else {
                return null;
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Work selectByWorkId(Long workId)throws RuntimeException{
        try {
            Work work = workMapper.selectByPrimaryKey(workId);
            if (work!=null){
                if (work.getIsDelete()){
                    return null;
                }else {
                    workMapper.updateHitsByWorkId(workId);
                    work.setPicture(Base64.toBase64W(work.getPicture()));
                    return work;
                }
            }else{
                return null;
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int workDelete(Long workId)throws RuntimeException{
        try {
            Work work = new Work();
            work.setPkId(workId);
            work.setIsDelete(true);
            return workMapper.updateByPrimaryKeySelective(work);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Work> selectByHits()throws RuntimeException{
        try{
            WorkExample example =new WorkExample();
            WorkExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteEqualTo(false);
            example.setOrderByClause("hits desc");
            example.setLimit(8);
            example.setDistinct(true);
            List<Work> list = workMapper.selectByExample(example);
            for(Work work : list){
                if(work.getPicture()!=null){
                    work.setPicture(Base64.toBase64W(work.getPicture()));
                }
            }
            if (list!=null && list.size()>0){
                return list;
            }else {
                return null;
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Work> selectBykeyan()throws RuntimeException{
        try{
            WorkExample example =new WorkExample();
            WorkExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteEqualTo(false);
            List<String> list1 = new ArrayList<>();
            list1.add("校级");
            list1.add("市级");
            list1.add("省级");
            criteria.andTypeIn(list1);
            example.setLimit(8);
            example.setDistinct(true);
            List<Work> list = workMapper.selectByExample(example);
            for(Work work : list){
                if(work.getPicture()!=null){
                    work.setPicture(Base64.toBase64W(work.getPicture()));
                }
            }
            if (list!=null && list.size()>0){
                return list;
            }else {
                return null;
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Work> selectByjingsai()throws RuntimeException{
        try{
            WorkExample example =new WorkExample();
            WorkExample.Criteria criteria = example.createCriteria();
            criteria.andIsDeleteEqualTo(false);
            criteria.andTypeNotEqualTo("校级");
            criteria.andTypeNotEqualTo("市级");
            criteria.andTypeNotEqualTo("省级");
            example.setLimit(8);
            example.setDistinct(true);
            List<Work> list = workMapper.selectByExample(example);
            for(Work work : list){
                if(work.getPicture()!=null){
                    work.setPicture(Base64.toBase64W(work.getPicture()));
                }
            }
            if (list!=null && list.size()>0){
                return list;
            }else {
                return null;
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
