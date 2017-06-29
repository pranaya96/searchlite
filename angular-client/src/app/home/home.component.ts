import { Component, OnInit } from '@angular/core';
import {SearchService} from '../search.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  term: string;
  results: string[];
  title = 'Got qWest?';

  constructor(private searchService: SearchService) {}

  search() {
    if (this.term === 'dog')
    {
      this.results = ['file1.pdf'];
    }
    if (this.term == 'Nepal')
    {
      this.results = ['Nepal is a great place'];
    }
    if (this.term == 'America')
    {
      this.results = ['Some link to America'];
    }
    

  }

}
