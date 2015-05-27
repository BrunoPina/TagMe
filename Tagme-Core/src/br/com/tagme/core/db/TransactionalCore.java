package br.com.tagme.core.db;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "TransactionManagerCore", propagation = Propagation.REQUIRES_NEW)
public @interface TransactionalCore {

}
