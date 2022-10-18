package com.bcd.ejournal.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer issueId;
    private Integer volume;
    private Integer issue;
    private Date startDate;
    private Date endDate;
    private Integer numberOfPage;
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "journalId", nullable = false)
    private Journal journal;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.MERGE)
    private List<Publish> publishes;
}
