package org.example.jwt_cookie_study.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.jwt_cookie_study.domain.QUser;
import org.example.jwt_cookie_study.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//	•	update/delete → 트랜잭션 필수 (없으면 TransactionRequiredException)
//	•	select(조회) → 트랜잭션 없어도 동작 가능 (JPA 스펙상 허용)

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional
    public long updateRefreshToken(long userId, String refreshToken) {
        return jpaQueryFactory.update(QUser.user)
            .set(QUser.user.currentRefreshToken, refreshToken)
            .where(QUser.user.userId.eq(userId))
            .execute();
    }

    @Override
    public User findUserByEmail(String email) {
        return jpaQueryFactory.selectFrom(QUser.user)
            .where(QUser.user.email.eq(email))
            .fetchFirst();
    }

}
