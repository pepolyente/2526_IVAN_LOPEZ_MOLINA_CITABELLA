export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  role: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
}

export interface UserInfoResponse {
  id: number;
  username: string;
  email: string;
  role: string;
}
