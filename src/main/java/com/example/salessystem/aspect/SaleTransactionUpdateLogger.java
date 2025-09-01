package com.example.salessystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@Slf4j
public class SaleTransactionUpdateLogger {

    public record SaleUpdatedEvent(Long saleId, int transactionCount, String seller) {}

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSaleUpdated(SaleUpdatedEvent event) {
        log.info("[AUDIT] Sale {} updated by seller='{}' with {} transactions", event.saleId(), event.seller(), event.transactionCount());
    }
}
