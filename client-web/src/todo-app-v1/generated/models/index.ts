/* tslint:disable */
/* eslint-disable */
/**
 *
 * @export
 * @interface BadRequest
 */
export interface BadRequest {
  /**
   *
   * @type {string}
   * @memberof BadRequest
   */
  message: string;
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
  message: string;
}
/**
 *
 * @export
 * @interface NotImplemented
 */
export interface NotImplemented {
  /**
   *
   * @type {string}
   * @memberof NotImplemented
   */
  message: string;
}
/**
 *
 * @export
 * @interface ServerError
 */
export interface ServerError {
  /**
   *
   * @type {string}
   * @memberof ServerError
   */
  message: string;
}
/**
 *
 * @export
 * @interface Todo
 */
export interface Todo {
  /**
   *
   * @type {number}
   * @memberof Todo
   */
  id: number;
  /**
   *
   * @type {string}
   * @memberof Todo
   */
  title: string;
  /**
   *
   * @type {string}
   * @memberof Todo
   */
  desc: string;
  /**
   *
   * @type {boolean}
   * @memberof Todo
   */
  finished: boolean;
  /**
   *
   * @type {Date}
   * @memberof Todo
   */
  updatedAt: Date;
}
/**
 *
 * @export
 * @interface TodoCreateRequest
 */
export interface TodoCreateRequest {
  /**
   *
   * @type {string}
   * @memberof TodoCreateRequest
   */
  title: string;
  /**
   *
   * @type {string}
   * @memberof TodoCreateRequest
   */
  desc: string;
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
  message: string;
}
