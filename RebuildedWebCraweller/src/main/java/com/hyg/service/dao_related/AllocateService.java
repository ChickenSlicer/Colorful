package com.hyg.service.dao_related;

import com.hyg.dao.FirstAllocateDao;
import com.hyg.dao.SecondAllocateDao;
import com.hyg.domain.Allocates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllocateService {
    @Autowired
    private FirstAllocateDao firstAllocateDao;
    @Autowired
    private SecondAllocateDao secondAllocateDao;

    public List<String> getAllocateFanhao(int type){
        List<Allocates> allocates = null;
        List<String> result = new ArrayList<>();

        if (type == 1)
            allocates = firstAllocateDao.findAll();
        else if (type == 2)
            allocates = secondAllocateDao.findAll();

        if (allocates == null)
            return result;
        else {
            for (Allocates allocate : allocates) {
                result.add(allocate.getFanhao());
            }
        }

        return result;
    }

    public void deleteAllocate(int type, String fanhao){
        if (type == 1)
            firstAllocateDao.delete(fanhao);
        else if (type == 2)
            secondAllocateDao.delete(fanhao);
    }

    public boolean addAllocate(int type, String fanhao){
        List<Allocates> allocated = null;
        boolean insertSuccess = false;

        if (type == 1) {
            allocated = firstAllocateDao.findAll();
            insertSuccess = insertOperation(type, allocated, fanhao);
        }
        else if (type == 2) {
            allocated = secondAllocateDao.findAll();
            insertSuccess = insertOperation(type, allocated, fanhao);
        }

        return insertSuccess;
    }

    private boolean insertOperation(int type, List<Allocates> allocated, String fanhao){
        boolean insertSuccess = false;

        if (allocated == null) {
            if (type == 1)
                firstAllocateDao.insertAllocate(fanhao);
            else if (type == 2)
                secondAllocateDao.insertAllocate(fanhao);

            insertSuccess = true;
        }
        else {
            boolean flag = true;

            for (Allocates allocates : allocated) {
                if (allocates.getFanhao().equals(fanhao)) {
                    flag = false;
                    break;
                }
            }

            if (flag){
                if (type == 1)
                    firstAllocateDao.insertAllocate(fanhao);
                else if (type == 2)
                    secondAllocateDao.insertAllocate(fanhao);

                insertSuccess = true;
            }
        }

        return insertSuccess;
    }
}
