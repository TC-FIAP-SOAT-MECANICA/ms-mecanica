package br.com.oficina.infrastructure.dataproviders.database.implementations.shared;

import br.com.oficina.application.gateways.shared.UnitOfWork;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

@Component
class UnitOfWorkImpl implements UnitOfWork {

    private final TransactionTemplate transactionTemplate;

    UnitOfWorkImpl(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public <T> T execute(Supplier<T> work) {
        return transactionTemplate.execute(status -> work.get());
    }
}
