import {BaseFilter} from "../../common/model/base";

export interface Test {
  id: number;
  name: string;
  description: string;
  price: number;
  lang: string;
  duration: number;
  isPublished: boolean;
  scienceId: number;
  scienceName: string;
  courseId: number;
  courseName: string;
  lessonId: number;
  gradeId: number;
  gradeName: string;
  imagePath: string;
  createdAt: string;
  updatedAt: string;
}

export interface TestCreate {
  name: string;
  description?: string;
  price: number;
  lang: string;
  lessonId?: number;
  courseId?: number;
  scienceId?: number;
  gradeId?: number;
  duration: number;
  isPublished: boolean;
  questions: QuestionCreate[];
}

export interface QuestionCreate {
  text: string;
  image?: File;
  correctOptionIndex: number;
  options: OptionCreate[];
}

export interface OptionCreate {
  text: string;
  image?: File;
}

export interface SubmitAnswer {
  sessionId: number;
  questionId: number;
  optionId: number;
}

export interface TestReview {
  questionId: number;
  questionText: string;
  selectedOptionId: number;
  selectedOptionText: string;
  wrong: boolean;
}

export interface TestResult {
  score: number;
  correctAnswers: number;
  totalQuestions: number
}

export interface TestUpdate {
  name?: string;
  description?: string;
  price?: number;
  lang?: string;
  lessonId?: number;
  courseId?: number;
  scienceId?: number;
  gradeId?: number;
  duration?: number;
  isPublished?: boolean;
  image?: File;
  questions?: QuestionUpdate[];
}

export interface QuestionUpdate {
  id?: number;
  text?: string;
  image?: File;
  correctOptionIndex?: number;
  options?: OptionUpdate[];
}

export interface OptionUpdate {
  id?: number;
  text?: string;
  image?: File;
}

export interface TestFilter extends BaseFilter{
  name?: string;
  scienceName?: string;
  lang?: string;
  durationFrom?: number;
  durationTo?: number;
  myTests?: boolean;
  gradeId?: number;
  priceFrom?: number;
  priceTo?: number;
}
