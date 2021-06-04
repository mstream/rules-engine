import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api/api.service';
import { EventsService, EventType } from 'src/app/services/events/events.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent implements OnInit{
  modeSwitchText = "Simple Mode";
  isChecked = false;

  constructor(private events: EventsService){}
  
  ngOnInit(){

  }

  onAdvancedModeToggleChange(event: any){

    if(this.isChecked) {
      this.modeSwitchText = "Simple Mode";
          this.events.sendMessage({type: 'SIMPLE_MODE'});
    } else {
      this.modeSwitchText = "Advance Mode";
          this.events.sendMessage({type: 'ADVANCE_MODE'});
    }

    this.isChecked = !this.isChecked;
    // console.log(event);
    // switch(event){
    //   case "advance":
    //       this.isChecked = true;
    //       this.modeSwitchText = "Advance Mode";
    //       this.events.sendMessage({type: 'ADVANCE_MODE'});
    //       break;
    //   default:
    //       this.isChecked = false;
    //       this.modeSwitchText = "Simple Mode";
    //       this.events.sendMessage({type: 'SIMPLE_MODE'});
    // }
    // console.log(this.modeSwitchText);
 }

 onReload() {
  this.events.sendMessage({type: EventType.RELOAD_REQUESTED});
 }

 onSave() {
  this.events.sendMessage({type: EventType.SAVE_REQUESTED});
 }
}




