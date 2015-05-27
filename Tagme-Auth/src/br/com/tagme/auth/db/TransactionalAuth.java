package br.com.tagme.auth.db;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "TransactionManagerAuth", propagation = Propagation.REQUIRES_NEW)
public @interface TransactionalAuth {

}
