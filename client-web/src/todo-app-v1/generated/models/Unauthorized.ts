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

export function UnauthorizedFromJSON(json: any): Unauthorized {
  return UnauthorizedFromJSONTyped(json, false);
}

export function UnauthorizedFromJSONTyped(json: any, ignoreDiscriminator: boolean): Unauthorized {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    message: json['message'],
  };
}

export function UnauthorizedToJSON(value?: Unauthorized | null): any {
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
