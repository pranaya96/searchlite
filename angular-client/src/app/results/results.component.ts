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

  item:string;
  title:string = "Results";
  myHero:string[];
  color:number = 1;
  numPerPage:number =10;
  cardColor:string = "white";
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
      for(var _i =0; _i < data.length; ++_i){
        // console.log(data[_i]);
        var parsedStr = data[_i].slice(3);
        var newString = parsedStr.replace(/_/gi, "/");
        data[_i] = newString;
        
      }
      this.myHero = data;
    });  
  }

  nightMode(){
    if(this.color == 1){
      this.cardColor = "blue";
      this.color=0;
    }
    else{
      this.cardColor = "white";
      this.color=1;

    }
      
     }

  moreResults(){
    this.numPerPage = this.numPerPage + 10;
    
  }
}

 


