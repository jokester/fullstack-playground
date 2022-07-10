/* tslint:disable */
/* eslint-disable */
/**
 * TodoAPI
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { exists, mapValues } from '../runtime';
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

export function TodoFromJSON(json: any): Todo {
  return TodoFromJSONTyped(json, false);
}

export function TodoFromJSONTyped(json: any, ignoreDiscriminator: boolean): Todo {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    id: json['id'],
    title: json['title'],
    desc: json['desc'],
    finished: json['finished'],
    updatedAt: new Date(json['updatedAt']),
  };
}

export function TodoToJSON(value?: Todo | null): any {
  if (value === undefined) {
    return undefined;
  }
  if (value === null) {
    return null;
  }
  return {
    id: value.id,
    title: value.title,
    desc: value.desc,
    finished: value.finished,
    updatedAt: value.updatedAt.toISOString(),
  };
}
