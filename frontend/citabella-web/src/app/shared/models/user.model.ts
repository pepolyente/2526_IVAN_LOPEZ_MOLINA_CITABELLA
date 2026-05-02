export type AccountStatus = 'PENDING' | 'ACTIVE' | 'LOCKED';
export type ProfileType = 'NONE' | 'CLIENT' | 'EMPLOYEE';
export interface UserResponse {
  id: number;
  username: string;
  email: string;
  role: string;
  accountStatus: AccountStatus;
  profileType: ProfileType;
}

export interface UserUpdateRequest {
  username: string;
  email: string;
  password: string;
}
