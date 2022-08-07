import { useContext, useMemo, useState } from "react";
import { Button, Form, InputGroup, Stack } from "react-bootstrap";
import { FcSearch } from "react-icons/fc";
import { useNavigate, useSearchParams } from "react-router-dom";
import Pagination from "src/components/Pagination";
import BuildingContext from "../contexts/BuildingContext";
import BuildingFacilitiesTable from "./components/BuildingFacilitiesTable";
import facilitiesPagesJson from "./facilitiesPages.json";
import { BuildingFacilitySummary } from "./types";

const facilitiesPagesUnknown = facilitiesPagesJson as Record<string, unknown>;
const facilitiesPages = facilitiesPagesUnknown as Record<string, BuildingFacilitySummary[][]>;

function BuildingFacilities() {
  const navigate = useNavigate();
  const { facilityPath, facilities } = useContext(BuildingContext);
  const [searchParams] = useSearchParams();
  const [page, setPage] = useState(1);
  const facility = useMemo(() => {
    if (facilityPath.length > 0) {
      const id = facilityPath[facilityPath.length - 1];
      return facilities[id];
    }
  }, [facilityPath, facilities]);

  return (
    <Stack gap={3} className="mt-3">
      <InputGroup>
        <Form.Control placeholder="검색어를 입력해 주세요." aria-label="시설 검색" aria-describedby="시설 검색" />
        <Button variant="outline-dark">
          <FcSearch size={30} />
        </Button>
      </InputGroup>
      <Button onClick={() => navigate(`../../form?${searchParams}`)}>등록</Button>
      <BuildingFacilitiesTable facilities={facilitiesPages[`${facility?.id ?? "root"}`]?.[page - 1] ?? []} />
      <div className="mx-auto">
        <Pagination page={page} totalPages={facilitiesPages[`${facility?.id ?? "root"}`]?.length ?? 0} onPaginate={setPage} />
      </div>
    </Stack>
  );
}

export default BuildingFacilities;
