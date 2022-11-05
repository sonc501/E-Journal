package com.bcd.ejournal.service.implementation;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcd.ejournal.domain.dto.response.InvoiceResponse;
import com.bcd.ejournal.domain.entity.Account;
import com.bcd.ejournal.domain.entity.Invoice;
import com.bcd.ejournal.domain.entity.Journal;
import com.bcd.ejournal.repository.AccountRepository;
import com.bcd.ejournal.repository.InvoiceRepository;
import com.bcd.ejournal.repository.JournalRepository;
import com.bcd.ejournal.service.InvoiceService;
import com.bcd.ejournal.utils.DTOMapper;

@Service
public class InvoiceServiceImp implements InvoiceService{
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private JournalRepository journalRepository;
	
	@Autowired
	private DTOMapper dtoMapper;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public void createInvoice(double amount,String method, Integer accountId, Integer journalId) {
		Invoice invoice = new Invoice();
		Journal journal = journalRepository.findById(journalId)
				.orElseThrow(() -> new NullPointerException("Journal not found. Id: " + journalId));
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new NullPointerException("Account not found. Id: " + accountId));
		invoice.setAccount(account);
		invoice.setJournal(journal);
		invoice.setPaymentTime(new Timestamp(System.currentTimeMillis()));
		invoice.setEndDate(new Timestamp(System.currentTimeMillis()));
		invoice.setAmount(amount);
		invoice.setPaymentMethod(method);
		invoiceRepository.save(invoice);
	}
	@Override
	public List<InvoiceResponse> getInvoicebyId(Integer AccountId) {
		Iterable<Invoice> invoice = invoiceRepository.getInvoiceByAccountId(AccountId);
		return StreamSupport.stream(invoice.spliterator(), false).map(dtoMapper::toInvoiceResponse)
                .collect(Collectors.toList());
	}
	
	
}