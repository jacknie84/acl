package com.jacknie.examples.acl.jpa.member;

import com.jacknie.examples.acl.web.member.MemberAccountsFilterDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.jacknie.examples.acl.jpa.QuerydslPredicateUtils.ifEmptyNone;
import static com.jacknie.examples.acl.jpa.member.QMemberAccount.memberAccount;
import static com.jacknie.examples.acl.utils.EnumUtils.parseEnum;

@SuppressWarnings("ALL")
public class MemberAccountCustomRepositoryImpl extends QuerydslRepositorySupport implements MemberAccountCustomRepository {

    public MemberAccountCustomRepositoryImpl() {
        super(MemberAccount.class);
    }

    @Override
    public Page<MemberAccount> findAll(MemberAccountsFilterDto dto, Pageable pageable) {
        JPQLQuery<MemberAccount> query = from(memberAccount).where(toPredicates(dto));
        long total = query.fetchCount();
        if (total > 0) {
            JPQLQuery<MemberAccount> joined = query.leftJoin(memberAccount.roles).fetchJoin();
            List<MemberAccount> content = getQuerydsl().applyPagination(pageable, joined).fetch();
            return new PageImpl<>(content, pageable, total);
        } else {
            return Page.empty(pageable);
        }
    }

    private Predicate[] toPredicates(MemberAccountsFilterDto dto) {
        return new Predicate[] {
            ifEmptyNone(this::toSearchBooleanExpression, dto.getSearch()),
        };
    }

    private BooleanExpression toSearchBooleanExpression(String search) {
        BooleanExpression expression = memberAccount.email.contains(search);
        return parseEnum(search, MemberRole.class)
            .map(role -> expression.or(memberAccount.roles.any().eq(role)))
            .orElse(expression);
    }
}
