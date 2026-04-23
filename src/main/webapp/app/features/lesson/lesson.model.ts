export interface Lesson {
  id: number;
  name: string;
  lessonNumber: string;
  isPublished: boolean;
}

export interface LessonInfo extends Lesson{
  videoUrl: string;
  courseName: string;
  courseImagePath: string;
  textContent: string;
  isPublished: boolean;
  courseId: number;
  fileUrls: string[]
}

export interface LessonCreate {
  name: string;
  lessonNumber: number;
  videoUrl: string;
  textContent: string;
  courseId: number | null;
  isPublished: boolean;
}

export interface LessonUpdate {
  name?: string | null;
  lessonNumber?: string | null;
  videoUrl?: string | null;
  textContent?: string | null;
  courseId?: number | null;
  isPublished?: boolean | null;
}


