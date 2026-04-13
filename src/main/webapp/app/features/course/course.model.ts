import {BaseFilter, Description, Name} from "../../common/model/base";

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

export interface CourseCreate {
  name: string;
  description: string;
  duration: number;
  durationType: string;
  scienceId: number;
  gradeId: number;
  price: number;
  lang: string;
  isVideoCourse: boolean;
  isPublished: boolean;
}

export interface CourseUpdate {
  name?: string | null;
  description?: string | null;
  duration?: number | null;
  durationType?: string | null;
  scienceId?: number | null;
  gradeId?: number | null;
  price?: number | null;
  lang?: string | null;
  isVideoCourse?: boolean | null;
}

export interface CourseInfo extends Course {
  description: string;
  gradeName: string;
  scienceName: string;
  duration: number;
  durationType: string;
  purchased: boolean;
  scienceId: number;
  gradeId: number;
}

export interface CourseFilter extends BaseFilter {
  search?: string;
  scienceId?: number;
  gradeId?: number;
  priceTo?: number;
  priceFrom?: number;
  lang?: string;
  durationType?: DurationType;
  duration?: number;
}

export enum DurationType {
  OY = "OY",
  YIL = "YIL",
  KUN = "KUN",
  SOAT = "SOAT",
}
//=========================Grades========================

export interface CourseGrade {
  id: number;
  name: string;
}
export interface OneCourseGrade {
  id: number;
  name: Name;
  description: Description;
}
export interface CourseGradeCreate {
  name: Name;
  description: Description;
}

export interface CourseGradeUpdate {
  name?: Name | null;
  description?: Description | null;
}

//=========================Science========================

export interface CourseScience {
  id: number;
  name: string;
  imagePath: string;
}

export interface OneCourseScience {
  id: number;
  name: Name;
  description: Description;
  imagePath: string;
}

export interface CourseScienceCreate {
  name: Name;
  description: Description;
}

export interface CourseScienceUpdate {
  name?: Name | null;
  description?: Description | null;
}
