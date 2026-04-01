package com.hkmu.online_course.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base implementation class providing JPAQueryFactory to all repository implementations.
 * Concrete implementations should extend this class to gain access to QueryDSL querying capabilities.
 */
public abstract class AbstractRepositoryImpl {

    @Autowired
    protected JPAQueryFactory queryFactory;

    protected AbstractRepositoryImpl() {
    }
}
