import { useState } from "react";
import { Button, Form, InputGroup, Stack } from "react-bootstrap";
import { FcSearch } from "react-icons/fc";
import { useNavigate } from "react-router-dom";
import Pagination from "src/components/Pagination";
import BuildingsTable from "./components/BuildingsTable";

const buildingsPages = [
  [
    { id: 1, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 2, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 3, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 4, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 5, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 6, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 7, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 8, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 9, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 10, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
  ],
  [
    { id: 11, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 12, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 13, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 14, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 15, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 16, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 17, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 18, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 19, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 20, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
  ],
  [
    { id: 21, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 22, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 23, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 24, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 25, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
    { id: 26, name: "꼬마 빌딩(30억)", lastModifiedDate: new Date().toJSON() },
  ],
];

function Buildings() {
  const navigate = useNavigate();
  const [page, setPage] = useState(1);

  return (
    <Stack gap={3}>
      <InputGroup>
        <Form.Control placeholder="검색어를 입력해 주세요." aria-label="건물 검색" aria-describedby="건물 검색" />
        <Button variant="outline-dark">
          <FcSearch size={30} />
        </Button>
      </InputGroup>
      <Button onClick={() => navigate("/buildings/form")}>등록</Button>
      <BuildingsTable buildings={buildingsPages[page - 1]} />
      <div className="mx-auto">
        <Pagination page={page} totalPages={buildingsPages.length} onPaginate={setPage} />
      </div>
    </Stack>
  );
}

export default Buildings;
