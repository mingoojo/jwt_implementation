package org.example.jwt_cookie_study.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1343255643L;

    public static final QUser user = new QUser("user");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final StringPath currentRefreshToken = createString("currentRefreshToken");

    public final StringPath department = createString("department");

    public final StringPath email = createString("email");

    public final ComparablePath<Character> flag = createComparable("flag", Character.class);

    public final StringPath password = createString("password");

    public final StringPath position = createString("position");

    public final EnumPath<org.example.jwt_cookie_study.enums.UserRole> role = createEnum("role", org.example.jwt_cookie_study.enums.UserRole.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath userName = createString("userName");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

