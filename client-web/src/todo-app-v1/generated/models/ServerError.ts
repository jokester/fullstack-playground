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

export function ServerErrorFromJSON(json: any): ServerError {
  return ServerErrorFromJSONTyped(json, false);
}

export function ServerErrorFromJSONTyped(json: any, ignoreDiscriminator: boolean): ServerError {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    message: json['message'],
  };
}

export function ServerErrorToJSON(value?: ServerError | null): any {
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
