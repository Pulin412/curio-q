package com.app.curioq.qaservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_seq")
    @SequenceGenerator(name = "answer_seq", sequenceName = "answer_seq", allocationSize = 1)
    private Long id;
    private String answerDescription;
    @Column(name = "useremail")
    private String userEmail;
    @Column(name = "userid")
    private long userId;

    private Set<Long> likedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
}
