package com.app.curioq.qaservice.repository;

import com.app.curioq.qaservice.entity.Answer;
import com.app.curioq.qaservice.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
