import { Component, OnInit } from '@angular/core';
import {SearchService} from '../search.service';
import {MdSnackBar} from '@angular/material';
import {ActivatedRoute, Params} from '@angular/router';

@Component({
  selector: 'app-results',
  templateUrl: 'results.component.html',
  styleUrls: ['results.component.css']
})
export class ResultsComponent implements OnInit{
  item: string;
	Results:string[]= ["Disney movies are trash. Dont @ me", "Nepal is the greatest place", "Sending help now!"];

	title:string = "Results";
	myHero:string[] = ["Results 1", "Results 2", "Results 3", "Results 4", "Results 5"];
	subTitle:string;

  constructor(
    private searchService: SearchService, 
    private snackBar: MdSnackBar,
    private activatedRoute: ActivatedRoute) {}
    
  ngOnInit() {
    let term = '';
    this.activatedRoute.params.subscribe((params: Params) => {
        term = params['term'];
      });
    this.searchService.search(term).subscribe((data: string[])=>{
      this.myHero = data;
    });  
  }

    openSnackBar() {
    this.snackBar.openFromComponent(MySnackBar, {
      duration: 500,
    });
  }
}
 
@Component({
  selector: 'my-snack-bar',
  template: '<div>Hello World</div>',
})
export class MySnackBar {}
