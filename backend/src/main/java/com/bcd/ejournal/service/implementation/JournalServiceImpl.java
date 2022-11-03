package com.bcd.ejournal.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bcd.ejournal.domain.dto.request.JournalCreateRequest;
import com.bcd.ejournal.domain.dto.request.JournalSearchRequest;
import com.bcd.ejournal.domain.dto.request.PaperSearchRequest;
import com.bcd.ejournal.domain.dto.response.IssueResponse;
import com.bcd.ejournal.domain.dto.response.JournalResponse;
import com.bcd.ejournal.domain.dto.response.PagingResponse;
import com.bcd.ejournal.domain.dto.response.PaperResponse;
import com.bcd.ejournal.domain.entity.Account;
import com.bcd.ejournal.domain.entity.Issue;
import com.bcd.ejournal.domain.entity.Journal;
import com.bcd.ejournal.domain.entity.Paper;
import com.bcd.ejournal.domain.enums.AccountRole;
import com.bcd.ejournal.domain.enums.JournalStatus;
import com.bcd.ejournal.domain.exception.ForbiddenException;
import com.bcd.ejournal.domain.exception.MethodNotAllowedException;
import com.bcd.ejournal.repository.AccountRepository;
import com.bcd.ejournal.repository.FieldRepository;
import com.bcd.ejournal.repository.IssueRepository;
import com.bcd.ejournal.repository.JournalRepository;
import com.bcd.ejournal.repository.PaperRepository;
import com.bcd.ejournal.service.JournalService;
import com.bcd.ejournal.utils.DTOMapper;

@Service
public class JournalServiceImpl implements JournalService {
    private final JournalRepository journalRepository;
    private final IssueRepository issueRepository;
    private final AccountRepository accountRepository;
    private final PaperRepository paperRepository;
    private final ModelMapper modelMapper;
    private final DTOMapper dtoMapper;
    private final FieldRepository fieldRepository;

    @Autowired
    public JournalServiceImpl(JournalRepository journalRepository, IssueRepository issueRepository,
            AccountRepository accountRepository, PaperRepository paperRepository, ModelMapper modelMapper,
            DTOMapper dtoMapper, FieldRepository fieldRepository) {
        this.journalRepository = journalRepository;
        this.issueRepository = issueRepository;
        this.accountRepository = accountRepository;
        this.paperRepository = paperRepository;
        this.modelMapper = modelMapper;
        this.dtoMapper = dtoMapper;
        this.fieldRepository = fieldRepository;
    }

    @Override
    public JournalResponse createJournal(JournalCreateRequest request) {
        // trim white space
        request.setName(request.getName().trim());
        request.setIntroduction(request.getIntroduction().trim());
        request.setOrganization(request.getOrganization().trim());
        request.setIssn(request.getIssn().trim());

        Account account = accountRepository.findByEmail(request.getManagerEmail())
                .orElseThrow(() -> new NullPointerException("Account not found. Email: " + request.getManagerEmail()));

        if (account.getJournal() != null) {
            throw new MethodNotAllowedException("This account is already a manager");
        }

        Journal journal = new Journal();
        journal.setName(request.getName());
        journal.setIntroduction(request.getIntroduction());
        journal.setOrganization(request.getOrganization());
        journal.setNumberOfRound(request.getNumberOfRound());
        journal.setNumberOfReviewer(request.getNumberOfReviewer());
        journal.setPrice(1000);
        journal.setIssn(request.getIssn());
        journal.setJournalId(0);
        journal.setStatus(JournalStatus.OPEN);
        journal.setSlug(request.getName().toLowerCase());
        journal.setFields(fieldRepository.findAllByFieldIdIn(request.getFieldId()));

        journal.setManager(account);
        account.setJournal(journal);
        account.setRole(AccountRole.MANAGER);

        journal = journalRepository.save(journal);
        accountRepository.save(account);

        return dtoMapper.toJournalResponse(journal);
    }

    @Override
    public JournalResponse getJournal(Integer journalId) {
        // TODO: return journal detail: latest issue, lastest publish
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new NullPointerException("Journal not found: " + journalId));
        return dtoMapper.toJournalResponse(journal);
    }

    @Override
    public JournalResponse getJournal(String slug) {
        Journal journal = journalRepository.findBySlug(slug)
                .orElseThrow(() -> new NullPointerException("Journal not found. Slug: " + slug));
        return dtoMapper.toJournalResponse(journal);
    }

    @Override
    public JournalResponse getJournalManager(Integer accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NullPointerException("Account not found. Id: " + accountId));
        if (account.getRole() != AccountRole.MANAGER) {
            throw new ForbiddenException("Unauthorized action");
        }
        return dtoMapper.toJournalResponse(account.getJournal());
    }

    @Override
    public PagingResponse search(JournalSearchRequest request) {
        int pageNum = request.getPage() != null ? request.getPage() - 1 : 0;
        Pageable page = PageRequest.of(pageNum, 10);
        if (request.getFieldIds() == null) {
            request.setFieldIds(new ArrayList<>());
        }

        Page<Journal> journals = journalRepository.searchRequest(request, Long.valueOf(request.getFieldIds().size()),
                page);

        PagingResponse response = new PagingResponse();
        response.setNumOfPage(journals.getTotalPages());
        response.setTotalFound(journals.getTotalElements());

        response.setResult(journals.stream()
                .map(dtoMapper::toJournalResponse)
                .collect(Collectors.toList()));
        return response;
    }

    @Override
    public List<IssueResponse> listAllIssues(Integer journalId) {
        Iterable<Issue> issues = issueRepository.findAllByJournalId(journalId);
        return StreamSupport.stream(issues.spliterator(), false).map(dtoMapper::toIssueResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueResponse> listAllIssues(String slug) {
        Iterable<Issue> issues = issueRepository.findAllByJournalSlug(slug);
        return StreamSupport.stream(issues.spliterator(), false).map(dtoMapper::toIssueResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueResponse> listAllIssuesFromManager(Integer accountId) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new NullPointerException("Manager not found. Id: " + accountId));
        List<Issue> issues = acc.getJournal().getIssues();
        return issues.stream()
                .map(dtoMapper::toIssueResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JournalResponse updateJournal(Integer journalId, JournalCreateRequest request) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new NullPointerException("Journal not found: " + journalId));

        journal.setName(request.getName());
        journal.setIntroduction(request.getIntroduction());
        journal.setOrganization(request.getOrganization());
        journal.setNumberOfRound(request.getNumberOfRound());
        journal.setNumberOfReviewer(request.getNumberOfReviewer());
        journal.setIssn(request.getIssn());
        journal.setSlug(request.getName().toLowerCase());

        journal.setFields(fieldRepository.findAllByFieldIdIn(request.getFieldId()));

        Account manager = journal.getManager();

        if (!manager.getEmail().equals(request.getManagerEmail())) {
            Account account = accountRepository.findByEmail(request.getManagerEmail())
                    .orElseThrow(
                            () -> new NullPointerException("Account not found. Email: " + request.getManagerEmail()));

            if (account.getJournal() != null) {
                throw new MethodNotAllowedException("This account is already a manager");
            }

            journal.setManager(account);
            account.setJournal(journal);

            account.setRole(AccountRole.MANAGER);
            manager.setRole(AccountRole.MEMBER);
            manager.setJournal(null);

            journal = journalRepository.save(journal);
            accountRepository.save(account);
            accountRepository.save(manager);
        } else {
            journal = journalRepository.save(journal);
        }

        return dtoMapper.toJournalResponse(journal);
    }

    @Override
    public void archiveJournal(Integer journalId) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new NullPointerException("Journal not found: " + journalId));
        journal.setStatus(JournalStatus.ARCHIVED);
        journalRepository.save(journal);
    }

    @Override
    public void openJournal(Integer journalId) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new NullPointerException("Journal not found: " + journalId));
        journal.setStatus(JournalStatus.OPEN);
        journalRepository.save(journal);
    }

    @Override
    public List<PaperResponse> getAllPaper(Integer accountId) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new NullPointerException("Account not found. Id: " + accountId));

        if (acc.getRole() != AccountRole.MANAGER) {
            throw new ForbiddenException("Unauthorized action");
        }

        return acc.getJournal().getPapers().stream()
                .map(dtoMapper::toPaperResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PagingResponse getAllPaper(Integer accountId, PaperSearchRequest request) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new NullPointerException("Account not found. Id: " + accountId));

        int pageNum = request.getPage() != null ? request.getPage() - 1 : 0;
        Pageable page = PageRequest.of(pageNum, 10, Sort.by("submitTime").descending());

        request.setJournalId(acc.getJournal().getJournalId());

        Page<Paper> papers = paperRepository.searchAndFilter(request, page);
        PagingResponse response = new PagingResponse();

        response.setResult(papers.stream().map(dtoMapper::toPaperResponse)
                .collect(Collectors.toList()));
        response.setNumOfPage(papers.getTotalPages());
        response.setTotalFound(papers.getTotalElements());

        return response;
    }
}
