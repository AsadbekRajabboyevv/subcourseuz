export interface Base<T> {
  success: boolean;
  message: string;
  timestamp: string;
  data: T;
}

export interface BaseAudit {
  createdAt: string;
  updatedAt: string;
  deletedAt: string;
  createdBy: number;
  updatedBy: number;
  deletedBy: number;
}

export interface Pageable<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface Nameable {
  nameUz: string;
  nameRu: string;
  nameEn: string;
  nameCrl: string;
}

export interface BaseFilter {
  id: number;
  createdAtFrom: string;
  createdAtTo: string;
  updatedAtFrom: string;
  updatedAtTo: string;
  createdBy: number;
  updatedBy: number;
  createdByName: string;
  updatedByName: string;
}

export interface Descriptive {
  descriptionUz: string;
  descriptionRu: string;
  descriptionEn: string;
  descriptionCrl: string;
}
