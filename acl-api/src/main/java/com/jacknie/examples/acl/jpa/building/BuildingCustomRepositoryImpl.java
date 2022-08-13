package com.jacknie.examples.acl.jpa.building;

import com.jacknie.examples.acl.web.building.BuildingsFilterDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.jacknie.examples.acl.jpa.QuerydslPredicateUtils.ifEmptyNone;
import static com.jacknie.examples.acl.jpa.building.QBuilding.building;

@SuppressWarnings("ALL")
public class BuildingCustomRepositoryImpl extends QuerydslRepositorySupport implements BuildingCustomRepository {

    public BuildingCustomRepositoryImpl() {
        super(Building.class);
    }

    @Override
    public Page<Building> findAll(BuildingsFilterDto dto, Pageable pageable) {
        JPQLQuery<Building> query = from(building).where(toPredicates(dto));
        long total = query.fetchCount();
        if (total > 0) {
            List<Building> content = getQuerydsl().applyPagination(pageable, query).fetch();
            return new PageImpl<>(content, pageable, total);
        } else {
            return Page.empty(pageable);
        }
    }

    private Predicate[] toPredicates(BuildingsFilterDto dto) {
        return new Predicate[] {
            ifEmptyNone(building.name::contains, dto.getSearch())
        };
    }
}
