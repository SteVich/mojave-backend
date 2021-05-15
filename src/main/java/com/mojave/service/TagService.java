package com.mojave.service;

import com.mojave.mapper.TagMapper;
import com.mojave.payload.response.TagResponse;
import com.mojave.repository.TagRepository;
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
    TagMapper mapper;

    @Transactional(readOnly = true)
    public List<TagResponse> getAllTagsForProject(Long projectId) {
        return mapper.toResponseList(tagRepository.findAllByProjectId(projectId));
    }
}
