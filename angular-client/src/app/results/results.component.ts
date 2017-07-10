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
  p:number = 1;
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
      this.myHero = data;
    });  
  }

  nightMode(){
    if(this.p == 1){
      this.cardColor = "blue";
      this.p=0;
    }
    else{
      this.cardColor = "white";
      this.p=1;

    }
      
     }


  moreResults(){
    this.numPerPage = this.numPerPage + 10;
    
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
 

