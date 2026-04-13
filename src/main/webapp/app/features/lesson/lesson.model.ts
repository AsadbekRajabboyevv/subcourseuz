export interface Lesson {
  id: number;
  name: string;
  lessonNumber: string;
}

export interface LessonInfo extends Lesson{
  videoUrl: string;
  courseName: string;
  courseImagePath: string;
  textContent: string;
}

export interface LessonCreate {
  name: string;
  lessonNumber: string;
  videoUrl: string;
  textContent: string;
  courseId: number;
}

export interface LessonUpdate {
  name?: string | null;
  lessonNumber?: string | null;
  videoUrl?: string | null;
  textContent?: string | null;
  courseId?: number | null;
}


