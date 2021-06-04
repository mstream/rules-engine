import { Component, OnInit, Input } from '@angular/core';
import { ApiService } from 'src/app/services/api/api.service';
import { RulesService } from 'src/app/services/rule/rules.service';
import { EventsService, EventType } from 'src/app/services/events/events.service';
import * as edn from 'jsedn';

@Component({
  selector: 'app-advanced',
  templateUrl: './advanced.component.html',
  styleUrls: ['./advanced.component.less']
})
export class AdvancedComponent implements OnInit {

  rules;

  title = 'rules-engine-frontend';

  constructor(private apiService: ApiService, private rulesService: RulesService, private events: EventsService) { }

  ngOnInit() {
    this.fetchRules();
    this.events.getMessage()
      .subscribe(e => {
        if (e.type === EventType.SAVE_REQUESTED) {
          this.onSave();
        }
        if (e.type === EventType.RELOAD_REQUESTED) {
            this.fetchRules();
        }
      })
  }

  fetchRules() {
    this.apiService.getRules()
      .subscribe(
        (rules: string) => {
          this.rules = rules;
        },
        error => {
          alert(JSON.stringify(error));
        }
      );
  }

  onSave() {
    this.apiService.updateRules(this.rules)
      .subscribe(
        ignored => alert("Saved"),
        error => console.log(JSON.stringify(error)) 
      );
  }

}
