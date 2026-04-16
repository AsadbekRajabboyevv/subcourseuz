export interface UserResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  position: string;
  role: string;
  permissions: string[];
  imagePath: string;
}

export interface AuthResponse {
  bearerToken: string;
  expiresIn: string;
  user: UserResponse;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  bio?: string;
  birthDate: string;
  phone: string;
  position: string;
}
