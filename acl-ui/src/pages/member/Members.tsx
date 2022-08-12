import { useQuery } from "@tanstack/react-query";
import { useCallback, useState } from "react";
import { Button, Form, InputGroup, Stack } from "react-bootstrap";
import { FcSearch } from "react-icons/fc";
import { useNavigate } from "react-router-dom";
import Pagination from "src/components/Pagination";
import { useGetMemberAccountsApi } from "src/hooks/api/member-account";
import MembersTable from "./components/MembersTable";

function Members() {
  const navigate = useNavigate();
  const [page, setPage] = useState(1);
  const [keyword, setKeyword] = useState("");
  const [search, setSearch] = useState("");
  const getMemberAccountsAsync = useGetMemberAccountsApi();
  const { data } = useQuery(["getMemberAccounts", page, search], ({ signal }) =>
    getMemberAccountsAsync(
      {
        filter: { search },
        pageable: { pageNumber: page - 1, pageSize: 10, sort: { orders: [{ property: "lastModifiedDate", direction: "desc" }] } },
      },
      { signal },
    ),
  );
  const onClickSearch = useCallback(() => {
    if (keyword.length > 1) {
      if ("관리 사용자".startsWith(keyword)) {
        setSearch("ROLE_ADMIN");
      } else if ("일반 사용자".startsWith(keyword)) {
        setSearch("ROLE_USER");
      } else {
        setSearch(keyword);
      }
    }
  }, [keyword]);

  return (
    <Stack gap={3}>
      <InputGroup>
        <Form.Control
          value={keyword}
          onChange={(e) => setKeyword(e.currentTarget.value)}
          onKeyUp={(e) => e.key === "Enter" && onClickSearch()}
          placeholder="검색어를 입력해 주세요."
          aria-label="회원 검색"
          aria-describedby="회원 검색"
        />
        <Button onClick={onClickSearch} variant="outline-dark">
          <FcSearch size={30} />
        </Button>
      </InputGroup>
      <Button onClick={() => navigate("form")}>등록</Button>
      <MembersTable members={data?.content ?? []} />
      <div className="mx-auto">
        <Pagination page={page} totalPages={data?.totalPages} onPaginate={setPage} />
      </div>
    </Stack>
  );
}

export default Members;
