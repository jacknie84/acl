import { useState } from "react";
import { Button, Form, InputGroup, Stack } from "react-bootstrap";
import { FcSearch } from "react-icons/fc";
import { useNavigate } from "react-router-dom";
import Pagination from "src/components/Pagination";
import MembersTable from "./components/MembersTable";

const membersPages = [
  [
    { id: 1, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 2, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 3, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 4, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 5, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 6, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 7, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 8, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 9, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 10, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
  ],
  [
    { id: 11, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 12, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 13, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 14, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 15, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 16, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 17, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 18, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 19, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 20, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
  ],
  [
    { id: 21, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 22, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 23, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 24, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 25, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
    { id: 26, email: "abc@email.com", lastModifiedDate: new Date().toJSON() },
  ],
];

function Members() {
  const navigate = useNavigate();
  const [page, setPage] = useState(1);

  return (
    <Stack gap={3}>
      <InputGroup>
        <Form.Control placeholder="검색어를 입력해 주세요." aria-label="회원 검색" aria-describedby="회원 검색" />
        <Button variant="outline-dark">
          <FcSearch size={30} />
        </Button>
      </InputGroup>
      <Button onClick={() => navigate("/members/form")}>등록</Button>
      <MembersTable members={membersPages[page - 1]} />
      <div className="mx-auto">
        <Pagination page={page} totalPages={membersPages.length} onPaginate={setPage} />
      </div>
    </Stack>
  );
}

export default Members;
