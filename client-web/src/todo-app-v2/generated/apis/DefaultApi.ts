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

import * as runtime from '../runtime';
import type {
  AuthSuccess,
  BadRequest,
  CreateTodoRequest,
  CreateUserRequest,
  Forbidden,
  LoginRequest,
  NotFound,
  NotImplemented,
  ServerError,
  TodoItem,
  TodoList,
  Unauthorized,
  UserAccount,
  UserProfile,
} from '../models';
import {
  AuthSuccessFromJSON,
  AuthSuccessToJSON,
  BadRequestFromJSON,
  BadRequestToJSON,
  CreateTodoRequestFromJSON,
  CreateTodoRequestToJSON,
  CreateUserRequestFromJSON,
  CreateUserRequestToJSON,
  ForbiddenFromJSON,
  ForbiddenToJSON,
  LoginRequestFromJSON,
  LoginRequestToJSON,
  NotFoundFromJSON,
  NotFoundToJSON,
  NotImplementedFromJSON,
  NotImplementedToJSON,
  ServerErrorFromJSON,
  ServerErrorToJSON,
  TodoItemFromJSON,
  TodoItemToJSON,
  TodoListFromJSON,
  TodoListToJSON,
  UnauthorizedFromJSON,
  UnauthorizedToJSON,
  UserAccountFromJSON,
  UserAccountToJSON,
  UserProfileFromJSON,
  UserProfileToJSON,
} from '../models';

export interface DeleteUserTodoApiUsersUseridTodosTodoidRequest {
  userId: number;
  todoId: number;
}

export interface GetUserTodoApiUsersUseridTodosRequest {
  userId: number;
}

export interface PatchUserTodoApiUsersUseridTodosTodoidRequest {
  userId: number;
  todoId: number;
  todoItem: TodoItem;
}

export interface PostUserTodoApiAuthLoginRequest {
  loginRequest: LoginRequest;
}

export interface PostUserTodoApiUsersRequest {
  createUserRequest: CreateUserRequest;
}

export interface PostUserTodoApiUsersUseridTodosRequest {
  userId: number;
  createTodoRequest: CreateTodoRequest;
}

export interface PutUserTodoApiUsersUseridProfileRequest {
  userId: number;
  userProfile: UserProfile;
}

/**
 *
 */
export class DefaultApi extends runtime.BaseAPI {
  /**
   */
  async deleteUserTodoApiUsersUseridTodosTodoidRaw(
    requestParameters: DeleteUserTodoApiUsersUseridTodosTodoidRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<runtime.ApiResponse<void>> {
    if (requestParameters.userId === null || requestParameters.userId === undefined) {
      throw new runtime.RequiredError(
        'userId',
        'Required parameter requestParameters.userId was null or undefined when calling deleteUserTodoApiUsersUseridTodosTodoid.',
      );
    }

    if (requestParameters.todoId === null || requestParameters.todoId === undefined) {
      throw new runtime.RequiredError(
        'todoId',
        'Required parameter requestParameters.todoId was null or undefined when calling deleteUserTodoApiUsersUseridTodosTodoid.',
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    if (this.configuration && this.configuration.accessToken) {
      const token = this.configuration.accessToken;
      const tokenString = await token('httpAuth', []);

      if (tokenString) {
        headerParameters['Authorization'] = `Bearer ${tokenString}`;
      }
    }
    const response = await this.request(
      {
        path: `/user_todo_api/users/{userId}/todos/{todoId}`
          .replace(`{${'userId'}}`, encodeURIComponent(String(requestParameters.userId)))
          .replace(`{${'todoId'}}`, encodeURIComponent(String(requestParameters.todoId))),
        method: 'DELETE',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides,
    );

    return new runtime.VoidApiResponse(response);
  }

  /**
   */
  async deleteUserTodoApiUsersUseridTodosTodoid(
    requestParameters: DeleteUserTodoApiUsersUseridTodosTodoidRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<void> {
    await this.deleteUserTodoApiUsersUseridTodosTodoidRaw(requestParameters, initOverrides);
  }

  /**
   */
  async getUserTodoApiUsersUseridTodosRaw(
    requestParameters: GetUserTodoApiUsersUseridTodosRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<runtime.ApiResponse<TodoList>> {
    if (requestParameters.userId === null || requestParameters.userId === undefined) {
      throw new runtime.RequiredError(
        'userId',
        'Required parameter requestParameters.userId was null or undefined when calling getUserTodoApiUsersUseridTodos.',
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    if (this.configuration && this.configuration.accessToken) {
      const token = this.configuration.accessToken;
      const tokenString = await token('httpAuth', []);

      if (tokenString) {
        headerParameters['Authorization'] = `Bearer ${tokenString}`;
      }
    }
    const response = await this.request(
      {
        path: `/user_todo_api/users/{userId}/todos`.replace(
          `{${'userId'}}`,
          encodeURIComponent(String(requestParameters.userId)),
        ),
        method: 'GET',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides,
    );

    return new runtime.JSONApiResponse(response, (jsonValue) => TodoListFromJSON(jsonValue));
  }

  /**
   */
  async getUserTodoApiUsersUseridTodos(
    requestParameters: GetUserTodoApiUsersUseridTodosRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<TodoList> {
    const response = await this.getUserTodoApiUsersUseridTodosRaw(requestParameters, initOverrides);
    return await response.value();
  }

  /**
   */
  async patchUserTodoApiUsersUseridTodosTodoidRaw(
    requestParameters: PatchUserTodoApiUsersUseridTodosTodoidRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<runtime.ApiResponse<TodoItem>> {
    if (requestParameters.userId === null || requestParameters.userId === undefined) {
      throw new runtime.RequiredError(
        'userId',
        'Required parameter requestParameters.userId was null or undefined when calling patchUserTodoApiUsersUseridTodosTodoid.',
      );
    }

    if (requestParameters.todoId === null || requestParameters.todoId === undefined) {
      throw new runtime.RequiredError(
        'todoId',
        'Required parameter requestParameters.todoId was null or undefined when calling patchUserTodoApiUsersUseridTodosTodoid.',
      );
    }

    if (requestParameters.todoItem === null || requestParameters.todoItem === undefined) {
      throw new runtime.RequiredError(
        'todoItem',
        'Required parameter requestParameters.todoItem was null or undefined when calling patchUserTodoApiUsersUseridTodosTodoid.',
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';

    if (this.configuration && this.configuration.accessToken) {
      const token = this.configuration.accessToken;
      const tokenString = await token('httpAuth', []);

      if (tokenString) {
        headerParameters['Authorization'] = `Bearer ${tokenString}`;
      }
    }
    const response = await this.request(
      {
        path: `/user_todo_api/users/{userId}/todos/{todoId}`
          .replace(`{${'userId'}}`, encodeURIComponent(String(requestParameters.userId)))
          .replace(`{${'todoId'}}`, encodeURIComponent(String(requestParameters.todoId))),
        method: 'PATCH',
        headers: headerParameters,
        query: queryParameters,
        body: TodoItemToJSON(requestParameters.todoItem),
      },
      initOverrides,
    );

    return new runtime.JSONApiResponse(response, (jsonValue) => TodoItemFromJSON(jsonValue));
  }

  /**
   */
  async patchUserTodoApiUsersUseridTodosTodoid(
    requestParameters: PatchUserTodoApiUsersUseridTodosTodoidRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<TodoItem> {
    const response = await this.patchUserTodoApiUsersUseridTodosTodoidRaw(requestParameters, initOverrides);
    return await response.value();
  }

  /**
   */
  async postUserTodoApiAuthLoginRaw(
    requestParameters: PostUserTodoApiAuthLoginRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<runtime.ApiResponse<AuthSuccess>> {
    if (requestParameters.loginRequest === null || requestParameters.loginRequest === undefined) {
      throw new runtime.RequiredError(
        'loginRequest',
        'Required parameter requestParameters.loginRequest was null or undefined when calling postUserTodoApiAuthLogin.',
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';

    const response = await this.request(
      {
        path: `/user_todo_api/auth/login`,
        method: 'POST',
        headers: headerParameters,
        query: queryParameters,
        body: LoginRequestToJSON(requestParameters.loginRequest),
      },
      initOverrides,
    );

    return new runtime.JSONApiResponse(response, (jsonValue) => AuthSuccessFromJSON(jsonValue));
  }

  /**
   */
  async postUserTodoApiAuthLogin(
    requestParameters: PostUserTodoApiAuthLoginRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<AuthSuccess> {
    const response = await this.postUserTodoApiAuthLoginRaw(requestParameters, initOverrides);
    return await response.value();
  }

  /**
   */
  async postUserTodoApiAuthRefreshTokenRaw(
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<runtime.ApiResponse<AuthSuccess>> {
    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    if (this.configuration && this.configuration.accessToken) {
      const token = this.configuration.accessToken;
      const tokenString = await token('httpAuth', []);

      if (tokenString) {
        headerParameters['Authorization'] = `Bearer ${tokenString}`;
      }
    }
    const response = await this.request(
      {
        path: `/user_todo_api/auth/refresh_token`,
        method: 'POST',
        headers: headerParameters,
        query: queryParameters,
      },
      initOverrides,
    );

    return new runtime.JSONApiResponse(response, (jsonValue) => AuthSuccessFromJSON(jsonValue));
  }

  /**
   */
  async postUserTodoApiAuthRefreshToken(
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<AuthSuccess> {
    const response = await this.postUserTodoApiAuthRefreshTokenRaw(initOverrides);
    return await response.value();
  }

  /**
   */
  async postUserTodoApiUsersRaw(
    requestParameters: PostUserTodoApiUsersRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<runtime.ApiResponse<UserAccount>> {
    if (requestParameters.createUserRequest === null || requestParameters.createUserRequest === undefined) {
      throw new runtime.RequiredError(
        'createUserRequest',
        'Required parameter requestParameters.createUserRequest was null or undefined when calling postUserTodoApiUsers.',
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';

    const response = await this.request(
      {
        path: `/user_todo_api/users`,
        method: 'POST',
        headers: headerParameters,
        query: queryParameters,
        body: CreateUserRequestToJSON(requestParameters.createUserRequest),
      },
      initOverrides,
    );

    return new runtime.JSONApiResponse(response, (jsonValue) => UserAccountFromJSON(jsonValue));
  }

  /**
   */
  async postUserTodoApiUsers(
    requestParameters: PostUserTodoApiUsersRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<UserAccount> {
    const response = await this.postUserTodoApiUsersRaw(requestParameters, initOverrides);
    return await response.value();
  }

  /**
   */
  async postUserTodoApiUsersUseridTodosRaw(
    requestParameters: PostUserTodoApiUsersUseridTodosRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<runtime.ApiResponse<TodoItem>> {
    if (requestParameters.userId === null || requestParameters.userId === undefined) {
      throw new runtime.RequiredError(
        'userId',
        'Required parameter requestParameters.userId was null or undefined when calling postUserTodoApiUsersUseridTodos.',
      );
    }

    if (requestParameters.createTodoRequest === null || requestParameters.createTodoRequest === undefined) {
      throw new runtime.RequiredError(
        'createTodoRequest',
        'Required parameter requestParameters.createTodoRequest was null or undefined when calling postUserTodoApiUsersUseridTodos.',
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';

    if (this.configuration && this.configuration.accessToken) {
      const token = this.configuration.accessToken;
      const tokenString = await token('httpAuth', []);

      if (tokenString) {
        headerParameters['Authorization'] = `Bearer ${tokenString}`;
      }
    }
    const response = await this.request(
      {
        path: `/user_todo_api/users/{userId}/todos`.replace(
          `{${'userId'}}`,
          encodeURIComponent(String(requestParameters.userId)),
        ),
        method: 'POST',
        headers: headerParameters,
        query: queryParameters,
        body: CreateTodoRequestToJSON(requestParameters.createTodoRequest),
      },
      initOverrides,
    );

    return new runtime.JSONApiResponse(response, (jsonValue) => TodoItemFromJSON(jsonValue));
  }

  /**
   */
  async postUserTodoApiUsersUseridTodos(
    requestParameters: PostUserTodoApiUsersUseridTodosRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<TodoItem> {
    const response = await this.postUserTodoApiUsersUseridTodosRaw(requestParameters, initOverrides);
    return await response.value();
  }

  /**
   */
  async putUserTodoApiUsersUseridProfileRaw(
    requestParameters: PutUserTodoApiUsersUseridProfileRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<runtime.ApiResponse<UserAccount>> {
    if (requestParameters.userId === null || requestParameters.userId === undefined) {
      throw new runtime.RequiredError(
        'userId',
        'Required parameter requestParameters.userId was null or undefined when calling putUserTodoApiUsersUseridProfile.',
      );
    }

    if (requestParameters.userProfile === null || requestParameters.userProfile === undefined) {
      throw new runtime.RequiredError(
        'userProfile',
        'Required parameter requestParameters.userProfile was null or undefined when calling putUserTodoApiUsersUseridProfile.',
      );
    }

    const queryParameters: any = {};

    const headerParameters: runtime.HTTPHeaders = {};

    headerParameters['Content-Type'] = 'application/json';

    if (this.configuration && this.configuration.accessToken) {
      const token = this.configuration.accessToken;
      const tokenString = await token('httpAuth', []);

      if (tokenString) {
        headerParameters['Authorization'] = `Bearer ${tokenString}`;
      }
    }
    const response = await this.request(
      {
        path: `/user_todo_api/users/{userId}/profile`.replace(
          `{${'userId'}}`,
          encodeURIComponent(String(requestParameters.userId)),
        ),
        method: 'PUT',
        headers: headerParameters,
        query: queryParameters,
        body: UserProfileToJSON(requestParameters.userProfile),
      },
      initOverrides,
    );

    return new runtime.JSONApiResponse(response, (jsonValue) => UserAccountFromJSON(jsonValue));
  }

  /**
   */
  async putUserTodoApiUsersUseridProfile(
    requestParameters: PutUserTodoApiUsersUseridProfileRequest,
    initOverrides?: RequestInit | runtime.InitOverrideFunction,
  ): Promise<UserAccount> {
    const response = await this.putUserTodoApiUsersUseridProfileRaw(requestParameters, initOverrides);
    return await response.value();
  }
}
