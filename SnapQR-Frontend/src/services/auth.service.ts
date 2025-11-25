import { Injectable } from '@angular/core';

import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl: string = `${environment.apiUrl}/auth`;
  private userUrl = `${environment.apiUrl}/user`;

  constructor(private http: HttpClient) { }

  // Register
  register(payload : {username: string, email: string, password: string}) : Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, payload);
  }

  //Login
  login(payload : {email: string, password: string}): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, payload);
  }

  //details about current user
  getCurrentUser(): Observable<any> {
    return this.http.get(`${this.userUrl}/me`);
  }


  isLoggedIn(){
    return !! localStorage.getItem('token');
  }

  getToken(){
    return localStorage.getItem('token');
  }
}

