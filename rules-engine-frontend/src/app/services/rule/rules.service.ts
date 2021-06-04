import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
import { EventsService } from '../events/events.service';

@Injectable({
  providedIn: 'root'
})
export class RulesService {

  rules: string = '[{:name "HOUSEHOLD_IS_BLOCKED" :conditions [{"IS_HOUSEHOLD_BLOCKED" true}] :actions ["RETURN_STREAMING_FORBIDDEN_RESPONSE"]}{:name "COUNTRY_IS_BLOCKED" :conditions [{"IS_HOUSEHOLD_BLOCKED" true}] :actions ["RETURN_STREAMING_FORBIDDEN_RESPONSE"]}]';

  constructor(private api: ApiService, private events: EventsService) { }

  
}
