import { useQuery } from "@tanstack/react-query";
import { useCallback, useState } from "react";
import { Button, Form, InputGroup, Stack } from "react-bootstrap";
import { FcSearch } from "react-icons/fc";
import { useNavigate } from "react-router-dom";
import Pagination from "src/components/Pagination";
import { useGetBuildingsApi } from "src/hooks/api/building";
import BuildingsTable from "./components/BuildingsTable";

function Buildings() {
  const navigate = useNavigate();
  const [page, setPage] = useState(1);
  const [keyword, setKeyword] = useState("");
  const [search, setSearch] = useState("");
  const getBuildingsAsync = useGetBuildingsApi();
  const { data } = useQuery(["getBuildings", page, search], ({ signal }) =>
    getBuildingsAsync(
      {
        filter: { search },
        pageable: { pageNumber: page - 1, pageSize: 10, sort: { orders: [{ property: "lastModifiedDate", direction: "desc" }] } },
      },
      { signal },
    ),
  );
  const onClickSearch = useCallback(() => {
    if (keyword.length > 1) {
      setSearch(keyword);
    }
  }, [keyword]);

  return (
    <Stack gap={3}>
      <InputGroup>
        <Form.Control
          onChange={(e) => setKeyword(e.currentTarget.value)}
          onKeyUp={(e) => e.key === "Enter" && onClickSearch()}
          placeholder="검색어를 입력해 주세요."
          aria-label="건물 검색"
          aria-describedby="건물 검색"
        />
        <Button variant="outline-dark">
          <FcSearch size={30} />
        </Button>
      </InputGroup>
      <Button onClick={() => navigate("/buildings/form")}>등록</Button>
      <BuildingsTable buildings={data?.content ?? []} />
      <div className="mx-auto">
        <Pagination page={page} totalPages={data?.totalPages} onPaginate={setPage} />
      </div>
    </Stack>
  );
}

export default Buildings;
