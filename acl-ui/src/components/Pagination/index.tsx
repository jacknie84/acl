import { range } from "lodash";
import { useMemo } from "react";
import { Pagination as BsPagination } from "react-bootstrap";

type Props = {
  page?: number;
  size?: number;
  totalPages?: number;
  onPaginate: (page: number) => void;
};

function Pagination({ page = 1, size = 5, totalPages = 1, onPaginate }: Props) {
  const blocks = useMemo(() => {
    const pageMod = page % size;
    const current = Math.floor(page / size) + (pageMod > 0 ? 1 : 0);
    const totalPagesMod = totalPages % size;
    const last = Math.floor(totalPages / size) + (totalPagesMod > 0 ? 1 : 0);
    return { current, last };
  }, [page, size, totalPages]);
  const pages = useMemo(() => {
    const start = (blocks.current - 1) * size + 1;
    const total = totalPages + 1;
    const end = total < start + size ? total : start + size;
    return range(start, end);
  }, [blocks, size, totalPages]);

  return (
    <BsPagination size="sm">
      <BsPagination.First disabled={page === 1} onClick={() => onPaginate(1)} />
      <BsPagination.Prev disabled={blocks.current === 1} onClick={() => onPaginate((blocks.current - 1) * size)} />
      {pages.map((no) => (
        <BsPagination.Item key={no} active={no === page} onClick={() => onPaginate(no)}>
          {no}
        </BsPagination.Item>
      ))}
      <BsPagination.Next disabled={blocks.current === blocks.last} onClick={() => onPaginate(blocks.current * size + 1)} />
      <BsPagination.Last disabled={page === totalPages} onClick={() => onPaginate(totalPages)} />
    </BsPagination>
  );
}

export default Pagination;
