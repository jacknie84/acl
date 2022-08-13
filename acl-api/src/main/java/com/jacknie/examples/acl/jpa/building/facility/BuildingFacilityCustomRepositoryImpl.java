package com.jacknie.examples.acl.jpa.building.facility;

import com.jacknie.examples.acl.web.building.facility.BuildingFacilitiesFilterDto;
import com.jacknie.examples.acl.web.building.facility.BuildingFacilityDto;
import com.jacknie.examples.acl.web.building.facility.QBuildingFacilityDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static com.jacknie.examples.acl.jpa.QuerydslPredicateUtils.*;
import static com.jacknie.examples.acl.jpa.building.facility.QBuildingFacility.buildingFacility;

@SuppressWarnings("ALL")
public class BuildingFacilityCustomRepositoryImpl extends QuerydslRepositorySupport implements BuildingFacilityCustomRepository {

    private final QBuildingFacilityDto buildingFacilityDto = new QBuildingFacilityDto(
        buildingFacility.id,
        buildingFacility.name,
        buildingFacility.parent.id,
        buildingFacility.building.id,
        buildingFacility.lastModifiedDate
    );

    public BuildingFacilityCustomRepositoryImpl() {
        super(BuildingFacility.class);
    }

    @Override
    public Page<BuildingFacilityDto> findAllDto(long buildingId, BuildingFacilitiesFilterDto dto, Pageable pageable) {
        JPQLQuery<BuildingFacility> query = from(buildingFacility).where(toPredicates(buildingId, dto));
        long total = query.fetchCount();
        if (total > 0) {
            List<BuildingFacilityDto> content = getQuerydsl().applyPagination(pageable, query)
                .select(buildingFacilityDto)
                .fetch();
            return new PageImpl<>(content, pageable, total);
        } else {
            return Page.empty(pageable);
        }
    }

    @Override
    public Optional<BuildingFacilityDto> findDtoByBuildingIdAndId(long buildingId, long id) {
        BuildingFacilityDto dto = from(buildingFacility).select(buildingFacilityDto)
            .where(
                buildingFacility.building.id.eq(buildingId),
                buildingFacility.id.eq(id)
            )
            .fetchOne();
        return Optional.ofNullable(dto);
    }

    private Predicate[] toPredicates(long buildingId, BuildingFacilitiesFilterDto dto) {
        return new Predicate[] {
            buildingFacility.building.id.eq(buildingId),
            ifEmptyNone(buildingFacility.name::contains, dto.getSearch()),
            ifNullNone(buildingFacility.parent.id::eq, dto.getParentId()),
            ifFalseNone(buildingFacility.parent.id::isNull, dto.isParentIdIsNull())
        };
    }
}
