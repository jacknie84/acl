import { isArrayLike, toArray } from "lodash";

export function parameterizeValue(getKey: (isObject: boolean) => string, value?: any): string[][] {
  switch (typeof value) {
    case "string":
      return [[getKey(false), value]];
    case "bigint":
    case "number":
    case "boolean":
      return [[getKey(false), `${value}`]];
    case "undefined":
    case "object":
      if (!value) {
        return [];
      } else if (isArrayLike(value)) {
        return parameterizeArray(toArray(value), getKey(true));
      } else {
        return parameterizeObject(value as Record<string, any>, getKey(true));
      }
    default:
      throw new TypeError(`지원 하지 않는 타입: ${typeof value}`);
  }
}

export function parameterizeObject(target: Record<string, any>, keyPrefix?: string) {
  return Object.entries(target).map(([key, value]) => {
    const decoratedKey = keyPrefix ? `${keyPrefix}.${key}` : key;
    return parameterizeValue(() => decoratedKey, value).reduce((prev, curr) => prev.concat(curr), []);
  });
}

export function parameterizeArray(array: any[], keyPrefix: string) {
  return array.map((value, index) => {
    const getKey = (isObject: boolean) => (isObject ? `${keyPrefix}[${index}]` : keyPrefix);
    return parameterizeValue(getKey, value).reduce((prev, curr) => prev.concat(curr), []);
  });
}

export function parameterizePageable(pageable: Pageable) {
  const page = ["page", `${pageable.pageNumber}`];
  const size = ["size", `${pageable.pageSize}`];
  const sort = pageable.sort?.orders?.map(({ property, direction }) => ["sort", `${property},${direction}`]) ?? [];
  return [page, size, ...sort];
}

export type PageRequestParams<F> = {
  filter: F;
  pageable: Pageable;
};

export type Page<T> = {
  content: T[];
  pageable: Pageable;
  totalPages: number;
  totalElements: number;
};

export type Pageable = {
  pageNumber: number;
  pageSize: number;
  sort?: { orders?: SortOrder[] };
};

export type SortOrder = {
  property: string;
  direction: "asc" | "desc";
};
