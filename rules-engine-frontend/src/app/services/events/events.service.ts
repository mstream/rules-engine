import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  private subject: Subject<any> = new Subject<any>();

  constructor() { }

  sendMessage(message) {
    this.subject.next(message);
  }

  clearMessage() {
    this.subject.next();
  }

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }

}

export enum EventType {
  SAVE_REQUESTED,
  RELOAD_REQUESTED
}
