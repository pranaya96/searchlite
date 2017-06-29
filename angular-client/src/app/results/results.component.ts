import { Component, OnInit } from '@angular/core';
import {SearchService} from '../search.service';


@Component({
  selector: 'app-results',
  templateUrl: 'results.component.html',
  styleUrls: ['results.component.css']
})
export class ResultsComponent{
	item:string;
	Results:string[];
	
	title:string;
	myHero:string;

constructor(private searchService: SearchService) {}


getSearch() {
    if (this.item == 'Scandal')
    {
      this.Results = ['Disney movies are trash. Dont @ me'];
    }
    if (this.item == 'Nepal')
    {
      this.Results = ['Nepal is the greatest place'];
    }
    if (this.item == 'Help')
    {
      this.Results = ['Sending help now!'];
    }
    

  }

}