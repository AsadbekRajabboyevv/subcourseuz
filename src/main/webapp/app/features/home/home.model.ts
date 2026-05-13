export interface HomePageStats {
  coursesCount: number;
  usersCount: number;
  videoCoursesCount: number;
  testsCount: number;
}

export interface HomeCourseGrade {
  id: number;
  name: string;
  description: string;
}

export interface HomeCourse {
  name: string;
  lessonsCount: number;
  studentsCount: number;
  ownerName: string;
  price: number;
  imagePath: string;
  lang: string;
  isPublished: boolean;
  slug: string;
}

export interface HomeComment {
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

export interface HomePageData {
  stats: HomePageStats;
  courseGrades: HomeCourseGrade[];
  topCourses: HomeCourse[];
  topComments: HomeComment[];
}

export type StatKey = keyof HomePageStats;

export interface StatCard {
  key: StatKey;
  label: string;
  icon: string;
  color: string;
  borderH: string;
}

export interface QuickAction {
  title: string;
  description: string;
  icon: string;
  color: string;
  borderH: string;
  route: string;
}
