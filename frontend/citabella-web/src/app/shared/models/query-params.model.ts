export interface BaseQueryParams {
  page?: number;
  size?: number;
  sort?: string[];
}

export interface ActiveQueryParams extends BaseQueryParams {
  active?: boolean;
}

export interface SearchableQueryParams extends ActiveQueryParams {
  search?: string;
}

export interface UserQueryParams extends BaseQueryParams {
  accountStatus?: string;
  search?: string;
}
