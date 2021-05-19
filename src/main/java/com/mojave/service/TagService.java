package com.mojave.service;

import com.mojave.exception.ResourceNotFoundException;
import com.mojave.mapper.TagMapper;
import com.mojave.model.Project;
import com.mojave.model.Tag;
import com.mojave.payload.response.TagResponse;
import com.mojave.repository.ProjectRepository;
import com.mojave.repository.TagRepository;
import com.mojave.repository.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TagService {

    TagRepository tagRepository;
    ProjectRepository projectRepository;
    TaskRepository taskRepository;
    TagMapper mapper;

    @Transactional(readOnly = true)
    public List<TagResponse> getAllTagsForProject(Long projectId) {
        return mapper.toResponseList(tagRepository.findAllByProjectId(projectId));
    }

    @Transactional
    public Long createTag(Long projectId, String name, String color) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));

        Tag tag = new Tag();
        tag.setProject(project);
        tag.setName(name);
        tag.setColor(color);

        return tagRepository.save(tag).getId();
    }

    @Transactional
    public boolean deleteTag(Long projectId, Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "tag id", tagId));

        boolean result = taskRepository.findAllByTag(tag).isEmpty();
        if (result) {
            tagRepository.delete(tag);
        }
        return result;
    }

    @Transactional
    public void updateColor(Long tagId, String newColor) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "tag id", tagId));

        tag.setColor(newColor);

        tagRepository.save(tag);
    }
}
