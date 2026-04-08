import {Descriptive, Nameable} from "../../../common/model/base";

export interface ScienceResponse {
  id: number;
  name: string;
  description: string;
  imagePath: string;
}

export interface OneScienceResponse {
  id: number;
  imagePath: string;
  name: Nameable;
  description: Descriptive;
}

export interface ScienceUpdateRequest {
  id: number;
  name: Nameable;
  description: Descriptive;
}

export interface ScienceRequest {
  name: Nameable;
  description: Descriptive;
}
