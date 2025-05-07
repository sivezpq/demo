package com.canal;

import lombok.Data;

@Data
public class Transaction {
    private Long id;          // 主键ID
    private String transactionId; // 交易ID
    private Double amount;      // 交易金额
    private String status;      // 交易状态
}
