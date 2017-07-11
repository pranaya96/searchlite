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
  Results:string[];
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
      if(term =="undefined"){
        //this.Results[] = "No results found";
        this.Results.length = 1;

      }else{
      for(var _i =0; _i < data.length; ++_i){
        var parsedStr = data[_i].slice(3);
        data[_i] = parsedStr;
      }
      this.Results = data;
    }

    });  
  }
  Scroll(){


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
  getSearch(){
    window.location.reload(true);
  }
  lessResults(){
    if(this.numPerPage > 10){
      this.numPerPage = this.numPerPage - 10;
    }
    else{
    this.numPerPage = 10
  }
    
  }
  }

 
// @Component({
//   selector: 'my-snack-bar',
//   template: '<div>Hello World</div>',
// })
// export class MySnackBar {}
//   private searchService: SearchService;
//   private toasterService: ToasterService;
//     getSearch() {
//       if (this.item == 'Scandal')
//       {
//         this.subTitle = this.Results[0];
//       }
//       if (this.item == 'Nepal')
//       {
//         this.subTitle = this.Results[1];
//       }
//       if (this.item == 'Help')
//       {
//         this.subTitle = this.Results[2];
//       }
//     }
//     nightMode(){
      
//     }
// }
 

