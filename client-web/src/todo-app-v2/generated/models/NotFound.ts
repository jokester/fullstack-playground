/* tslint:disable */
/* eslint-disable */
/**
 * user-todo
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.1
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

export function NotFoundFromJSON(json: any): NotFound {
  return NotFoundFromJSONTyped(json, false);
}

export function NotFoundFromJSONTyped(json: any, ignoreDiscriminator: boolean): NotFound {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    message: json['message'],
  };
}

export function NotFoundToJSON(value?: NotFound | null): any {
  if (value === undefined) {
    return undefined;
  }
  if (value === null) {
    return null;
  }
  return {
    message: value.message,
  };
}
