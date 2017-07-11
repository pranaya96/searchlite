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
  callTime:string;
  constructor(
    private searchService: SearchService, 
    private snackBar: MdSnackBar,
    private activatedRoute: ActivatedRoute) {}
   
  ngOnInit() {
    var start = performance.now(); //to check the time of function call
    let term = '';
    this.activatedRoute.params.subscribe((params: Params) => {
        term = params['term'];
        term = term.toLowerCase(); //for case insensitive search
      });
    this.searchService.search(term).subscribe((data: string[])=>{
      this.callTime = '';
      //case when there is empty search
      if (term=="undefined"){
        this.callTime = "No results found";
      }
      else{
        for(var _i =0; _i < data.length; ++_i){
          var parsedStr = data[_i].slice(3); //parse the filename 
          var newString = parsedStr.replace(/_/gi, "/"); //get url from the filename
          data[_i] = newString;
          
        }
    
        this.myHero = data; 
        var mylen = this.myHero.length;
        
        //case when there is no result for the query terms
        if (mylen == 0){
          this.callTime = "No results found";
        }
        
        else{
          var end = performance.now();
          this.callTime  = "Call took " + (end - start) + " milliseconds to get "+ mylen+" results";
          
        }
      }
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

 


