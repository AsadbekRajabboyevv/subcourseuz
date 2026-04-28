import {Description, Name} from "../../common/model/base";

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
