package com.mojave.controller;

import com.mojave.dto.UserDTO;
import com.mojave.payload.request.InviteMembersRequest;
import com.mojave.payload.response.ApiResponse;
import com.mojave.service.TeamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TeamController {

    TeamService teamService;

    @GetMapping("/{projectId}/team")
    public ResponseEntity<List<UserDTO>> getProjectMembers(@PathVariable Long projectId) {
        return ResponseEntity.ok(teamService.getProjectMembers(projectId));
    }

    @GetMapping("/{projectId}/team/verify-email-presents")
    public ResponseEntity<Boolean> checkIfMemberWithEmailExists(@PathVariable Long projectId, @RequestParam String email) {
        return ResponseEntity.ok(teamService.checkIfMemberWithEmailExists(projectId, email));
    }

    @PostMapping("/{projectId}/team")
    public ResponseEntity<ApiResponse> inviteMembers(@PathVariable Long projectId,
                                                     @RequestBody InviteMembersRequest request) {
        teamService.inviteMembers(projectId, request);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully invited"));
    }

    @PutMapping("/{projectId}/team/{memberId}")
    public ResponseEntity<ApiResponse> updateMemberRole(@PathVariable Long projectId,
                                                        @PathVariable Long memberId,
                                                        @RequestParam String role) {
        teamService.updateMemberRole(projectId, memberId, role);
        return ResponseEntity.ok(new ApiResponse(true, "Role was updated successfully"));
    }

    @DeleteMapping("/{projectId}/team/{memberId}")
    public ResponseEntity<ApiResponse> removeMemberFromProject(@PathVariable Long projectId,
                                                               @PathVariable Long memberId) {
        teamService.removeMemberFromProject(projectId, memberId);
        return ResponseEntity.ok(new ApiResponse(true, "Successfully removed"));
    }
}
