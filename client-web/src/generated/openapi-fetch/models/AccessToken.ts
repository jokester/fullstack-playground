/* tslint:disable */
/* eslint-disable */
/**
 * User Todos
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

export function AccessTokenFromJSON(json: any): AccessToken {
  return AccessTokenFromJSONTyped(json, false);
}

export function AccessTokenFromJSONTyped(json: any, ignoreDiscriminator: boolean): AccessToken {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    value: json['value'],
  };
}

export function AccessTokenToJSON(value?: AccessToken | null): any {
  if (value === undefined) {
    return undefined;
  }
  if (value === null) {
    return null;
  }
  return {
    value: value.value,
  };
}