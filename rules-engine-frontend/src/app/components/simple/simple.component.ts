import { Component, OnInit, Input } from '@angular/core';
import * as edn from 'jsedn';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { RulesService } from 'src/app/services/rule/rules.service';
import { EventsService, EventType } from 'src/app/services/events/events.service';
import { ApiService } from 'src/app/services/api/api.service';

@Component({
  selector: 'app-simple',
  templateUrl: './simple.component.html',
  styleUrls: ['./simple.component.less']
})
export class SimpleComponent implements OnInit {
  selectedRule;
  orderedRuleNames = [];
  rulesDictionary = {};

  constructor(private apiService: ApiService, private events: EventsService) { }

  ngOnInit() {
    this.fetchRules();

    this.events.getMessage().subscribe(event => {
      if (event.type === EventType.RELOAD_REQUESTED) {
        this.fetchRules();
      }

      if (event.type === EventType.SAVE_REQUESTED) {
        this.doSave();
      }
    });
  }

  fetchRules() {
    this.apiService.getRules()
      .subscribe(
        (rules: string) => {
          console.log("fetchRules HERE");
          this.displayRules(rules);
        },
        error => {
          alert(JSON.stringify(error));
        }
      );
  }

  displayRules(rules) {
    
    let rulesJs = edn.toJS(edn.parse(rules));

    console.log("RULES");
    console.log(rules);
    console.log(rulesJs);
    

    this.orderedRuleNames = rulesJs.map(rule => rule[':name']);

    console.log(this.orderedRuleNames);

    this.rulesDictionary = rulesJs.reduce((map, rule) => {
      map[rule[':name']] = rule;
      return map;
    }, {});
    if(this.orderedRuleNames.length > 0) {
      this.editRule(0);
    }
  }

  doSave() {
    let rulesToSave = [];
    for (let i in this.orderedRuleNames) {
      rulesToSave.push(this.rulesDictionary[this.orderedRuleNames[i]]);
    }

    let ednString = edn.encode(rulesToSave);
    this.apiService.updateRules(ednString)
      .subscribe(
        ignored => {
          alert("Saved");
          this.fetchRules();
        },
        error => console.log(JSON.stringify(error))
      );
  }

  onRuleClicked(position) {
    this.editRule(position);
  }

  editRule(position) {
    let ruleName = this.orderedRuleNames[position];
    let rule = this.rulesDictionary[ruleName];
    this.selectedRule = {
      position: position,
      name: rule[':name'],
      conditions: rule[':conditions'].map(c => {
        let name = Object.keys(c)[0];
        return name + ':' + c[name];
      }).join(','),
      actions: rule[':actions'].join(',')
    };
  }

  onRuleMove(event) {
    moveItemInArray(this.orderedRuleNames, event.previousIndex, event.currentIndex);
  }

  onRuleNameChange() {
    let oldRuleName = this.orderedRuleNames[this.selectedRule.position];

    this.orderedRuleNames[this.selectedRule.position] = this.selectedRule.name;
    this.rulesDictionary[this.selectedRule.name] = this.rulesDictionary[oldRuleName];
    this.rulesDictionary[this.selectedRule.name][':name'] = this.selectedRule.name;

    delete this.rulesDictionary[oldRuleName];
  }

  onActionsChange() {
    let rule = this.rulesDictionary[this.orderedRuleNames[this.selectedRule.position]];
    if(this.selectedRule.actions.trim() === '') {
      rule[':actions'] = [];
      return;
    }
    rule[':actions'] = this.selectedRule.actions.split(',');
  }

  onConditionsChange() {

    if(this.selectedRule.conditions !== '' && /^([a-zA-Z0-9-_]+:(true|false))(,[a-zA-Z0-9-_]+:(true|false))*$/.test(this.selectedRule.conditions) === false) {
      console.log('Not match');
      return;
    }

    let rule = this.rulesDictionary[this.orderedRuleNames[this.selectedRule.position]];
    if(this.selectedRule.conditions.trim() === '') {
      rule[':conditions'] = [];
      return;
    }

    
    rule[':conditions'] = this.selectedRule.conditions.split(',').map(c => {
      let nameAndBool = c.split(':');
      let conditonObj = {};
      conditonObj[nameAndBool[0]] = nameAndBool[1].toLowerCase() === 'true';
      return conditonObj;
    });
  }
}
