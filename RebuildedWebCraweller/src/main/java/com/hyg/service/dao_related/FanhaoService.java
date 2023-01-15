package com.hyg.service.dao_related;

import com.hyg.dao.FanhaoDao;
import com.hyg.domain.Fanhao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("fanhaoService")
public class FanhaoService {
    @Autowired
    private FanhaoDao fanhaoDao;

    public List<Fanhao> findAll(){
        return fanhaoDao.findAll();
    }

    public int saveFanhao(Fanhao fanhao){
        return fanhaoDao.saveFanhao(fanhao);
    }

    public int updateFanhao(Fanhao fanhao){
        return fanhaoDao.update(fanhao);
    }

    public List<Fanhao> findByFanhao(String fanhao){
        return fanhaoDao.findByFanhao(fanhao);
    }

    public Fanhao getResultByFanhao(String fanhao){
        List<Fanhao> fanhaos = this.findByFanhao(fanhao);

        if (fanhaos.size() != 0){
            return fanhaos.get(0);
        }

        return new Fanhao();
    }
}
