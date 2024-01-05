package com.fpt.fptproducthunt.changelog.service;

import com.fpt.fptproducthunt.changelog.repository.ChangelogRepository;
import com.fpt.fptproducthunt.common.entity.Changelog;
import com.fpt.fptproducthunt.common.entity.Project;
import com.fpt.fptproducthunt.common.metadata.ChangelogStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChangelogServiceImpl implements ChangelogService {

    @Autowired
    private ChangelogRepository changelogRepository;
    @Override
    public List<Changelog> getAll(UUID projectId) {
        return changelogRepository.findAllByProjectId(projectId);
    }

    @Override
    public Page<Changelog> getAll(UUID projectId, int pageNo, int pageSize, String sortBy, String ascending, ChangelogStatus changelogStatus) {
        System.out.println(sortBy);
        PageRequest paging = PageRequest.of(pageNo, pageSize, ascending.equals("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        int[] status = changelogStatus == null ? new int[]{0, 1} : new int[]{changelogStatus.ordinal()};

        Page<Changelog> pagedResult = changelogRepository.findAllByProjectId(projectId.toString(), paging, status);

        return pagedResult;
    }

    @Override
    public Optional<Changelog> get(UUID id) {
        return changelogRepository.findById(id);
    }

    @Override
    public Changelog create(Changelog changelog) {
        return changelogRepository.save(changelog);
    }

    @Override
    public Changelog update(UUID id, String title, String description) {
        Changelog changelog = null;

        try {
             changelog = changelogRepository.findById(id).orElseThrow(() -> new Exception());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        changelog.setDescription(description);
        changelog.setTitle(title);

        return changelogRepository.save(changelog);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        changelogRepository.deleteChangelogById(id);
    }

    @Override
    public Changelog changeStatus(UUID id, ChangelogStatus status) {
        Changelog changelog = changelogRepository.findById(id).get();
        changelog.setChangelogStatus(status);

        changelogRepository.save(changelog);

        return changelog;
    }
}
