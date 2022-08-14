import { useQuery } from "@tanstack/react-query";
import { useCallback, useContext, useState } from "react";
import { Button, Form, InputGroup, Stack } from "react-bootstrap";
import { FcSearch } from "react-icons/fc";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import Pagination from "src/components/Pagination";
import { useGetBuildingFacilitiesApi } from "src/hooks/api/building-facility";
import BuildingFacilitiesTable from "./components/BuildingFacilitiesTable";
import BuildingFacilityContext from "./contexts/BuildingFacilityContext";

function BuildingFacilities() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { buildingId } = useParams();
  const [page, setPage] = useState(1);
  const [keyword, setKeyword] = useState("");
  const [search, setSearch] = useState("");
  const onClickSearch = useCallback(() => {
    if (keyword.length > 1) {
      setSearch(keyword);
    }
  }, [keyword]);
  const { facilityPath } = useContext(BuildingFacilityContext);
  const current = facilityPath[facilityPath.length - 1];
  const getFacilitiesAsync = useGetBuildingFacilitiesApi(buildingId);
  const { data } = useQuery(["getFacilities", buildingId, search, current], ({ signal }) =>
    getFacilitiesAsync(
      {
        filter: { search, parentId: current?.id, nullParentId: !current },
        pageable: { pageNumber: page - 1, pageSize: 10, sort: { orders: [{ property: "lastModifiedDate", direction: "desc" }] } },
      },
      { signal },
    ),
  );

  return (
    <Stack gap={3} className="mt-3">
      <InputGroup>
        <Form.Control
          value={keyword}
          onChange={(e) => setKeyword(e.currentTarget.value)}
          onKeyUp={(e) => e.key === "Enter" && onClickSearch()}
          placeholder="검색어를 입력해 주세요."
          aria-label="시설 검색"
          aria-describedby="시설 검색"
        />
        <Button variant="outline-dark">
          <FcSearch size={30} />
        </Button>
      </InputGroup>
      <Button onClick={() => navigate(`../../form?${searchParams}`)}>등록</Button>
      <BuildingFacilitiesTable facilities={data?.content ?? []} />
      <div className="mx-auto">
        <Pagination page={page} totalPages={data?.totalPages} onPaginate={setPage} />
      </div>
    </Stack>
  );
}

export default BuildingFacilities;
