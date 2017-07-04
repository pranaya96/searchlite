import { Component, OnInit } from '@angular/core';
import {SearchService} from '../search.service';
import {ToasterModule, ToasterService} from 'angular2-toaster';

@Component({
  selector: 'app-results',
  templateUrl: 'results.component.html',
  styleUrls: ['results.component.css']
})
export class ResultsComponent{
	item:string;
	Results:string[]= ["Disney movies are trash. Dont @ me", "Nepal is the greatest place", "Sending help now!"];

	title:string = "Results";
	myHero:string[] = ["Results 1", "Results 2", "Results 3", "Results 4", "Results 5"];
	subTitle:string;

  private searchService: SearchService;
  private toasterService: ToasterService;
    getSearch() {
      if (this.item == 'Scandal')
      {
        this.subTitle = this.Results[0];
      }
      if (this.item == 'Nepal')
      {
        this.subTitle = this.Results[1];
      }
      if (this.item == 'Help')
      {
        this.subTitle = this.Results[2];
      }
    }
    nightMode(){
      
    }

    Toast(){
      var toast: Toast = {
      type: 'info',
      title: 'Here is a Toast Title',
      body: 'Here is a Toast Body'
    };

      this.toasterService.pop(toast);}
}
 

