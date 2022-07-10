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
 * Check if a given object implements the NotImplemented interface.
 */
export function instanceOfNotImplemented(value: object): boolean {
  let isInstance = true;
  isInstance = isInstance && 'message' in value;

  return isInstance;
}

export function NotImplementedFromJSON(json: any): NotImplemented {
  return NotImplementedFromJSONTyped(json, false);
}

export function NotImplementedFromJSONTyped(json: any, ignoreDiscriminator: boolean): NotImplemented {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    message: json['message'],
  };
}

export function NotImplementedToJSON(value?: NotImplemented | null): any {
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
