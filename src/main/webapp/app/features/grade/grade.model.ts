import {Description, Name} from "../../common/model/base";

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
