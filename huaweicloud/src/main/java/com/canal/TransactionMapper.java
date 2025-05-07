package com.canal;

@Mapper
public interface TransactionMapper {

     /**
     * 插入一条新的交易记录
     *
     * @param transaction 交易对象
     */
    @Insert("INSERT INTO transaction(transaction_id, amount, status) VALUES(#{
        transaction.transactionId},
       #{
        transaction.amount},
       #{
        transaction.status})"
      )
    void insert(@Param("transaction") Transaction transaction);

/**
 * 更新一条交易记录
 *
 * @param transaction 交易对象
 */
    @Update("UPDATE transaction SET amount=#{
        transaction.amount},
       status=#{
        transaction.status}
       WHERE transaction_id=#{
        transaction.transactionId}"
      )
    void update(@Param("transaction") Transaction transaction);
}
