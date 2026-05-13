export interface Lesson {
  id: number;
  name: string;
  lessonNumber: string;
  isPublished: boolean;
}

export interface LessonInfo extends Lesson {
  videoUrl: string;
  courseName: string;
  courseImagePath: string;
  textContent: string;
  isPublished: boolean;
  courseSlug: string;
  fileUrls: string[]
}

export interface LessonCreate {
  name: string;
  lessonNumber: number;
  videoUrl: string;
  textContent: string;
  courseSlug: string | null;
  isPublished: boolean;
}

export interface LessonUpdate {
  name?: string | null;
  lessonNumber?: string | null;
  videoUrl?: string | null;
  textContent?: string | null;
  isPublished?: boolean | null;
}


