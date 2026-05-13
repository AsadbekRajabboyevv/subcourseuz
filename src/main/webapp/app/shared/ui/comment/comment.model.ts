export interface CommentRequest {
  text: string;
  rating: number;
  courseSlug?: string | null;
  lessonId?: number | null;
  testId?: number | null;
}

export interface CommentResponse {
  id: number;
  text: string;
  createdByName: string;
  createdBy: number;
  createdAt: string;
  rating: number;
  courseSlug: string | null;
  lessonId: number | null;
  testId: number | null;
}
