package com.levelUp2.project_scaffolding_server.db.service;

import com.levelUp2.project_scaffolding_server.db.entity.Insertion;
import com.levelUp2.project_scaffolding_server.db.repo.InsertionRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class InsertionService {
    private final InsertionRepo ins;

    public InsertionService(InsertionRepo insertionRepo) {
        this.ins = insertionRepo;
    }

    public boolean saveInsertion(Insertion insertion) {
        Optional<Insertion> insertion1 = ins.findById(insertion.getId());
        if (insertion1.isEmpty()) {
            try {
                ins.save(insertion);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;

    }

    public void deleteScaff(String id) {
        ins.deleteById(id);
    }
}
