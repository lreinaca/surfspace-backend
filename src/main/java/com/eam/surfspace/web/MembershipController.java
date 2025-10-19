package com.eam.surfspace.web;
import com.eam.surfspace.domain.dto.MembershipCreateDTO;
import com.eam.surfspace.domain.dto.MembershipDTO;
import com.eam.surfspace.domain.dto.MembershipUpdateDTO;
import com.eam.surfspace.domain.service.MembershipService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/memberships")
@Tag(name = "Memberships", description = "API for managing memberships")
public class MembershipController {
    private final MembershipService membershipService;

    @Autowired
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    @PostMapping
    public ResponseEntity<MembershipDTO> createMembership(@RequestBody MembershipCreateDTO membership) {
        try {
            MembershipDTO saved = membershipService.save(membership);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<MembershipDTO>> getAllMemberships() {
        try {
            List<MembershipDTO> memberships = membershipService.findAll();
            return ResponseEntity.ok(memberships);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipDTO> getMembershipById(@PathVariable Integer id) {
        try {
            Optional<MembershipDTO> membership = membershipService.findById(id);
            return membership.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembershipDTO> updateMembership(@PathVariable Integer id, @RequestBody MembershipUpdateDTO updatedMembership) {
        try {
            Optional<MembershipDTO> updated = membershipService.update(id, updatedMembership);
            return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembership(@PathVariable Integer id) {
        try {
            boolean deleted = membershipService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
