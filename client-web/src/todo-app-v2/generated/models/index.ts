/* tslint:disable */
/* eslint-disable */
/**
 *
 * @export
 * @interface AccessToken
 */
export interface AccessToken {
  /**
   *
   * @type {string}
   * @memberof AccessToken
   */
  value: string;
}
/**
 *
 * @export
 * @interface CreateTodoRequest
 */
export interface CreateTodoRequest {
  /**
   *
   * @type {string}
   * @memberof CreateTodoRequest
   */
  title: string;
  /**
   *
   * @type {string}
   * @memberof CreateTodoRequest
   */
  description: string;
}
/**
 *
 * @export
 * @interface CreateUserRequest
 */
export interface CreateUserRequest {
  /**
   *
   * @type {string}
   * @memberof CreateUserRequest
   */
  email: string;
  /**
   *
   * @type {string}
   * @memberof CreateUserRequest
   */
  initialPass: string;
  /**
   *
   * @type {UserProfile}
   * @memberof CreateUserRequest
   */
  profile: UserProfile;
}
/**
 *
 * @export
 * @interface CreateUserResponse
 */
export interface CreateUserResponse {
  /**
   *
   * @type {number}
   * @memberof CreateUserResponse
   */
  userId: number;
  /**
   *
   * @type {UserProfile}
   * @memberof CreateUserResponse
   */
  userProfile: UserProfile;
}
/**
 *
 * @export
 * @interface ListTodoResponse
 */
export interface ListTodoResponse {
  /**
   *
   * @type {Array<TodoItem>}
   * @memberof ListTodoResponse
   */
  todos?: Array<TodoItem>;
}
/**
 *
 * @export
 * @interface LoginRequest
 */
export interface LoginRequest {
  /**
   *
   * @type {string}
   * @memberof LoginRequest
   */
  email: string;
  /**
   *
   * @type {string}
   * @memberof LoginRequest
   */
  password: string;
}
/**
 *
 * @export
 * @interface LoginResponse
 */
export interface LoginResponse {
  /**
   *
   * @type {number}
   * @memberof LoginResponse
   */
  userId: number;
  /**
   *
   * @type {UserProfile}
   * @memberof LoginResponse
   */
  userProfile: UserProfile;
  /**
   *
   * @type {RefreshToken}
   * @memberof LoginResponse
   */
  refreshToken: RefreshToken;
  /**
   *
   * @type {AccessToken}
   * @memberof LoginResponse
   */
  accessToken: AccessToken;
}
/**
 *
 * @export
 * @interface NotFound
 */
export interface NotFound {
  /**
   *
   * @type {string}
   * @memberof NotFound
   */
  error: string;
}
/**
 *
 * @export
 * @interface RefreshToken
 */
export interface RefreshToken {
  /**
   *
   * @type {string}
   * @memberof RefreshToken
   */
  value: string;
}
/**
 *
 * @export
 * @interface TodoItem
 */
export interface TodoItem {
  /**
   *
   * @type {number}
   * @memberof TodoItem
   */
  todoId: number;
  /**
   *
   * @type {number}
   * @memberof TodoItem
   */
  userId: number;
  /**
   *
   * @type {string}
   * @memberof TodoItem
   */
  title: string;
  /**
   *
   * @type {string}
   * @memberof TodoItem
   */
  description: string;
  /**
   *
   * @type {Date}
   * @memberof TodoItem
   */
  finishedAt?: Date;
}
/**
 *
 * @export
 * @interface Unauthenticated
 */
export interface Unauthenticated {
  /**
   *
   * @type {string}
   * @memberof Unauthenticated
   */
  error: string;
}
/**
 *
 * @export
 * @interface Unauthorized
 */
export interface Unauthorized {
  /**
   *
   * @type {string}
   * @memberof Unauthorized
   */
  error: string;
}
/**
 *
 * @export
 * @interface UserProfile
 */
export interface UserProfile {
  /**
   *
   * @type {string}
   * @memberof UserProfile
   */
  nickname?: string;
  /**
   *
   * @type {string}
   * @memberof UserProfile
   */
  avatarUrl?: string;
}
