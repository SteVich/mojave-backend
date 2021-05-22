package com.mojave.service;

import com.mojave.dictionary.Role;
import com.mojave.dto.UserDTO;
import com.mojave.exception.ResourceNotFoundException;
import com.mojave.mapper.UserMapper;
import com.mojave.model.Invite;
import com.mojave.model.Project;
import com.mojave.model.User;
import com.mojave.model.UserProjectRole;
import com.mojave.payload.request.InviteMembersRequest;
import com.mojave.repository.InviteRepository;
import com.mojave.repository.ProjectRepository;
import com.mojave.service.mailsender.MailSender;
import com.mojave.util.EmailConstants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TeamService {

    ProjectRepository projectRepository;
    InviteRepository inviteRepository;
    MailSender mailSender;
    UserMapper userMapper;
    Environment environment;

    @Transactional(readOnly = true)
    public List<UserDTO> getProjectMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));

        return project.getUsers().stream()
                .map(user -> {
                    UserDTO userDTO = userMapper.toUserDTO(user);

                    user.getProjectRoles().stream()
                            .filter(role -> projectId.equals(role.getId().getProjectId()))
                            .findFirst()
                            .ifPresent(role -> userDTO.setRole(role.getRole().getValue()));

                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMemberRole(Long projectId, Long memberId, String role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));

        project.getUsers()
                .forEach(user -> {
                    user.getProjectRoles().stream()
                            .filter(userProjectRole -> userProjectRole.getId().getUserId().equals(memberId)
                                    && userProjectRole.getId().getProjectId().equals(projectId))
                            .forEach(userProjectRole -> userProjectRole.setRole(Role.valueOfName(role)));
                });

        projectRepository.save(project);
    }

    @Transactional
    public void removeMemberFromProject(Long projectId, Long memberId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));

        User memberToDelete = project.getUsers().stream()
                .filter(user -> user.getId().equals(memberId))
                .findFirst()
                .orElse(new User());

        memberToDelete.getProjects().remove(project);
        project.getUsers().remove(memberToDelete);

        projectRepository.save(project);
    }

    @Transactional
    public void inviteMembers(Long projectId, InviteMembersRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));

        String url = "http://localhost:8080/";
        String profile = Arrays.stream(environment.getActiveProfiles()).findFirst().orElse("local");
        if (profile.equals("prod")) {
            url = "prod";
        }

        String acceptUrl = url;

        request.getEmails().forEach(recipient -> {
            createInvite(project, recipient);

            sendEmail(recipient, request, project, acceptUrl);
        });
    }

    private void createInvite(Project project, String email) {
        Optional<Invite> possibleExistingInvite = inviteRepository.findByEmail(email);
        if (possibleExistingInvite.isPresent()) {
            Invite invite = possibleExistingInvite.get();
            invite.setDueDate(LocalDateTime.now().plusDays(3));
        } else {
            Invite invite = new Invite();
            invite.setEmail(email);
            invite.setProject(project);
            invite.setDueDate(LocalDateTime.now().plusDays(3));

            inviteRepository.save(invite);
        }
    }

    private void sendEmail(String recipient, InviteMembersRequest request, Project project, String url) {
        Map<String, Object> mailMap = new HashMap<>();
        mailMap.put(EmailConstants.MAIL_CONTENT, EmailConstants.MAIL_LAYOUT);
        mailMap.put("projectTitle", project.getName());
        mailMap.put("message", request.getMessage());
        mailMap.put("acceptUrl", url + "api/google/login-with-google");

        String subject = String.format("Invitation to the %s project", project.getName());

        mailSender.sendMessageWithTemplate(recipient, subject, mailMap);
    }

    @Transactional(readOnly = true)
    public Boolean checkIfMemberWithEmailExists(Long projectId, String email) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "project id", projectId));

        return project.getUsers().stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }
}
