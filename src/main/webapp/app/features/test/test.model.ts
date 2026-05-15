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
  enabledViewCorrectAnswers: boolean;
  maxScore: number;
  count: number;
  questions: Question[];
}

export interface Question {
  id: number;
  text: string;
  imagePath: string;
  options: Option[]
}

export interface Option {
  id: number;
  text: string;
  imageUrl: string;
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
  count: number;
  maxScore: number;
  enabledViewCorrectAnswers: boolean;
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

export interface TestResult {
  score: number;
  correctAnswers: number;
  totalQuestions: number;
  spentTime: string;
  startedAt: string;
  finishedAt: string;
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
  count?: number;
  maxScore?: number;
  enabledViewCorrectAnswers?: boolean;
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
  isPublished?: boolean;
}

export interface TestSession {
  id: number;
  testId: number;
  testName: string;
  testDescription: string;
  imagePath: string;
  status: string;
  remainingSeconds: number;
  questions: TestSessionQuestion[];
}

export interface TestSessionQuestion {
  id: number;
  orderNumber: number;
  text: string;
  imagePath: string;
  selectedOptionId: number | null;
  answered: boolean;
  options: TestSessionOption[];
}
export interface TestGenerateRequestDto {
  name: string;
  description?: string;
  lang: string;
  count: number;
  isPublished: boolean;
  scienceId: number;
  gradeId: number;
  courseId?: number;
  lessonId?: number;
  duration: number;
  price: number;
  maxScore: number;
  enabledViewCorrectAnswers: boolean;
}
export interface TestSessionOption {
  id: number;
  text: string;
  imagePath: string;
}

export interface TestReview {
  testName: string;
  testDescription: string;
  score: number;
  maxScore: number;
  correctAnswers: number;
  totalQuestions: number;
  testImagePath: string | null;
  testLang: string;
  testScience: string;
  testGrade: string;
  testLesson: string | null;
  testCourse: string | null;
  testAuthor: string;
  spentTime: string;
  startedAt: string;
  finishedAt: string;
  enabledViewCorrectAnswers: boolean;
  questions: TestReviewQuestion[];
}

export interface TestReviewQuestion {
  questionText: string;
  imagePath: string | null;
  selectedOptionOrderNumber: number | null;
  correctOptionId: number | null;
  isCorrect: boolean;
  options: TestReviewOption[];
}

export interface TestReviewOption {
  id: number;
  text: string;
  imagePath: string | null;
}
