export interface BaseResponseDto<T> {
  success: boolean;
  message: string;
  timestamp: string;
  data: T;
}

export interface UserResponseDto {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  position: string;
}

export interface AuthResponseDto {
  bearerToken: string;
  expiresIn: string;
  user: UserResponseDto;
}

export interface LoginRequestDto {
  email: string;
  password: string;
}

export interface RegisterRequestDto {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  bio?: string;
  birthDate: string;
  phone: string;
  position: string;
}
