package com.bcd.ejournal.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcd.ejournal.configuration.jwt.payload.AccountJWTPayload;
import com.bcd.ejournal.domain.dto.request.InvitationSearchFilterRequest;
import com.bcd.ejournal.domain.dto.request.InvitationUpdateStatusRequest;
import com.bcd.ejournal.domain.dto.request.ReviewerInvitationRequest;
import com.bcd.ejournal.domain.dto.request.ReviewerUpdateFieldRequest;
import com.bcd.ejournal.domain.dto.response.InvitationPaperResponse;
import com.bcd.ejournal.domain.dto.response.InvitationReviewerResponse;
import com.bcd.ejournal.domain.dto.response.PagingResponse;
import com.bcd.ejournal.domain.dto.response.ReviewReportDetailResponse;
import com.bcd.ejournal.domain.dto.response.ReviewerResponse;
import com.bcd.ejournal.service.InvitationService;
import com.bcd.ejournal.service.ReviewReportService;
import com.bcd.ejournal.service.ReviewerService;

@RestController
@RequestMapping("/reviewer")
public class ReviewerApi {
    private final InvitationService invitationService;
    private final ReviewReportService reviewReportService;
    private final ReviewerService reviewerService;

    @Autowired
    public ReviewerApi(InvitationService invitationService, ReviewReportService reviewReportService,
            ReviewerService reviewerService) {
        this.invitationService = invitationService;
        this.reviewReportService = reviewReportService;
        this.reviewerService = reviewerService;
    }

    @GetMapping
    public ResponseEntity<ReviewerResponse> getReviewerSetting(@AuthenticationPrincipal AccountJWTPayload payload) {
        ReviewerResponse response = reviewerService.getReviewerSetting(payload.getAccountId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/invitable/{invitable}")
    public ResponseEntity<Void> updateInvitable(@AuthenticationPrincipal AccountJWTPayload payload, @PathVariable Boolean invitable) {
        reviewerService.updateInvitable(payload.getAccountId(), invitable);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/field")
    public ResponseEntity<Void> updateFields(@AuthenticationPrincipal AccountJWTPayload payload, @RequestBody ReviewerUpdateFieldRequest request) {
        reviewerService.updateField(payload.getAccountId(), request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/invitation")
    public ResponseEntity<InvitationPaperResponse> sendInvitation(@AuthenticationPrincipal AccountJWTPayload payload,
            @PathVariable(name = "id") Integer reviewerId, @Valid @RequestBody ReviewerInvitationRequest request) {
        InvitationPaperResponse response = invitationService.sendInvitation(payload.getAccountId(), reviewerId,
                request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/invitation")
    public ResponseEntity<List<InvitationReviewerResponse>> listAllInvitation(
            @AuthenticationPrincipal AccountJWTPayload payload) {
        Integer reviewerId = payload.getAccountId();
        List<InvitationReviewerResponse> responses = invitationService.listInvitationFromReviewer(reviewerId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/invitation/{id}")
    public ResponseEntity<InvitationReviewerResponse> getInvitation(@AuthenticationPrincipal AccountJWTPayload payload,
            @PathVariable Integer id) {
        InvitationReviewerResponse response = invitationService.getInvitation(payload.getAccountId(), id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/invitation/search")
    public ResponseEntity<PagingResponse> searchFilter(@AuthenticationPrincipal AccountJWTPayload payload,
            @RequestBody InvitationSearchFilterRequest req) {
        req.setReviewerId(payload.getAccountId());
        PagingResponse response = invitationService.searchFilterInvitation(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/invitation/{id}/status")
    public ResponseEntity<Void> acceptOrRejectInvitation(@AuthenticationPrincipal AccountJWTPayload payload,
            @PathVariable(name = "id") Integer invitationId,
            @Valid @RequestBody InvitationUpdateStatusRequest request) {
        Integer reviewerId = payload.getAccountId();
        invitationService.updateStatus(reviewerId, invitationId, request.getStatus());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/reviewreport")
    public ResponseEntity<List<ReviewReportDetailResponse>> getReviewDetail(
            @AuthenticationPrincipal AccountJWTPayload payload) {
        Integer reviewerId = payload.getAccountId();
        List<ReviewReportDetailResponse> responses = reviewReportService.getAllReviewReport(reviewerId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/reviewreport/{id}")
    public ResponseEntity<ReviewReportDetailResponse> getReviewDetail(
            @AuthenticationPrincipal AccountJWTPayload payload, @PathVariable Integer id) {
        Integer reviewerId = payload.getAccountId();
        ReviewReportDetailResponse responses = reviewReportService.getReviewReport(reviewerId, id);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/paper/{id}/search")
    public ResponseEntity<List<ReviewerResponse>> getReviewerAvailableForPaper(
            @AuthenticationPrincipal AccountJWTPayload payload, @PathVariable(name = "id") Integer paperId,
            @RequestParam String name) {
        // TODO: might need paging
        Integer accountId = payload.getAccountId();
        List<ReviewerResponse> responses = reviewerService.searchReviewerAvailable(paperId, name);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
