package com.levelUp2.project_scaffolding_server.control;

import com.levelUp2.project_scaffolding_server.db.entity.Scaff;
import com.levelUp2.project_scaffolding_server.db.service.ScaffService;
import com.levelUp2.project_scaffolding_server.lib.fs.FileSystem;

import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/scaff")
public class ScaffController {
    @PersistenceContext
    private EntityManager entityManager;

    private final ScaffService scaffService;

    public ScaffController(ScaffService scaffService) {
        this.scaffService = scaffService;
    }

    @GetMapping
    public List<Scaff> getAllScaffs() {
        return scaffService.getAllScaffs();
    }

    @GetMapping("/{id}")
    public Optional<Scaff> getScaffById(@PathVariable String id) {
        return scaffService.getScaffById(id);
    }

    @GetMapping("/{id}/options")
    public Map<String, Scaff> getOptions(@PathVariable String id) {
        return scaffService.getScaffChildren(id);
    }

    @PostMapping
    public Scaff createScaff(@RequestBody Scaff scaff) {
        return scaffService.saveScaff(scaff);
    }

    @DeleteMapping("/{id}")
    public void deleteScaff(@PathVariable String id) {
        scaffService.deleteScaff(id);
    }

    // ✨ Recursively backtracks through the scaff tree, obtaining the ✨
    // ✨ in-order steps required to generate the rendered file system ✨
    private static final String RENDER_SCAFF_QUERY = ""
        + "WITH RECURSIVE ancs(n, id, parent_id) AS ( "
        + "    SELECT 0, id, parent_id FROM scaffs WHERE id = :scaffId "
        + "    UNION ALL "
        + "    SELECT n+1, s.id, s.parent_id FROM scaffs s "
        + "    INNER JOIN ancs a ON s.id = a.parent_id "
        + "    WHERE a.id NOT LIKE '000%' "
        + "), "
        + "ins AS ( "
        + "    SELECT a.n, 0 AS step, a.id, a.parent_id, ins.filepath, NULL AS variable, ins.value "
        + "    FROM ancs a "
        + "    INNER JOIN insertion ins ON a.id = ins.scaff_id "
        + "), "
        + "sub AS ( "
        + "    SELECT a.n, 1 AS step, a.id, a.parent_id, NULL AS filepath, sub.variable, sub.value "
        + "    FROM ancs a "
        + "    INNER JOIN substitution sub ON a.id = sub.scaff_id "
        + ") "
        + "SELECT * FROM ins "
        + "UNION ALL "
        + "SELECT * FROM sub "
        + "ORDER BY n DESC, step ASC";

    // Returns the rendered file system at the given scaff ID
    @GetMapping("/{id}/rendered")
    public ResponseEntity<?> getRenderedScaff(@PathVariable String id) {
        // Create initial empty filesystem
        FileSystem fs = new FileSystem();

        Query query = entityManager.createNativeQuery(RENDER_SCAFF_QUERY);
        query.setParameter("scaffId", id);
        
        // Row format: [ (0:n),   (1:step),   (2:id),   (3:parent_id),   (4:filepath),   (5:variable),   (6:value) ]
        @SuppressWarnings("unchecked") // A little bit 'trust me bro' unfortunately
        List<Object[]> rows = query.getResultList();
        
        // Process each row in order
        for (Object[] row : rows) {
            int step = ((Number) row[1]).intValue(); // trust me bro
            if (step == 0) {
                //-- Insert file --//
                String filepath = (String) row[4];
                String content = (String) row[6];
                fs.insertFile(filepath, content);
            } else if (step == 1) {
                //-- Substitute var --//
                String variable = (String) row[5];
                String replacement = (String) row[6];
                fs.substituteVar(variable, replacement);
            }
        }

        // Build final JSON response
        Map<String, Object> response = new HashMap<>();
        response.put("vars", new HashMap<>()); // TODO: Populate "vars": { ... }
        response.put("files", fs.getFiles());
        
        return ResponseEntity.ok(response);
    }
}
