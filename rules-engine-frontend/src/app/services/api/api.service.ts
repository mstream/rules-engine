import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {  
  configUrl = "http://localhost:5050/admin/rules";

  constructor(private httpClient: HttpClient) { }

  getRules(): Observable<string>{
    return this.httpClient.get(this.configUrl, {responseType: "text"})
    //return of('[{:name "HOUSEHOLD_IS_BLOCKED" :conditions [{"IS_HOUSEHOLD_BLOCKED" true}] :actions ["RETURN_STREAMING_FORBIDDEN_RESPONSE"]}{:name "COUNTRY_IS_BLOCKED" :conditions [{"IS_HOUSEHOLD_BLOCKED" true}] :actions ["RETURN_STREAMING_FORBIDDEN_RESPONSE"]}]');
  }

  updateRules(rules: string): Observable<any>{
    console.log("updateRules: " + rules);
    return this.httpClient.put(this.configUrl, rules);
  }
}
