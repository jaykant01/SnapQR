import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class QrService {

  private baseUrl = `${environment.apiUrl}/qrcode`

  constructor(private http: HttpClient) { }

  // generate qr
  generateQr(payload: any): Observable<Blob> {
    return this.http.post<Blob>(`${this.baseUrl}/generate`, payload, {
      responseType: 'blob' as 'json'
    });
  }


  // get qr history
  getHistory(): Observable<any> {
    return this.http.get(`${this.baseUrl}/history`);
  }

  // download qr by id
  downloadById(id: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/download/${id}`, {
      responseType: 'blob'
    });
  }

  //Delete QR By Id
  deleteById(id: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`, {})
  }


}
