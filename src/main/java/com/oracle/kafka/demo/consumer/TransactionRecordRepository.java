package com.oracle.kafka.demo.consumer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.kafka.demo.config.TransactionRecord;


@Repository
public interface TransactionRecordRepository 
  extends JpaRepository<TransactionRecord, Long> {
}