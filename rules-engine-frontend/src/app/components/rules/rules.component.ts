import { Component, OnInit } from '@angular/core';
import { ApiService } from 'src/app/services/api/api.service';
import { EventsService } from 'src/app/services/events/events.service';
import { RulesService } from 'src/app/services/rule/rules.service';

@Component({
  selector: 'app-rules',
  templateUrl: './rules.component.html',
  styleUrls: ['./rules.component.less']
})
export class RulesComponent implements OnInit {

  isAdvancedMode = false;
  title = 'rules-engine-frontend';

  constructor(private apiService: ApiService, private events: EventsService, private rulesService: RulesService) { }

  ngOnInit() {
    this.events.getMessage().subscribe(event => {
      console.log(event);
      if(event.type === 'ADVANCE_MODE') {
          this.isAdvancedMode = true;
      } else if(event.type === 'SIMPLE_MODE') {
        this.isAdvancedMode = false;
      }
    })

    this.apiService.getRules()
    .subscribe(
        (rules: string) => {
          this.rulesService.rules = rules;
          this.events.sendMessage({type: 'RULES_REFRESHED_FROM_SERVER'});
        },
        error => {
          alert(JSON.stringify(error));
          this.events.sendMessage({type: 'RULES_REFRESHED_FROM_SERVER'}); //TODO REMOVE!!!!!!!!
        }
    );
  }

}
