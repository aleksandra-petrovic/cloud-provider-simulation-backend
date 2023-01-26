package com.example.demo.repositories;

import com.example.demo.model.ErrorMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorMsgRepository extends JpaRepository<ErrorMsg,Long> {
    public ErrorMsg findErrorMsgByErrorId(Long id);
    public List<ErrorMsg> findErrorMsgsByMachineId(Long id);
}
