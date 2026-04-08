import {BaseFilter, Descriptive, Nameable} from "../../../common/model/base";

export interface Course {
  id: number;
  name: string;
  lessonsCount: number;
  studentsCount: number;
  ownerName: string;
  price: number;
  imagePath: string;
  lang: string;
}

export interface CourseInfo extends Course {
  description: string;
  gradeName: string;
  scienceName: string;
  duration: number;
  durationType: DurationType;
}

export interface CourseRequest {
  name: string;
  description: string;
  duration: number;
  durationType: DurationType;
  scienceId: number;
  gradeId: number;
  price: number;
}

export interface OneCourseGrade {
  id: number;
  name: Nameable;
  description: Descriptive;
}
export interface CourseGrade {
  id: number;
  name: string;
  description: string;
}
export interface CourseGradeRequest {
  name: Nameable;
  description: Descriptive;
}

export interface CourseFilter extends BaseFilter {
  name: string;
  gradeId: number;
  scienceId: number;
  ownerName: string;
  priceFrom: number;
  priceTo: number;
  lang: string;
  duration: number;
  durationType: DurationType;
}
export enum DurationType {
  OY,
  YIL,
  KUN,
  SOAT
}
